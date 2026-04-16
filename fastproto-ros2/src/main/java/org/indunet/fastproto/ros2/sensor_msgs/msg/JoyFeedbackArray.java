package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/JoyFeedbackArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoyFeedbackArray {
    private JoyFeedback[] array;
}
