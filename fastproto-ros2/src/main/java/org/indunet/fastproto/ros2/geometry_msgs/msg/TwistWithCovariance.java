package org.indunet.fastproto.ros2.geometry_msgs.msg;

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
}
