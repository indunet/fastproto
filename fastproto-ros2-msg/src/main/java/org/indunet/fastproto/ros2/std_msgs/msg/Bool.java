package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Bool
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bool {
    private boolean data;

    public void writeTo(Ros2CdrWriter writer) {
        writer.writeBool(this.isData());
    }

    public static Bool readFrom(Ros2CdrReader reader) {
        return Bool.builder().data(reader.readBool()).build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Bool::writeTo);
    }

    public static Bool decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Bool::readFrom);
    }
}
