package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Int16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Int16 {
    private int data;

    public void writeTo(Ros2CdrWriter writer) {
        writer.writeInt16(this.getData());
    }

    public static Int16 readFrom(Ros2CdrReader reader) {
        return Int16.builder().data(reader.readInt16()).build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Int16::writeTo);
    }

    public static Int16 decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Int16::readFrom);
    }
}
