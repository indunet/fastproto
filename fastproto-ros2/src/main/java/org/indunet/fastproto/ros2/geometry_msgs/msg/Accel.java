package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/Accel
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Accel {
    private Vector3 linear;
    private Vector3 angular;
}
