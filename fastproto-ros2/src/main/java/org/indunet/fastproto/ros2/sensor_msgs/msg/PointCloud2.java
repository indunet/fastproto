package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/PointCloud2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointCloud2 {
    private Header header;
    private long height;
    private long width;
    private PointField[] fields;
    private boolean isBigendian;
    private long pointStep;
    private long rowStep;
    private byte[] data;
    private boolean isDense;
}
