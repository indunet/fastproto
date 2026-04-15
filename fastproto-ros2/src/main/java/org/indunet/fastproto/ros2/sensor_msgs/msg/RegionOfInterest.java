package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/RegionOfInterest
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionOfInterest {
    private long xOffset;
    private long yOffset;
    private long height;
    private long width;
    private boolean doRectify;
}
