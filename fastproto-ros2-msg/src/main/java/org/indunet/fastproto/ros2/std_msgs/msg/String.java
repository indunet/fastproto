package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/String
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class String {
    private java.lang.String data;

    public void writeTo(Ros2CdrWriter writer) {
        writer.writeString(getData() == null ? "" : getData());
    }

    public static String readFrom(Ros2CdrReader reader) {
        return String.builder().data(reader.readString()).build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, String::writeTo);
    }

    public static String decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, String::readFrom);
    }
}
