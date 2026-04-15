package org.indunet.fastproto.ros2.nav_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Pose;

/**
 * nav_msgs/msg/MapMetaData
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapMetaData {
    private Time mapLoadTime;
    private float resolution;
    private long width;
    private long height;
    private Pose origin;
}
