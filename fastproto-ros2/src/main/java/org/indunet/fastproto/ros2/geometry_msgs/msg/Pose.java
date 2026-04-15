package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/Pose
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pose {
    private Point position;
    private Quaternion orientation;
}
