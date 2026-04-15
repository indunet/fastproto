package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * geometry_msgs/msg/Vector3Stamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vector3Stamped {
    private Header header;
    private Vector3 vector;
}
