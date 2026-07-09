package org.indunet.fastproto.ros2.bag.internal;

import org.indunet.fastproto.ros2.bag.Ros2BagMessage;
import org.indunet.fastproto.ros2.bag.Ros2BagTopic;
import org.indunet.fastproto.ros2.bag.Ros2MessageDecoder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

final class McapBagBackend implements BagBackend {
    private final Map<Integer, Ros2BagTopic> topicsById;
    private final Map<Integer, McapReader.McapChannel> channelsById;
    private final List<McapReader.McapMessage> messages;

    McapBagBackend(Path mcapFile) throws IOException {
        McapReader reader = McapReader.read(mcapFile);
        this.channelsById = new LinkedHashMap<Integer, McapReader.McapChannel>(reader.channelsById());
        this.messages = new ArrayList<McapReader.McapMessage>(reader.messages());
        this.topicsById = new LinkedHashMap<Integer, Ros2BagTopic>();

        Map<Integer, String> schemasById = reader.schemasById();
        for (McapReader.McapChannel channel : channelsById.values()) {
            String type = schemasById.get(channel.getSchemaId());
            if (type == null) {
                type = "";
            }
            topicsById.put(channel.getId(), new Ros2BagTopic(
                    channel.getId(),
                    channel.getTopic(),
                    type,
                    channel.getSerializationFormat(),
                    channel.getOfferedQosProfiles()
            ));
        }
    }

    @Override
    public List<Ros2BagTopic> topics() {
        return new ArrayList<Ros2BagTopic>(topicsById.values());
    }

    @Override
    public void forEachMessage(String topic, Consumer<Ros2BagMessage> consumer) throws IOException {
        Integer channelId = null;
        if (topic != null) {
            channelId = findChannelId(topic);
        }

        for (McapReader.McapMessage message : messages) {
            if (channelId != null && message.getChannelId() != channelId) {
                continue;
            }

            Ros2BagTopic bagTopic = topicsById.get(message.getChannelId());
            if (bagTopic == null) {
                continue;
            }

            byte[] payload = message.getPayload();
            Object decoded = Ros2MessageDecoder.decode(bagTopic.getType(), payload);
            consumer.accept(new Ros2BagMessage(
                    bagTopic.getName(),
                    bagTopic.getType(),
                    message.getLogTime(),
                    payload,
                    decoded
            ));
        }
    }

    @Override
    public void close() {
    }

    private int findChannelId(String topic) throws IOException {
        for (Ros2BagTopic bagTopic : topicsById.values()) {
            if (bagTopic.getName().equals(topic)) {
                return bagTopic.getId();
            }
        }
        throw new IOException("Unknown rosbag2 topic: " + topic);
    }
}
