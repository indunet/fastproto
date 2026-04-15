package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/Wrench
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wrench {
    private Vector3 force;
    private Vector3 torque;
}
