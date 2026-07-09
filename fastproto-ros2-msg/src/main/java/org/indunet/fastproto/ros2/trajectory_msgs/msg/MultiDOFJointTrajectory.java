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
 * trajectory_msgs/msg/MultiDOFJointTrajectory
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiDOFJointTrajectory {
    private Header header;
    private String[] jointNames;
    private MultiDOFJointTrajectoryPoint[] points;

    public void writeTo(Ros2CdrWriter writer) {
                            this.getHeader().writeTo(writer);
                            writer.writeStringSequence(Ros2CodecSupport.safeStringArray(this.getJointNames()));
                            Ros2CodecSupport.writeMultiDOFJointTrajectoryPointArray(writer, this.getPoints());
    }

    public static MultiDOFJointTrajectory readFrom(Ros2CdrReader reader) {
                            return MultiDOFJointTrajectory.builder()
                                    .header(Header.readFrom(reader))
                                    .jointNames(reader.readStringSequence())
                                    .points(Ros2CodecSupport.readMultiDOFJointTrajectoryPointArray(reader))
                                    .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, MultiDOFJointTrajectory::writeTo);
    }

    public static MultiDOFJointTrajectory decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, MultiDOFJointTrajectory::readFrom);
    }
}
