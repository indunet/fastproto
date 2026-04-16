package org.indunet.fastproto.ros2.nav_msgs.msg;

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
 * nav_msgs/msg/Trajectory
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trajectory {
    private Header header;
    private TrajectoryPoint[] points;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    Ros2CodecSupport.writeTrajectoryPointArray(writer, this.getPoints());
    }

    public static Trajectory readFrom(Ros2CdrReader reader) {
                    return Trajectory.builder()
                            .header(Header.readFrom(reader))
                            .points(Ros2CodecSupport.readTrajectoryPointArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Trajectory::writeTo);
    }

    public static Trajectory decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Trajectory::readFrom);
    }
}
