package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * geometry_msgs/msg/WrenchStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WrenchStamped {
    private Header header;
    private Wrench wrench;
}
