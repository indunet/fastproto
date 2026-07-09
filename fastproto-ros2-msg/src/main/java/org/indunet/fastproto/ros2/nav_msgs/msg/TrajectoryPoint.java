package org.indunet.fastproto.ros2.nav_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Accel;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Pose;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Twist;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Wrench;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * nav_msgs/msg/TrajectoryPoint
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrajectoryPoint {
    private Header header;
    private Pose pose;
    private Twist velocity;
    private Accel acceleration;
    private Wrench effort;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getPose().writeTo(writer);
                    this.getVelocity().writeTo(writer);
                    this.getAcceleration().writeTo(writer);
                    this.getEffort().writeTo(writer);
    }

    public static TrajectoryPoint readFrom(Ros2CdrReader reader) {
                    return TrajectoryPoint.builder()
                            .header(Header.readFrom(reader))
                            .pose(Pose.readFrom(reader))
                            .velocity(Twist.readFrom(reader))
                            .acceleration(Accel.readFrom(reader))
                            .effort(Wrench.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, TrajectoryPoint::writeTo);
    }

    public static TrajectoryPoint decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, TrajectoryPoint::readFrom);
    }
}
