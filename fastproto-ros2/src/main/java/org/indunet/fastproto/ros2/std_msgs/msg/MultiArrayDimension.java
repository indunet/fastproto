package org.indunet.fastproto.ros2.std_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/MultiArrayDimension
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiArrayDimension {
    private String label;
    private long size;
    private long stride;
}
