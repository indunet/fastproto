package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/CompressedImage
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompressedImage {
    private Header header;
    private String format;
    private byte[] data;
}
