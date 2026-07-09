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
 * geometry_msgs/msg/PoseWithCovarianceStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoseWithCovarianceStamped {
    private Header header;
    private PoseWithCovariance pose;

    public void writeTo(Ros2CdrWriter writer) {
                            this.getHeader().writeTo(writer);
                            this.getPose().writeTo(writer);
    }

    public static PoseWithCovarianceStamped readFrom(Ros2CdrReader reader) {
                            return PoseWithCovarianceStamped.builder()
                                    .header(Header.readFrom(reader))
                                    .pose(PoseWithCovariance.readFrom(reader))
                                    .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, PoseWithCovarianceStamped::writeTo);
    }

    public static PoseWithCovarianceStamped decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, PoseWithCovarianceStamped::readFrom);
    }
}
