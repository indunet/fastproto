package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point32;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/PointCloud
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointCloud {
    private Header header;
    private Point32[] points;
    private ChannelFloat32[] channels;
}
