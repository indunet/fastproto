package org.indunet.fastproto.ros2.trajectory_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * trajectory_msgs/msg/JointTrajectory
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JointTrajectory {
    private Header header;
    private String[] jointNames;
    private JointTrajectoryPoint[] points;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeStringSequence(Ros2CodecSupport.safeStringArray(this.getJointNames()));
                    Ros2CodecSupport.writeJointTrajectoryPointArray(writer, this.getPoints());
    }

    public static JointTrajectory readFrom(Ros2CdrReader reader) {
                    return JointTrajectory.builder()
                            .header(Header.readFrom(reader))
                            .jointNames(reader.readStringSequence())
                            .points(Ros2CodecSupport.readJointTrajectoryPointArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, JointTrajectory::writeTo);
    }

    public static JointTrajectory decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, JointTrajectory::readFrom);
    }
}
