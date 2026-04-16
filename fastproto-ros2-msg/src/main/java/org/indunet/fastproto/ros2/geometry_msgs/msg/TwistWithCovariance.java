package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/TwistWithCovariance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwistWithCovariance {
    private Twist twist;
    private double[] covariance;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getTwist().writeTo(writer);
                    Ros2CodecSupport.writeFixedDoubleArray(writer, this.getCovariance(), 36, "covariance");
    }

    public static TwistWithCovariance readFrom(Ros2CdrReader reader) {
                    return TwistWithCovariance.builder()
                            .twist(Twist.readFrom(reader))
                            .covariance(Ros2CodecSupport.readFixedDoubleArray(reader, 36))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, TwistWithCovariance::writeTo);
    }

    public static TwistWithCovariance decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, TwistWithCovariance::readFrom);
    }
}
