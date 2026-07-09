package org.indunet.fastproto.ros2.bag;

import java.util.Arrays;

/**
 * One message row read from a rosbag2 storage file.
 */
public final class Ros2BagMessage {
    private final String topic;
    private final String type;
    private final long timestamp;
    private final byte[] payload;
    private final Object decodedMessage;

    public Ros2BagMessage(String topic, String type, long timestamp, byte[] payload, Object decodedMessage) {
        this.topic = topic;
        this.type = type;
        this.timestamp = timestamp;
        this.payload = payload == null ? new byte[0] : Arrays.copyOf(payload, payload.length);
        this.decodedMessage = decodedMessage;
    }

    public String getTopic() {
        return topic;
    }

    public String getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public byte[] getPayload() {
        return Arrays.copyOf(payload, payload.length);
    }

    public Object getDecodedMessage() {
        return decodedMessage;
    }

    public boolean isDecoded() {
        return decodedMessage != null;
    }
}
