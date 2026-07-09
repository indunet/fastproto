package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/UInt16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UInt16 {
    private int data;

    public void writeTo(Ros2CdrWriter writer) {
        writer.writeUInt16(this.getData());
    }

    public static UInt16 readFrom(Ros2CdrReader reader) {
        return UInt16.builder().data(reader.readUInt16()).build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, UInt16::writeTo);
    }

    public static UInt16 decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, UInt16::readFrom);
    }
}
