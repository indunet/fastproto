package org.indunet.fastproto.ros2.sensor_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/JoyFeedback
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoyFeedback {
    public static final int TYPE_LED = 0;
    public static final int TYPE_RUMBLE = 1;
    public static final int TYPE_BUZZER = 2;

    private int type;
    private int id;
    private float intensity;
}
