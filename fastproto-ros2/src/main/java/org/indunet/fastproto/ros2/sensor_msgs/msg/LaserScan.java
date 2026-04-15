package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/LaserScan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaserScan {
    private Header header;
    private float angleMin;
    private float angleMax;
    private float angleIncrement;
    private float timeIncrement;
    private float scanTime;
    private float rangeMin;
    private float rangeMax;
    private float[] ranges;
    private float[] intensities;
}
