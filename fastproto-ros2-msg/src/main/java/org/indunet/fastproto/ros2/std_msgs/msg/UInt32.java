package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/UInt32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UInt32 {
    private long data;

    public void writeTo(Ros2CdrWriter writer) {
        writer.writeUInt32(this.getData());
    }

    public static UInt32 readFrom(Ros2CdrReader reader) {
        return UInt32.builder().data(reader.readUInt32()).build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, UInt32::writeTo);
    }

    public static UInt32 decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, UInt32::readFrom);
    }
}
