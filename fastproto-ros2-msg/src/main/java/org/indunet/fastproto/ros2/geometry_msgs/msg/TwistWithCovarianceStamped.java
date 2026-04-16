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
 * geometry_msgs/msg/TwistWithCovarianceStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwistWithCovarianceStamped {
    private Header header;
    private TwistWithCovariance twist;

    public void writeTo(Ros2CdrWriter writer) {
                            this.getHeader().writeTo(writer);
                            this.getTwist().writeTo(writer);
    }

    public static TwistWithCovarianceStamped readFrom(Ros2CdrReader reader) {
                            return TwistWithCovarianceStamped.builder()
                                    .header(Header.readFrom(reader))
                                    .twist(TwistWithCovariance.readFrom(reader))
                                    .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, TwistWithCovarianceStamped::writeTo);
    }

    public static TwistWithCovarianceStamped decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, TwistWithCovarianceStamped::readFrom);
    }
}
