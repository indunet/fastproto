package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Header
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Header {
    private Time stamp;
    private java.lang.String frameId;
}
