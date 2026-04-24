package org.indunet.fastproto.ros2.bag;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.std_msgs.msg.Bool;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Ros2BagReaderTest {
    @TempDir
    Path tempDir;

    @Test
    void readsSqliteRosbag2MessagesAndDecodesKnownTypes() throws Exception {
        Path bag = tempDir.resolve("sample_bag");
        Files.createDirectories(bag);
        Path database = bag.resolve("sample_bag_0.db3");

        Header header = Header.builder()
                .stamp(Time.builder().sec(12).nanosec(34L).build())
                .frameId("map")
                .build();
        createDatabase(database,
                topic(1, "/header", "std_msgs/msg/Header"),
                message(1, 1, 99L, header.encode()));
        createMetadata(bag);

        try (Ros2BagReader reader = Ros2BagReader.open(bag)) {
            List<Ros2BagTopic> topics = reader.topics();
            assertEquals(1, topics.size());
            assertEquals("/header", topics.get(0).getName());
            assertEquals("std_msgs/msg/Header", topics.get(0).getType());

            List<Ros2BagMessage> messages = reader.readMessages("/header");
            assertEquals(1, messages.size());
            assertEquals(99L, messages.get(0).getTimestamp());
            assertTrue(messages.get(0).isDecoded());
            assertEquals(header, messages.get(0).getDecodedMessage());
        }
    }

    @Test
    void keepsRawPayloadForUnknownTypes() throws Exception {
        Path database = tempDir.resolve("unknown.db3");
        byte[] payload = new byte[]{0, 1, 2};
        createDatabase(database,
                topic(1, "/header", "custom_msgs/msg/Unknown"),
                message(1, 1, 99L, payload));

        try (Ros2BagReader reader = Ros2BagReader.open(database)) {
            List<Ros2BagMessage> messages = reader.readMessages();
            assertEquals(1, messages.size());
            assertFalse(messages.get(0).isDecoded());
            assertEquals(3, messages.get(0).getPayload().length);
        }
    }

    @Test
    void readsMultipleTopicsInTimestampOrder() throws Exception {
        Path bag = tempDir.resolve("multi_topic_bag");
        Files.createDirectories(bag);
        Path database = bag.resolve("sample_bag_0.db3");

        Header header = Header.builder()
                .stamp(Time.builder().sec(7).nanosec(8L).build())
                .frameId("base_link")
                .build();
        Bool bool = Bool.builder().data(true).build();

        createDatabase(database,
                Arrays.asList(
                        topic(1, "/header", "std_msgs/msg/Header"),
                        topic(2, "/enabled", "std_msgs/msg/Bool")
                ),
                Arrays.asList(
                        message(1, 1, 300L, header.encode()),
                        message(2, 2, 100L, bool.encode()),
                        message(3, 1, 200L, header.encode())
                ));
        createMetadata(bag);

        try (Ros2BagReader reader = Ros2BagReader.open(bag)) {
            List<Ros2BagTopic> topics = reader.topics();
            assertEquals(2, topics.size());
            assertEquals("/header", topics.get(0).getName());
            assertEquals("/enabled", topics.get(1).getName());

            List<Ros2BagMessage> messages = reader.readMessages();
            assertEquals(3, messages.size());
            assertEquals(100L, messages.get(0).getTimestamp());
            assertEquals("/enabled", messages.get(0).getTopic());
            assertEquals(bool, messages.get(0).getDecodedMessage());
            assertEquals(200L, messages.get(1).getTimestamp());
            assertEquals(300L, messages.get(2).getTimestamp());
        }
    }

    @Test
    void filtersMessagesByTopic() throws Exception {
        Path database = tempDir.resolve("filtered.db3");
        Header header = Header.builder()
                .stamp(Time.builder().sec(1).nanosec(2L).build())
                .frameId("odom")
                .build();
        Bool bool = Bool.builder().data(false).build();

        createDatabase(database,
                Arrays.asList(
                        topic(1, "/header", "std_msgs/msg/Header"),
                        topic(2, "/enabled", "std_msgs/msg/Bool")
                ),
                Arrays.asList(
                        message(1, 1, 10L, header.encode()),
                        message(2, 2, 20L, bool.encode()),
                        message(3, 1, 30L, header.encode())
                ));

        try (Ros2BagReader reader = Ros2BagReader.open(database)) {
            List<Ros2BagMessage> messages = reader.readMessages("/header");
            assertEquals(2, messages.size());
            assertEquals("/header", messages.get(0).getTopic());
            assertEquals("/header", messages.get(1).getTopic());
            assertEquals(10L, messages.get(0).getTimestamp());
            assertEquals(30L, messages.get(1).getTimestamp());
        }
    }

    @Test
    void groupsMessagesByTopic() throws Exception {
        Path database = tempDir.resolve("grouped.db3");
        Header header = Header.builder()
                .stamp(Time.builder().sec(3).nanosec(4L).build())
                .frameId("laser")
                .build();
        Bool bool = Bool.builder().data(true).build();

        createDatabase(database,
                Arrays.asList(
                        topic(1, "/header", "std_msgs/msg/Header"),
                        topic(2, "/enabled", "std_msgs/msg/Bool"),
                        topic(3, "/empty", "custom_msgs/msg/Empty")
                ),
                Arrays.asList(
                        message(1, 1, 50L, header.encode()),
                        message(2, 2, 10L, bool.encode()),
                        message(3, 1, 30L, header.encode())
                ));

        try (Ros2BagReader reader = Ros2BagReader.open(database)) {
            Map<String, List<Ros2BagMessage>> messagesByTopic = reader.readMessagesByTopic();

            assertEquals(3, messagesByTopic.size());
            assertEquals(2, messagesByTopic.get("/header").size());
            assertEquals(1, messagesByTopic.get("/enabled").size());
            assertEquals(0, messagesByTopic.get("/empty").size());
            assertEquals(30L, messagesByTopic.get("/header").get(0).getTimestamp());
            assertEquals(50L, messagesByTopic.get("/header").get(1).getTimestamp());
            assertEquals(bool, messagesByTopic.get("/enabled").get(0).getDecodedMessage());
        }
    }

    @Test
    void failsWhenMetadataReferencesMissingSqliteFile() throws Exception {
        Path bag = tempDir.resolve("broken_bag");
        Files.createDirectories(bag);
        createMetadata(bag);

        IOException exception = assertThrows(IOException.class, () -> Ros2BagReader.open(bag));
        assertTrue(exception.getMessage().contains("missing SQLite file"));
    }

    @Test
    void failsWhenFilteringUnknownTopic() throws Exception {
        Path database = tempDir.resolve("unknown_topic.db3");
        createDatabase(database,
                topic(1, "/header", "custom_msgs/msg/Unknown"),
                message(1, 1, 99L, new byte[]{1}));

        try (Ros2BagReader reader = Ros2BagReader.open(database)) {
            IOException exception = assertThrows(IOException.class, () -> reader.readMessages("/missing"));
            assertTrue(exception.getMessage().contains("/missing"));
        }
    }

    private void createMetadata(Path bag) throws Exception {
        String metadata = "rosbag2_bagfile_information:\n"
                + "  version: 8\n"
                + "  storage_identifier: sqlite3\n"
                + "  relative_file_paths:\n"
                + "    - sample_bag_0.db3\n";
        Files.write(bag.resolve("metadata.yaml"), metadata.getBytes(StandardCharsets.UTF_8));
    }

    private TopicRow topic(int id, String name, String type) {
        return new TopicRow(id, name, type);
    }

    private MessageRow message(int id, int topicId, long timestamp, byte[] payload) {
        return new MessageRow(id, topicId, timestamp, payload);
    }

    private void createDatabase(Path database, TopicRow topic, MessageRow message) throws Exception {
        createDatabase(database, Arrays.asList(topic), Arrays.asList(message));
    }

    private void createDatabase(Path database, List<TopicRow> topics, List<MessageRow> messages) throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + database.toAbsolutePath());
             Statement statement = connection.createStatement()) {
            statement.execute("create table topics (id integer primary key, name text not null, type text not null, "
                    + "serialization_format text not null, offered_qos_profiles text not null)");
            statement.execute("create table messages (id integer primary key, topic_id integer not null, "
                    + "timestamp integer not null, data blob not null)");
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + database.toAbsolutePath());
             PreparedStatement topicStatement = connection.prepareStatement(
                     "insert into topics(id, name, type, serialization_format, offered_qos_profiles) values (?, ?, ?, 'cdr', '')");
             PreparedStatement messageStatement = connection.prepareStatement(
                     "insert into messages(id, topic_id, timestamp, data) values (?, ?, ?, ?)")) {
            for (TopicRow topic : topics) {
                topicStatement.setInt(1, topic.id);
                topicStatement.setString(2, topic.name);
                topicStatement.setString(3, topic.type);
                topicStatement.executeUpdate();
            }

            for (MessageRow message : messages) {
                messageStatement.setInt(1, message.id);
                messageStatement.setInt(2, message.topicId);
                messageStatement.setLong(3, message.timestamp);
                messageStatement.setBytes(4, message.payload);
                messageStatement.executeUpdate();
            }
        }
    }

    private static final class TopicRow {
        private final int id;
        private final String name;
        private final String type;

        private TopicRow(int id, String name, String type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }
    }

    private static final class MessageRow {
        private final int id;
        private final int topicId;
        private final long timestamp;
        private final byte[] payload;

        private MessageRow(int id, int topicId, long timestamp, byte[] payload) {
            this.id = id;
            this.topicId = topicId;
            this.timestamp = timestamp;
            this.payload = payload;
        }
    }
}
