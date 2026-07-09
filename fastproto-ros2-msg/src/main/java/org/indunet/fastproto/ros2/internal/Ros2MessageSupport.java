package org.indunet.fastproto.ros2.internal;

import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;

/**
 * Shared helpers for message-local ROS2 serialization methods.
 */
public final class Ros2MessageSupport {
    private Ros2MessageSupport() {
    }

    public static <T> byte[] encode(T value, Writer<T> writerFn) {
        Ros2CdrWriter writer = new Ros2CdrWriter();
        writerFn.write(value, writer);
        return writer.toByteArray();
    }

    public static <T> T decode(byte[] bytes, Reader<T> readerFn) {
        Ros2CdrReader reader = new Ros2CdrReader(bytes);
        return readerFn.read(reader);
    }

    public static <T> void writeFixedSize(Ros2CdrWriter writer, T value, int alignment, int size) {
        writer.align(alignment);
        writer.writeBytes(FastProto.encode(value, size));
    }

    public static <T> T readFixedSize(Ros2CdrReader reader, int alignment, int size, Class<T> type) {
        reader.align(alignment);
        return FastProto.decode(reader.readBytes(size), type);
    }

    public interface Writer<T> {
        void write(T value, Ros2CdrWriter writer);
    }

    public interface Reader<T> {
        T read(Ros2CdrReader reader);
    }
}
