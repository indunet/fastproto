package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.DoubleType;

/**
 * geometry_msgs/msg/Quaternion
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quaternion {
    public static final int SIZE = 32;

    @DoubleType(offset = 0)
    private double x;

    @DoubleType(offset = 8)
    private double y;

    @DoubleType(offset = 16)
    private double z;

    @DoubleType(offset = 24)
    private double w;
}
