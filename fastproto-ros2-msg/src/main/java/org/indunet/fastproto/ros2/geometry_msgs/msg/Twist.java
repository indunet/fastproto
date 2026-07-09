package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/Twist
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Twist {
    private Vector3 linear;
    private Vector3 angular;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getLinear().writeTo(writer);
                    this.getAngular().writeTo(writer);
    }

    public static Twist readFrom(Ros2CdrReader reader) {
                    return Twist.builder()
                            .linear(Vector3.readFrom(reader))
                            .angular(Vector3.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Twist::writeTo);
    }

    public static Twist decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Twist::readFrom);
    }
}
