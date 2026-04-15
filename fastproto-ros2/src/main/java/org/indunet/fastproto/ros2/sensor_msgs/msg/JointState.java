package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/JointState
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JointState {
    private Header header;
    private String[] name;
    private double[] position;
    private double[] velocity;
    private double[] effort;
}
