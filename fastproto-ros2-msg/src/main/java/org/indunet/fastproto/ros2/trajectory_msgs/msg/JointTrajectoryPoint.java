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

/**
 * trajectory_msgs/msg/JointTrajectoryPoint
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JointTrajectoryPoint {
    private double[] positions;
    private double[] velocities;
    private double[] accelerations;
    private double[] effort;
    private Duration timeFromStart;

    public void writeTo(Ros2CdrWriter writer) {
                    writer.writeDoubleSequence(Ros2CodecSupport.safeDoubleArray(this.getPositions()));
                    writer.writeDoubleSequence(Ros2CodecSupport.safeDoubleArray(this.getVelocities()));
                    writer.writeDoubleSequence(Ros2CodecSupport.safeDoubleArray(this.getAccelerations()));
                    writer.writeDoubleSequence(Ros2CodecSupport.safeDoubleArray(this.getEffort()));
                    this.getTimeFromStart().writeTo(writer);
    }

    public static JointTrajectoryPoint readFrom(Ros2CdrReader reader) {
                    return JointTrajectoryPoint.builder()
                            .positions(reader.readDoubleSequence())
                            .velocities(reader.readDoubleSequence())
                            .accelerations(reader.readDoubleSequence())
                            .effort(reader.readDoubleSequence())
                            .timeFromStart(Duration.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, JointTrajectoryPoint::writeTo);
    }

    public static JointTrajectoryPoint decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, JointTrajectoryPoint::readFrom);
    }
}
