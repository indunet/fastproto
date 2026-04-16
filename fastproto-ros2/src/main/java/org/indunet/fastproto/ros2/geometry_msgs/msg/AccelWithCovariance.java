package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/AccelWithCovariance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccelWithCovariance {
    private Accel accel;
    private double[] covariance;
}
