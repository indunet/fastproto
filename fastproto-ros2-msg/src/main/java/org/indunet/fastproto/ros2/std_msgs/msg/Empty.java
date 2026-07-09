package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Empty
 */
@Data
@Builder
@NoArgsConstructor
public class Empty {

    public void writeTo(Ros2CdrWriter writer) {

    }

    public static Empty readFrom(Ros2CdrReader reader) {
        return Empty.builder().build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Empty::writeTo);
    }

    public static Empty decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Empty::readFrom);
    }
}
