package org.indunet.fastproto.ros2.sensor_msgs.msg;

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
 * sensor_msgs/msg/NavSatFix
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NavSatFix {
    public static final int COVARIANCE_TYPE_UNKNOWN = 0;
    public static final int COVARIANCE_TYPE_APPROXIMATED = 1;
    public static final int COVARIANCE_TYPE_DIAGONAL_KNOWN = 2;
    public static final int COVARIANCE_TYPE_KNOWN = 3;

    private Header header;
    private NavSatStatus status;
    private double latitude;
    private double longitude;
    private double altitude;
    private double[] positionCovariance;
    private int positionCovarianceType;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getStatus().writeTo(writer);
                    writer.writeDouble(this.getLatitude());
                    writer.writeDouble(this.getLongitude());
                    writer.writeDouble(this.getAltitude());
                    Ros2CodecSupport.writeFixedDoubleArray(writer, this.getPositionCovariance(), 9, "position_covariance");
                    writer.writeUInt8(this.getPositionCovarianceType());
    }

    public static NavSatFix readFrom(Ros2CdrReader reader) {
                    return NavSatFix.builder()
                            .header(Header.readFrom(reader))
                            .status(NavSatStatus.readFrom(reader))
                            .latitude(reader.readDouble())
                            .longitude(reader.readDouble())
                            .altitude(reader.readDouble())
                            .positionCovariance(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                            .positionCovarianceType(reader.readUInt8())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, NavSatFix::writeTo);
    }

    public static NavSatFix decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, NavSatFix::readFrom);
    }
}
