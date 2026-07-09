package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/Accel
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Accel {
    private Vector3 linear;
    private Vector3 angular;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getLinear().writeTo(writer);
                    this.getAngular().writeTo(writer);
    }

    public static Accel readFrom(Ros2CdrReader reader) {
                    return Accel.builder()
                            .linear(Vector3.readFrom(reader))
                            .angular(Vector3.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Accel::writeTo);
    }

    public static Accel decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Accel::readFrom);
    }
}
