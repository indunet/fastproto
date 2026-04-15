package org.indunet.fastproto.ros2.std_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Int32MultiArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Int32MultiArray {
    private MultiArrayLayout layout;
    private int[] data;
}
