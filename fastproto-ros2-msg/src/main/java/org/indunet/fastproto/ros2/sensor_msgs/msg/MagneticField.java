package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Vector3;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/MagneticField
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MagneticField {
    private Header header;
    private Vector3 magneticField;
    private double[] magneticFieldCovariance;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getMagneticField().writeTo(writer);
                    Ros2CodecSupport.writeFixedDoubleArray(writer, this.getMagneticFieldCovariance(), 9, "magnetic_field_covariance");
    }

    public static MagneticField readFrom(Ros2CdrReader reader) {
                    return MagneticField.builder()
                            .header(Header.readFrom(reader))
                            .magneticField(Vector3.readFrom(reader))
                            .magneticFieldCovariance(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, MagneticField::writeTo);
    }

    public static MagneticField decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, MagneticField::readFrom);
    }
}
