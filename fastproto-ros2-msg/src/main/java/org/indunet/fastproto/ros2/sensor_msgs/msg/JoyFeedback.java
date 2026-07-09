package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
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

    public void writeTo(Ros2CdrWriter writer) {
                    writer.writeUInt8(this.getType());
                    writer.writeUInt8(this.getId());
                    writer.writeFloat(this.getIntensity());
    }

    public static JoyFeedback readFrom(Ros2CdrReader reader) {
                    return JoyFeedback.builder()
                            .type(reader.readUInt8())
                            .id(reader.readUInt8())
                            .intensity(reader.readFloat())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, JoyFeedback::writeTo);
    }

    public static JoyFeedback decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, JoyFeedback::readFrom);
    }
}
