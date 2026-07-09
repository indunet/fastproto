package org.indunet.fastproto.ros2.bag.internal;

import org.indunet.fastproto.ros2.bag.Ros2BagMessage;
import org.indunet.fastproto.ros2.bag.Ros2BagTopic;
import org.indunet.fastproto.ros2.bag.Ros2MessageDecoder;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

final class Sqlite3BagBackend implements BagBackend {
    private final Connection connection;
    private final Map<Integer, Ros2BagTopic> topicsById;

    Sqlite3BagBackend(Path database) throws IOException {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + database.toAbsolutePath());
            this.topicsById = loadTopics(connection);
        } catch (SQLException e) {
            throw new IOException("Failed to open rosbag2 SQLite database: " + database, e);
        }
    }

    @Override
    public List<Ros2BagTopic> topics() {
        return new ArrayList<Ros2BagTopic>(topicsById.values());
    }

    @Override
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
}
