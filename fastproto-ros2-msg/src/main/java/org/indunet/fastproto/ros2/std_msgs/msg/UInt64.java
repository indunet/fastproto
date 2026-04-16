package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * std_msgs/msg/UInt64
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UInt64 {
    private BigInteger data;

    public void writeTo(Ros2CdrWriter writer) {
        writer.writeUInt64(this.getData() == null ? BigInteger.ZERO : this.getData());
    }

    public static UInt64 readFrom(Ros2CdrReader reader) {
        return UInt64.builder().data(reader.readUInt64()).build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, UInt64::writeTo);
    }

    public static UInt64 decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, UInt64::readFrom);
    }
}
