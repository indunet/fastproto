package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Float64
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Float64 {
    private double data;

    public void writeTo(Ros2CdrWriter writer) {
        writer.writeDouble(this.getData());
    }

    public static Float64 readFrom(Ros2CdrReader reader) {
        return Float64.builder().data(reader.readDouble()).build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Float64::writeTo);
    }

    public static Float64 decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Float64::readFrom);
    }
}
