package org.indunet.fastproto.ros2.std_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * std_msgs/msg/UInt64
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UInt64 {
    private BigInteger data;
}
