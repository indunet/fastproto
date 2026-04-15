package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/Joy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Joy {
    private Header header;
    private float[] axes;
    private int[] buttons;
}
