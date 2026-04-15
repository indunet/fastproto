package org.indunet.fastproto.ros2.std_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Float64MultiArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Float64MultiArray {
    private MultiArrayLayout layout;
    private double[] data;
}
