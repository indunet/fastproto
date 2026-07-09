package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Quaternion;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Vector3;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/Imu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Imu {
    private Header header;
    private Quaternion orientation;
    private double[] orientationCovariance;
    private Vector3 angularVelocity;
    private double[] angularVelocityCovariance;
    private Vector3 linearAcceleration;
    private double[] linearAccelerationCovariance;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getOrientation().writeTo(writer);
                    Ros2CodecSupport.writeFixedDoubleArray(writer, this.getOrientationCovariance(), 9, "orientation_covariance");
                    this.getAngularVelocity().writeTo(writer);
                    Ros2CodecSupport.writeFixedDoubleArray(writer, this.getAngularVelocityCovariance(), 9, "angular_velocity_covariance");
                    this.getLinearAcceleration().writeTo(writer);
                    Ros2CodecSupport.writeFixedDoubleArray(writer, this.getLinearAccelerationCovariance(), 9, "linear_acceleration_covariance");
    }

    public static Imu readFrom(Ros2CdrReader reader) {
                    return Imu.builder()
                            .header(Header.readFrom(reader))
                            .orientation(Quaternion.readFrom(reader))
                            .orientationCovariance(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                            .angularVelocity(Vector3.readFrom(reader))
                            .angularVelocityCovariance(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                            .linearAcceleration(Vector3.readFrom(reader))
                            .linearAccelerationCovariance(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Imu::writeTo);
    }

    public static Imu decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Imu::readFrom);
    }
}
