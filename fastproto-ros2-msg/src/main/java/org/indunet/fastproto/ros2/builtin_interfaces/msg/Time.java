package org.indunet.fastproto.ros2.builtin_interfaces.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.Int32Type;
import org.indunet.fastproto.annotation.UInt32Type;

/**
 * builtin_interfaces/msg/Time
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Time {
    public static final int SIZE = 8;

    @Int32Type(offset = 0)
    private int sec;

    @UInt32Type(offset = 4)
    private long nanosec;

    public void writeTo(Ros2CdrWriter writer) {
        Ros2MessageSupport.writeFixedSize(writer, this, 4, SIZE);
    }

    public static Time readFrom(Ros2CdrReader reader) {
        return Ros2MessageSupport.readFixedSize(reader, 4, SIZE, Time.class);
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Time::writeTo);
    }

    public static Time decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Time::readFrom);
    }
}
