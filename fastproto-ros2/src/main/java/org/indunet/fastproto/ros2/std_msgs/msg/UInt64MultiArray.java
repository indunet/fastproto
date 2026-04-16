package org.indunet.fastproto.ros2.std_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * std_msgs/msg/UInt64MultiArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UInt64MultiArray {
    private MultiArrayLayout layout;
    private BigInteger[] data;
}
