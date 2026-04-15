package org.indunet.fastproto.ros2;

/**
 * Codec for a ROS2 message or value object serialized in CDR format.
 *
 * @param <T> java type
 */
public interface Ros2Codec<T> {
    void serialize(Ros2CdrWriter writer, T value);

    T deserialize(Ros2CdrReader reader);
}
