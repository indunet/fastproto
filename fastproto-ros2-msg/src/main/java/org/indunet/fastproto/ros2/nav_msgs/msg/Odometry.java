package org.indunet.fastproto.ros2.nav_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseWithCovariance;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TwistWithCovariance;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * nav_msgs/msg/Odometry
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Odometry {
    private Header header;
    private String childFrameId;
    private PoseWithCovariance pose;
    private TwistWithCovariance twist;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeString(this.getChildFrameId());
                    this.getPose().writeTo(writer);
                    this.getTwist().writeTo(writer);
    }

    public static Odometry readFrom(Ros2CdrReader reader) {
                    return Odometry.builder()
                            .header(Header.readFrom(reader))
                            .childFrameId(reader.readString())
                            .pose(PoseWithCovariance.readFrom(reader))
                            .twist(TwistWithCovariance.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Odometry::writeTo);
    }

    public static Odometry decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Odometry::readFrom);
    }
}
