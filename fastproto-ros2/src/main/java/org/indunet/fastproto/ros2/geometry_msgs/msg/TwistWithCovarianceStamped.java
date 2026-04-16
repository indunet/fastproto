package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * geometry_msgs/msg/TwistWithCovarianceStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwistWithCovarianceStamped {
    private Header header;
    private TwistWithCovariance twist;
}
