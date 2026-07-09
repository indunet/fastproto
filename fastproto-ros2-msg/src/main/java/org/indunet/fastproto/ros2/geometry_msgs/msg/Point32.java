package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.FloatType;

/**
 * geometry_msgs/msg/Point32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Point32 {
    public static final int SIZE = 12;

    @FloatType(offset = 0)
    private float x;
    @FloatType(offset = 4)
    private float y;
    @FloatType(offset = 8)
    private float z;

    public void writeTo(Ros2CdrWriter writer) {
        Ros2MessageSupport.writeFixedSize(writer, this, 4, SIZE);
    }

    public static Point32 readFrom(Ros2CdrReader reader) {
        return Ros2MessageSupport.readFixedSize(reader, 4, SIZE, Point32.class);
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Point32::writeTo);
    }

    public static Point32 decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Point32::readFrom);
    }
}
