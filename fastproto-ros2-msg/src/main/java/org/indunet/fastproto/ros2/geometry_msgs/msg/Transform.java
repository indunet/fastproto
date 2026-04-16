package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/Transform
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transform {
    private Vector3 translation;
    private Quaternion rotation;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getTranslation().writeTo(writer);
                    this.getRotation().writeTo(writer);
    }

    public static Transform readFrom(Ros2CdrReader reader) {
                    return Transform.builder()
                            .translation(Vector3.readFrom(reader))
                            .rotation(Quaternion.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Transform::writeTo);
    }

    public static Transform decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Transform::readFrom);
    }
}
