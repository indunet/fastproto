package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/TimeReference
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeReference {
    private Header header;
    private Time timeRef;
    private String source;
}
