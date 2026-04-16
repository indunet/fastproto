package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * geometry_msgs/msg/TwistStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwistStamped {
    private Header header;
    private Twist twist;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getTwist().writeTo(writer);
    }

    public static TwistStamped readFrom(Ros2CdrReader reader) {
                    return TwistStamped.builder()
                            .header(Header.readFrom(reader))
                            .twist(Twist.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, TwistStamped::writeTo);
    }

    public static TwistStamped decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, TwistStamped::readFrom);
    }
}
