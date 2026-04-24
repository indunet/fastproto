package org.indunet.fastproto.ros2.bag;

import org.yaml.snakeyaml.Yaml;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Reader for rosbag2 SQLite3 bags.
 */
public final class Ros2BagReader implements Closeable {
    private final Connection connection;
    private final Map<Integer, Ros2BagTopic> topicsById;

    private Ros2BagReader(Connection connection) throws SQLException {
        this.connection = connection;
        this.topicsById = loadTopics(connection);
    }

    public static Ros2BagReader open(Path path) throws IOException {
        Path database = resolveSqliteDatabase(path);

        try {
            return new Ros2BagReader(DriverManager.getConnection("jdbc:sqlite:" + database.toAbsolutePath()));
        } catch (SQLException e) {
            throw new IOException("Failed to open rosbag2 SQLite database: " + database, e);
        }
    }

    public List<Ros2BagTopic> topics() {
        return Collections.unmodifiableList(new ArrayList<Ros2BagTopic>(topicsById.values()));
    }

    public List<Ros2BagMessage> readMessages() throws IOException {
        return readMessages(null);
    }

    public List<Ros2BagMessage> readMessages(String topic) throws IOException {
        final List<Ros2BagMessage> messages = new ArrayList<Ros2BagMessage>();
        forEachMessage(topic, new Consumer<Ros2BagMessage>() {
            @Override
            public void accept(Ros2BagMessage message) {
                messages.add(message);
            }
        });
        return messages;
    }

    public Map<String, List<Ros2BagMessage>> readMessagesByTopic() throws IOException {
        final Map<String, List<Ros2BagMessage>> messagesByTopic = new LinkedHashMap<String, List<Ros2BagMessage>>();
        for (Ros2BagTopic topic : topicsById.values()) {
            messagesByTopic.put(topic.getName(), new ArrayList<Ros2BagMessage>());
        }

        forEachMessage(new Consumer<Ros2BagMessage>() {
            @Override
            public void accept(Ros2BagMessage message) {
                List<Ros2BagMessage> messages = messagesByTopic.get(message.getTopic());
                if (messages == null) {
                    messages = new ArrayList<Ros2BagMessage>();
                    messagesByTopic.put(message.getTopic(), messages);
                }
                messages.add(message);
            }
        });

        return messagesByTopic;
    }

    public void forEachMessage(Consumer<Ros2BagMessage> consumer) throws IOException {
        forEachMessage(null, consumer);
    }

    public void forEachMessage(String topic, Consumer<Ros2BagMessage> consumer) throws IOException {
        String sql = "select topic_id, timestamp, data from messages";
        if (topic != null) {
            sql += " where topic_id = ?";
        }
        sql += " order by timestamp, id";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (topic != null) {
                statement.setInt(1, findTopicId(topic));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int topicId = resultSet.getInt("topic_id");
                    Ros2BagTopic bagTopic = topicsById.get(topicId);
                    if (bagTopic == null) {
                        continue;
                    }

                    byte[] payload = resultSet.getBytes("data");
                    Object decoded = Ros2MessageDecoder.decode(bagTopic.getType(), payload);
                    consumer.accept(new Ros2BagMessage(
                            bagTopic.getName(),
                            bagTopic.getType(),
                            resultSet.getLong("timestamp"),
                            payload,
                            decoded
                    ));
                }
            }
        } catch (SQLException e) {
            throw new IOException("Failed to read rosbag2 messages.", e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IOException("Failed to close rosbag2 SQLite database.", e);
        }
    }

    private int findTopicId(String topic) throws IOException {
        for (Ros2BagTopic bagTopic : topicsById.values()) {
            if (bagTopic.getName().equals(topic)) {
                return bagTopic.getId();
            }
        }

        throw new IOException("Unknown rosbag2 topic: " + topic);
    }

    private static Map<Integer, Ros2BagTopic> loadTopics(Connection connection) throws SQLException {
        Map<Integer, Ros2BagTopic> topics = new LinkedHashMap<Integer, Ros2BagTopic>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "select id, name, type, serialization_format, offered_qos_profiles from topics order by id")) {
            while (resultSet.next()) {
                Ros2BagTopic topic = new Ros2BagTopic(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("serialization_format"),
                        resultSet.getString("offered_qos_profiles")
                );
                topics.put(topic.getId(), topic);
            }
        }

        return topics;
    }

    private static Path resolveSqliteDatabase(Path path) throws IOException {
        if (Files.isRegularFile(path)) {
            return path;
        }

        if (!Files.isDirectory(path)) {
            throw new IOException("rosbag2 path does not exist: " + path);
        }

        Path metadata = path.resolve("metadata.yaml");
        if (Files.exists(metadata)) {
            Path fromMetadata = sqliteDatabaseFromMetadata(path, metadata);
            if (fromMetadata != null) {
                if (!Files.isRegularFile(fromMetadata)) {
                    throw new IOException("rosbag2 metadata references missing SQLite file: " + fromMetadata);
                }
                return fromMetadata;
            }
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.db3")) {
            for (Path candidate : stream) {
                return candidate;
            }
        }

        throw new IOException("No rosbag2 SQLite .db3 file found under: " + path);
    }

    @SuppressWarnings("unchecked")
    private static Path sqliteDatabaseFromMetadata(Path bagDirectory, Path metadata) throws IOException {
        Yaml yaml = new Yaml();
        Object loaded;
        try (InputStream inputStream = Files.newInputStream(metadata)) {
            loaded = yaml.load(inputStream);
        }

        if (!(loaded instanceof Map)) {
            return null;
        }

        Object rosbag2BagfileInformation = ((Map<String, Object>) loaded).get("rosbag2_bagfile_information");
        if (!(rosbag2BagfileInformation instanceof Map)) {
            return null;
        }

        Map<String, Object> information = (Map<String, Object>) rosbag2BagfileInformation;
        Object storageIdentifier = information.get("storage_identifier");
        if (storageIdentifier != null && !"sqlite3".equals(storageIdentifier.toString())) {
            throw new IOException("Unsupported rosbag2 storage identifier: " + storageIdentifier);
        }

        Object relativeFilePaths = information.get("relative_file_paths");
        if (!(relativeFilePaths instanceof List)) {
            return null;
        }

        for (Object relativeFilePath : (List<Object>) relativeFilePaths) {
            if (relativeFilePath != null && relativeFilePath.toString().endsWith(".db3")) {
                return bagDirectory.resolve(relativeFilePath.toString());
            }
        }

        return null;
    }
}
