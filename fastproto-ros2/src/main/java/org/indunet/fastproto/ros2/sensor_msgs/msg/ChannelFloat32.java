package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/ChannelFloat32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelFloat32 {
    private String name;
    private float[] values;
}
