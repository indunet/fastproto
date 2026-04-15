package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/Transform
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transform {
    private Vector3 translation;
    private Quaternion rotation;
}
