package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/Wrench
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wrench {
    private Vector3 force;
    private Vector3 torque;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getForce().writeTo(writer);
                    this.getTorque().writeTo(writer);
    }

    public static Wrench readFrom(Ros2CdrReader reader) {
                    return Wrench.builder()
                            .force(Vector3.readFrom(reader))
                            .torque(Vector3.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Wrench::writeTo);
    }

    public static Wrench decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Wrench::readFrom);
    }
}
