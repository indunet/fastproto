package org.indunet.fastproto.ros2.bag.internal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal MCAP reader for rosbag2 recordings.
 */
final class McapReader {
    private static final byte[] MAGIC = new byte[]{
            (byte) 0x89, 'M', 'C', 'A', 'P', '0', '\r', '\n'
    };

    private static final int OP_SCHEMA = 0x03;
    private static final int OP_CHANNEL = 0x04;
    private static final int OP_MESSAGE = 0x05;
    private static final int OP_CHUNK = 0x06;

    private final Map<Integer, String> schemasById = new HashMap<Integer, String>();
    private final Map<Integer, McapChannel> channelsById = new LinkedHashMap<Integer, McapChannel>();
    private final List<McapMessage> messages = new ArrayList<McapMessage>();

    static McapReader read(Path path) throws IOException {
        byte[] data = Files.readAllBytes(path);
        McapReader reader = new McapReader();
        reader.parse(data);
        return reader;
    }

    Map<Integer, String> schemasById() {
        return Collections.unmodifiableMap(schemasById);
    }

    Map<Integer, McapChannel> channelsById() {
        return Collections.unmodifiableMap(channelsById);
    }

    List<McapMessage> messages() {
        return Collections.unmodifiableList(messages);
    }

    private void parse(byte[] data) throws IOException {
        if (data.length < MAGIC.length || !Arrays.equals(Arrays.copyOf(data, MAGIC.length), MAGIC)) {
            throw new IOException("Invalid MCAP magic header.");
        }

        parseRecords(data, MAGIC.length);
        messages.sort(new Comparator<McapMessage>() {
            @Override
            public int compare(McapMessage left, McapMessage right) {
                int byTime = Long.compare(left.getLogTime(), right.getLogTime());
                if (byTime != 0) {
                    return byTime;
                }
                return Integer.compare(left.getChannelId(), right.getChannelId());
            }
        });
    }

    private void parseRecords(byte[] data, int offset) throws IOException {
        int position = offset;
        while (position + 9 <= data.length) {
            int opcode = data[position++] & 0xFF;
            long length = readUint64(data, position);
            position += 8;
            if (position + length > data.length) {
                throw new IOException("Truncated MCAP record.");
            }

            byte[] body = Arrays.copyOfRange(data, position, position + (int) length);
            position += (int) length;

            if (opcode == OP_CHUNK) {
                parseChunk(body);
            } else {
                parseRecord(opcode, body);
            }
        }
    }

    private void parseChunk(byte[] body) throws IOException {
        int offset = 28;
        String compression = readString(body, offset);
        offset += 4 + compression.length();
        long recordsLength = readUint64(body, offset);
        offset += 8;
        if (!compression.isEmpty()) {
            throw new IOException("Unsupported MCAP chunk compression: " + compression);
        }
        if (offset + recordsLength > body.length) {
            throw new IOException("Truncated MCAP chunk records.");
        }
        parseRecords(body, offset);
    }

    private void parseRecord(int opcode, byte[] body) throws IOException {
        if (opcode == OP_SCHEMA) {
            parseSchema(body);
        } else if (opcode == OP_CHANNEL) {
            parseChannel(body);
        } else if (opcode == OP_MESSAGE) {
            parseMessage(body);
        }
    }

    private void parseSchema(byte[] body) {
        int schemaId = readUint16(body, 0);
        int offset = 2;
        String name = readString(body, offset);
        schemasById.put(schemaId, name);
    }

    private void parseChannel(byte[] body) throws IOException {
        int channelId = readUint16(body, 0);
        int schemaId = readUint16(body, 2);
        int offset = 4;
        String topic = readString(body, offset);
        offset += 4 + topic.length();
        String messageEncoding = readString(body, offset);
        offset += 4 + messageEncoding.length();
        Map<String, String> metadata = readMap(body, offset);
        String qosProfiles = metadata.get("offered_qos_profiles");
        if (qosProfiles == null) {
            qosProfiles = "";
        }
        channelsById.put(channelId, new McapChannel(
                channelId,
                schemaId,
                topic,
                messageEncoding,
                qosProfiles
        ));
    }

    private void parseMessage(byte[] body) {
        int channelId = readUint16(body, 0);
        long logTime = readUint64(body, 6);
        byte[] payload = Arrays.copyOfRange(body, 22, body.length);
        messages.add(new McapMessage(channelId, logTime, payload));
    }

    private static Map<String, String> readMap(byte[] body, int offset) throws IOException {
        int mapLength = (int) readUint32(body, offset);
        offset += 4;
        int end = offset + mapLength;
        if (end > body.length) {
            throw new IOException("Truncated MCAP metadata map.");
        }

        Map<String, String> metadata = new LinkedHashMap<String, String>();
        while (offset < end) {
            String key = readString(body, offset);
            offset += 4 + key.length();
            String value = readString(body, offset);
            offset += 4 + value.length();
            metadata.put(key, value);
        }
        return metadata;
    }

    private static String readString(byte[] body, int offset) {
        int length = (int) readUint32(body, offset);
        return new String(body, offset + 4, length, StandardCharsets.UTF_8);
    }

    private static int readUint16(byte[] body, int offset) {
        return ByteBuffer.wrap(body, offset, 2).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
    }

    private static long readUint32(byte[] body, int offset) {
        return ByteBuffer.wrap(body, offset, 4).order(ByteOrder.LITTLE_ENDIAN).getInt() & 0xFFFFFFFFL;
    }

    private static long readUint64(byte[] body, int offset) {
        return ByteBuffer.wrap(body, offset, 8).order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    static final class McapChannel {
        private final int id;
        private final int schemaId;
        private final String topic;
        private final String serializationFormat;
        private final String offeredQosProfiles;

        McapChannel(int id, int schemaId, String topic, String serializationFormat, String offeredQosProfiles) {
            this.id = id;
            this.schemaId = schemaId;
            this.topic = topic;
            this.serializationFormat = serializationFormat;
            this.offeredQosProfiles = offeredQosProfiles;
        }

        int getId() {
            return id;
        }

        int getSchemaId() {
            return schemaId;
        }

        String getTopic() {
            return topic;
        }

        String getSerializationFormat() {
            return serializationFormat;
        }

        String getOfferedQosProfiles() {
            return offeredQosProfiles;
        }
    }

    static final class McapMessage {
        private final int channelId;
        private final long logTime;
        private final byte[] payload;

        McapMessage(int channelId, long logTime, byte[] payload) {
            this.channelId = channelId;
            this.logTime = logTime;
            this.payload = payload;
        }

        int getChannelId() {
            return channelId;
        }

        long getLogTime() {
            return logTime;
        }

        byte[] getPayload() {
            return payload;
        }
    }
}
