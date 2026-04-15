package org.indunet.fastproto.ros2.internal;

import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import org.indunet.fastproto.ros2.Ros2Codec;

/**
 * Bridges fixed-size FastProto objects into ROS2 CDR streams.
 *
 * @param <T> java type
 */
public final class FixedSizeRos2Codec<T> implements Ros2Codec<T> {
    private final Class<T> type;
    private final int alignment;
    private final int size;

    public FixedSizeRos2Codec(Class<T> type, int alignment, int size) {
        this.type = type;
        this.alignment = alignment;
        this.size = size;
    }

    @Override
    public void serialize(Ros2CdrWriter writer, T value) {
        writer.align(alignment);
        writer.writeBytes(FastProto.encode(value, size));
    }

    @Override
    public T deserialize(Ros2CdrReader reader) {
        reader.align(alignment);
        return FastProto.decode(reader.readBytes(size), type);
    }
}
