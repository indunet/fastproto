package org.indunet.fastproto.ros2.bag;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Ros2McapBagReaderTest {
    @TempDir
    Path tempDir;

    @Test
    void opensMcapBagDirectoryFromMetadata() throws Exception {
        Path fixture = Paths.get("src/test/resources/fixtures/mcap/cdr_test").toAbsolutePath();
        try (Ros2BagReader reader = Ros2BagReader.open(fixture)) {
            List<Ros2BagTopic> topics = reader.topics();
            assertEquals(2, topics.size());
            assertEquals("/test_topic", topics.get(0).getName());
            assertEquals("test_msgs/msg/BasicTypes", topics.get(0).getType());
            assertEquals("cdr", topics.get(0).getSerializationFormat());

            List<Ros2BagMessage> messages = reader.readMessages();
            assertEquals(7, messages.size());
            assertFalse(messages.get(0).isDecoded());
            assertTrue(messages.get(0).getPayload().length > 0);
        }
    }

    @Test
    void opensSingleMcapFile() throws Exception {
        Path mcap = Paths.get("src/test/resources/fixtures/mcap/cdr_test/cdr_test_0.mcap").toAbsolutePath();
        try (Ros2BagReader reader = Ros2BagReader.open(mcap)) {
            List<Ros2BagMessage> messages = reader.readMessages("/array_topic");
            assertEquals(4, messages.size());
            for (Ros2BagMessage message : messages) {
                assertEquals("/array_topic", message.getTopic());
                assertEquals("test_msgs/msg/Arrays", message.getType());
            }
        }
    }

    @Test
    void failsWhenMetadataReferencesMissingMcapFile() throws Exception {
        Path bag = tempDir.resolve("broken_mcap_bag");
        Files.createDirectories(bag);
        String metadata = "rosbag2_bagfile_information:\n"
                + "  version: 8\n"
                + "  storage_identifier: mcap\n"
                + "  relative_file_paths:\n"
                + "    - missing_0.mcap\n";
        Files.write(bag.resolve("metadata.yaml"), metadata.getBytes(StandardCharsets.UTF_8));

        IOException exception = assertThrows(IOException.class, () -> Ros2BagReader.open(bag));
        assertTrue(exception.getMessage().contains("missing MCAP file"));
    }
}
