package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Float32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Float32 {
    private float data;

    public void writeTo(Ros2CdrWriter writer) {
        writer.writeFloat(this.getData());
    }

    public static Float32 readFrom(Ros2CdrReader reader) {
        return Float32.builder().data(reader.readFloat()).build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Float32::writeTo);
    }

    public static Float32 decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Float32::readFrom);
    }
}
