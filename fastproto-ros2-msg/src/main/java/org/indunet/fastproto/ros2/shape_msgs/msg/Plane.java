package org.indunet.fastproto.ros2.shape_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * shape_msgs/msg/Plane
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plane {
    public static final int SIZE = 32;

    private double[] coef;

    public void writeTo(Ros2CdrWriter writer) {
                    Ros2CodecSupport.writeFixedDoubleArray(writer, this.getCoef(), 4, "coef");
    }

    public static Plane readFrom(Ros2CdrReader reader) {
                    return Plane.builder()
                            .coef(Ros2CodecSupport.readFixedDoubleArray(reader, 4))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Plane::writeTo);
    }

    public static Plane decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Plane::readFrom);
    }
}
