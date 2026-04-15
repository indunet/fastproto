package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.std_msgs.msg.Header;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/Image
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private Header header;
    private long height;
    private long width;
    private String encoding;
    private int isBigendian;
    private long step;
    private byte[] data;
}
