package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/Pose
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pose {
    private Point position;
    private Quaternion orientation;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getPosition().writeTo(writer);
                    this.getOrientation().writeTo(writer);
    }

    public static Pose readFrom(Ros2CdrReader reader) {
                    return Pose.builder()
                            .position(Point.readFrom(reader))
                            .orientation(Quaternion.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Pose::writeTo);
    }

    public static Pose decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Pose::readFrom);
    }
}
