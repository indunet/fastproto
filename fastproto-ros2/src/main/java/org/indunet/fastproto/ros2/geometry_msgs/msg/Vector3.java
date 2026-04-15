package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.DoubleType;

/**
 * geometry_msgs/msg/Vector3
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vector3 {
    public static final int SIZE = 24;

    @DoubleType(offset = 0)
    private double x;

    @DoubleType(offset = 8)
    private double y;

    @DoubleType(offset = 16)
    private double z;
}
