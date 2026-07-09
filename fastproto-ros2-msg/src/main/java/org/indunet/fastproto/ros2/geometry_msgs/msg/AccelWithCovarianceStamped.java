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
 * geometry_msgs/msg/AccelWithCovarianceStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccelWithCovarianceStamped {
    private Header header;
    private AccelWithCovariance accel;

    public void writeTo(Ros2CdrWriter writer) {
                            this.getHeader().writeTo(writer);
                            this.getAccel().writeTo(writer);
    }

    public static AccelWithCovarianceStamped readFrom(Ros2CdrReader reader) {
                            return AccelWithCovarianceStamped.builder()
                                    .header(Header.readFrom(reader))
                                    .accel(AccelWithCovariance.readFrom(reader))
                                    .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, AccelWithCovarianceStamped::writeTo);
    }

    public static AccelWithCovarianceStamped decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, AccelWithCovarianceStamped::readFrom);
    }
}
