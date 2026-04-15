package org.indunet.fastproto.ros2.std_msgs.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.FloatType;

/**
 * std_msgs/msg/ColorRGBA
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColorRGBA {
    public static final int SIZE = 16;

    @FloatType(offset = 0)
    private float r;

    @FloatType(offset = 4)
    private float g;

    @FloatType(offset = 8)
    private float b;

    @FloatType(offset = 12)
    private float a;
}
