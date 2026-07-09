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
 * geometry_msgs/msg/InertiaStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InertiaStamped {
    private Header header;
    private Inertia inertia;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getInertia().writeTo(writer);
    }

    public static InertiaStamped readFrom(Ros2CdrReader reader) {
                    return InertiaStamped.builder()
                            .header(Header.readFrom(reader))
                            .inertia(Inertia.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, InertiaStamped::writeTo);
    }

    public static InertiaStamped decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, InertiaStamped::readFrom);
    }
}
