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
 * geometry_msgs/msg/WrenchStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WrenchStamped {
    private Header header;
    private Wrench wrench;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getWrench().writeTo(writer);
    }

    public static WrenchStamped readFrom(Ros2CdrReader reader) {
                    return WrenchStamped.builder()
                            .header(Header.readFrom(reader))
                            .wrench(Wrench.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, WrenchStamped::writeTo);
    }

    public static WrenchStamped decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, WrenchStamped::readFrom);
    }
}
