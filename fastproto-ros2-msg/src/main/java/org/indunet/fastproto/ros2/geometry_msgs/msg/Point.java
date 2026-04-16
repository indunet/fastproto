package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.DoubleType;

/**
 * geometry_msgs/msg/Point
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    public static final int SIZE = 24;

    @DoubleType(offset = 0)
    private double x;

    @DoubleType(offset = 8)
    private double y;

    @DoubleType(offset = 16)
    private double z;

    public void writeTo(Ros2CdrWriter writer) {
        Ros2MessageSupport.writeFixedSize(writer, this, 8, SIZE);
    }

    public static Point readFrom(Ros2CdrReader reader) {
        return Ros2MessageSupport.readFixedSize(reader, 8, SIZE, Point.class);
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Point::writeTo);
    }

    public static Point decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Point::readFrom);
    }
}
