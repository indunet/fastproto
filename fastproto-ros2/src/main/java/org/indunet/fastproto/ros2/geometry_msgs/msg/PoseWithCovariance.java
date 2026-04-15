package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/PoseWithCovariance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoseWithCovariance {
    private Pose pose;
    private double[] covariance;
}
