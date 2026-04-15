package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * geometry_msgs/msg/PointStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointStamped {
    private Header header;
    private Point point;
}
