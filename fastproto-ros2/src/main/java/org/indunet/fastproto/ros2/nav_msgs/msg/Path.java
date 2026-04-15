package org.indunet.fastproto.ros2.nav_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseStamped;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * nav_msgs/msg/Path
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Path {
    private Header header;
    private PoseStamped[] poses;
}
