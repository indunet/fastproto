package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Byte
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Byte {
    private int data;

    public void writeTo(Ros2CdrWriter writer) {
        writer.writeInt8(this.getData());
    }

    public static Byte readFrom(Ros2CdrReader reader) {
        return Byte.builder().data(reader.readInt8()).build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Byte::writeTo);
    }

    public static Byte decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Byte::readFrom);
    }
}
