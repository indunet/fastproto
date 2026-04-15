package org.indunet.fastproto.ros2.nav_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseWithCovariance;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TwistWithCovariance;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * nav_msgs/msg/Odometry
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Odometry {
    private Header header;
    private String childFrameId;
    private PoseWithCovariance pose;
    private TwistWithCovariance twist;
}
