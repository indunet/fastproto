package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/LaserEcho
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaserEcho {
    private float[] echoes;
}
