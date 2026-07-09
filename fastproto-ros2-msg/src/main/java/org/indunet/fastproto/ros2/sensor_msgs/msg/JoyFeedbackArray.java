package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
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

    public void writeTo(Ros2CdrWriter writer) {
                    Ros2CodecSupport.writeJoyFeedbackArray(writer, this.getArray());
    }

    public static JoyFeedbackArray readFrom(Ros2CdrReader reader) {
                    return JoyFeedbackArray.builder()
                            .array(Ros2CodecSupport.readJoyFeedbackArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, JoyFeedbackArray::writeTo);
    }

    public static JoyFeedbackArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, JoyFeedbackArray::readFrom);
    }
}
