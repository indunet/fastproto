package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/Inertia
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inertia {
    private double m;
    private Vector3 com;
    private double ixx;
    private double ixy;
    private double ixz;
    private double iyy;
    private double iyz;
    private double izz;
}
