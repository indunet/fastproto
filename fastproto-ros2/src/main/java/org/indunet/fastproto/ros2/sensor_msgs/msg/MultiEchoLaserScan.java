package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/MultiEchoLaserScan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiEchoLaserScan {
    private Header header;
    private float angleMin;
    private float angleMax;
    private float angleIncrement;
    private float timeIncrement;
    private float scanTime;
    private float rangeMin;
    private float rangeMax;
    private LaserEcho[] ranges;
    private LaserEcho[] intensities;
}
