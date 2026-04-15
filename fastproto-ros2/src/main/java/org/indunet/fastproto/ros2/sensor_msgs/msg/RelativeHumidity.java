package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/RelativeHumidity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelativeHumidity {
    private Header header;
    private double relativeHumidity;
    private double variance;
}
