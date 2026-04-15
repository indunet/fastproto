package org.indunet.fastproto.ros2.geometry_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.FloatType;

/**
 * geometry_msgs/msg/Point32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Point32 {
    public static final int SIZE = 12;

    @FloatType(offset = 0)
    private float x;
    @FloatType(offset = 4)
    private float y;
    @FloatType(offset = 8)
    private float z;
}
