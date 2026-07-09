package org.indunet.fastproto.ros2.bag;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RealRosbagFixtureTest {
    @Test
    void readsOfficialSqliteCdrTestBag() throws Exception {
        Path fixture = Paths.get("src/test/resources/fixtures/sqlite3/cdr_test").toAbsolutePath();
        try (Ros2BagReader reader = Ros2BagReader.open(fixture)) {
            assertBagContents(reader);
        }
    }

    @Test
    void readsOfficialMcapCdrTestBag() throws Exception {
        Path fixture = Paths.get("src/test/resources/fixtures/mcap/cdr_test").toAbsolutePath();
        try (Ros2BagReader reader = Ros2BagReader.open(fixture)) {
            assertBagContents(reader);
        }
    }

    @Test
    void readsOfficialSqliteCdrTestDatabaseFile() throws Exception {
        Path database = Paths.get("src/test/resources/fixtures/sqlite3/cdr_test/cdr_test_0.db3").toAbsolutePath();
        try (Ros2BagReader reader = Ros2BagReader.open(database)) {
            assertBagContents(reader);
        }
    }

    private static void assertBagContents(Ros2BagReader reader) throws Exception {
        List<Ros2BagTopic> topics = reader.topics();
        assertEquals(2, topics.size());
        assertEquals("/test_topic", topics.get(0).getName());
        assertEquals("test_msgs/msg/BasicTypes", topics.get(0).getType());
        assertEquals("/array_topic", topics.get(1).getName());
        assertEquals("test_msgs/msg/Arrays", topics.get(1).getType());

        List<Ros2BagMessage> messages = reader.readMessages();
        assertEquals(7, messages.size());
        for (int index = 1; index < messages.size(); index++) {
            assertTrue(messages.get(index).getTimestamp() >= messages.get(index - 1).getTimestamp());
        }
        for (Ros2BagMessage message : messages) {
            assertFalse(message.isDecoded());
            assertTrue(message.getPayload().length > 0);
        }

        List<Ros2BagMessage> testTopicMessages = reader.readMessages("/test_topic");
        assertEquals(3, testTopicMessages.size());
        for (Ros2BagMessage message : testTopicMessages) {
            assertEquals("/test_topic", message.getTopic());
        }

        Map<String, List<Ros2BagMessage>> messagesByTopic = reader.readMessagesByTopic();
        assertEquals(2, messagesByTopic.size());
        assertEquals(3, messagesByTopic.get("/test_topic").size());
        assertEquals(4, messagesByTopic.get("/array_topic").size());
    }
}
