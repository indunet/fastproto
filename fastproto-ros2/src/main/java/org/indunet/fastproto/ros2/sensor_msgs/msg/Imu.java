package org.indunet.fastproto.ros2.sensor_msgs.msg;

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
}
