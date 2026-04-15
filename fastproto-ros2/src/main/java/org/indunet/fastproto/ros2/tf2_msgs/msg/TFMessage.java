package org.indunet.fastproto.ros2.tf2_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TransformStamped;

/**
 * tf2_msgs/msg/TFMessage
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TFMessage {
    private TransformStamped[] transforms;
}
