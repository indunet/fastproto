package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Int64
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Int64 {
    private long data;

    public void writeTo(Ros2CdrWriter writer) {
        writer.writeInt64(this.getData());
    }

    public static Int64 readFrom(Ros2CdrReader reader) {
        return Int64.builder().data(reader.readInt64()).build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Int64::writeTo);
    }

    public static Int64 decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Int64::readFrom);
    }
}
