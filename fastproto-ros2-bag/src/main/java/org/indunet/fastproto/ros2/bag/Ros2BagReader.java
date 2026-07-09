package org.indunet.fastproto.ros2.bag;

import org.indunet.fastproto.ros2.bag.internal.BagBackend;
import org.indunet.fastproto.ros2.bag.internal.BagBackendFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Reader for rosbag2 recordings stored as sqlite3 or MCAP files.
 */
public final class Ros2BagReader implements Closeable {
    private final BagBackend backend;

    private Ros2BagReader(BagBackend backend) {
        this.backend = backend;
    }

    public static Ros2BagReader open(Path path) throws IOException {
        return new Ros2BagReader(BagBackendFactory.open(path));
    }

    public List<Ros2BagTopic> topics() {
        return Collections.unmodifiableList(backend.topics());
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
        for (Ros2BagTopic topic : topics()) {
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
        backend.forEachMessage(topic, consumer);
    }

    @Override
    public void close() throws IOException {
        backend.close();
    }
}
