package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * geometry_msgs/msg/PoseStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoseStamped {
    private Header header;
    private Pose pose;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getPose().writeTo(writer);
    }

    public static PoseStamped readFrom(Ros2CdrReader reader) {
                    return PoseStamped.builder()
                            .header(Header.readFrom(reader))
                            .pose(Pose.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, PoseStamped::writeTo);
    }

    public static PoseStamped decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, PoseStamped::readFrom);
    }
}
