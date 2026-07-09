package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/UInt8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UInt8 {
    private int data;

    public void writeTo(Ros2CdrWriter writer) {
        writer.writeUInt8(this.getData());
    }

    public static UInt8 readFrom(Ros2CdrReader reader) {
        return UInt8.builder().data(reader.readUInt8()).build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, UInt8::writeTo);
    }

    public static UInt8 decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, UInt8::readFrom);
    }
}
