package org.indunet.fastproto.ros2;

/**
 * Entry points for ROS2 CDR serialization/deserialization.
 */
public final class Ros2FastProto {
    private Ros2FastProto() {
    }

    public static <T> byte[] encode(T value, Ros2Codec<T> codec) {
        Ros2CdrWriter writer = new Ros2CdrWriter();
        codec.serialize(writer, value);
        return writer.toByteArray();
    }

    public static <T> T decode(byte[] bytes, Ros2Codec<T> codec) {
        Ros2CdrReader reader = new Ros2CdrReader(bytes);
        return codec.deserialize(reader);
    }
}
