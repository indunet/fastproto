package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * geometry_msgs/msg/TransformStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransformStamped {
    private Header header;
    private String childFrameId;
    private Transform transform;
}
