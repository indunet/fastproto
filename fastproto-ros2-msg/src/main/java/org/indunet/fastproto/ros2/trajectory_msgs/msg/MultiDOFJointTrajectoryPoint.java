package org.indunet.fastproto.ros2.trajectory_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Duration;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Transform;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Twist;

/**
 * trajectory_msgs/msg/MultiDOFJointTrajectoryPoint
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiDOFJointTrajectoryPoint {
    private Transform[] transforms;
    private Twist[] velocities;
    private Twist[] accelerations;
    private Duration timeFromStart;

    public void writeTo(Ros2CdrWriter writer) {
                            Ros2CodecSupport.writeTransformArray(writer, this.getTransforms());
                            Ros2CodecSupport.writeTwistArray(writer, this.getVelocities());
                            Ros2CodecSupport.writeTwistArray(writer, this.getAccelerations());
                            this.getTimeFromStart().writeTo(writer);
    }

    public static MultiDOFJointTrajectoryPoint readFrom(Ros2CdrReader reader) {
                            return MultiDOFJointTrajectoryPoint.builder()
                                    .transforms(Ros2CodecSupport.readTransformArray(reader))
                                    .velocities(Ros2CodecSupport.readTwistArray(reader))
                                    .accelerations(Ros2CodecSupport.readTwistArray(reader))
                                    .timeFromStart(Duration.readFrom(reader))
                                    .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, MultiDOFJointTrajectoryPoint::writeTo);
    }

    public static MultiDOFJointTrajectoryPoint decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, MultiDOFJointTrajectoryPoint::readFrom);
    }
}
