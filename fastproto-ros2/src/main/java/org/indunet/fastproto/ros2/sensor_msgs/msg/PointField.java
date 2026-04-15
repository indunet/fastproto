package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/PointField
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointField {
    public static final int INT8 = 1;
    public static final int UINT8 = 2;
    public static final int INT16 = 3;
    public static final int UINT16 = 4;
    public static final int INT32 = 5;
    public static final int UINT32 = 6;
    public static final int FLOAT32 = 7;
    public static final int FLOAT64 = 8;

    private String name;
    private long offset;
    private int datatype;
    private long count;
}
