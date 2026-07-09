package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Char
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Char {
    private int data;

    public void writeTo(Ros2CdrWriter writer) {
        writer.writeUInt8(this.getData());
    }

    public static Char readFrom(Ros2CdrReader reader) {
        return Char.builder().data(reader.readUInt8()).build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Char::writeTo);
    }

    public static Char decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Char::readFrom);
    }
}
