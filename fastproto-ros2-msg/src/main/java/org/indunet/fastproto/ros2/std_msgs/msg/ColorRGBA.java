package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
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

    public void writeTo(Ros2CdrWriter writer) {
        Ros2MessageSupport.writeFixedSize(writer, this, 4, SIZE);
    }

    public static ColorRGBA readFrom(Ros2CdrReader reader) {
        return Ros2MessageSupport.readFixedSize(reader, 4, SIZE, ColorRGBA.class);
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, ColorRGBA::writeTo);
    }

    public static ColorRGBA decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, ColorRGBA::readFrom);
    }
}
