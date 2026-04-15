package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/Range
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Range {
    public static final int ULTRASOUND = 0;
    public static final int INFRARED = 1;

    private Header header;
    private int radiationType;
    private float fieldOfView;
    private float minRange;
    private float maxRange;
    private float range;
}
