package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Float32MultiArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Float32MultiArray {
    private MultiArrayLayout layout;
    private float[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getLayout().writeTo(writer);
                    writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(this.getData()));
    }

    public static Float32MultiArray readFrom(Ros2CdrReader reader) {
                    return Float32MultiArray.builder()
                            .layout(MultiArrayLayout.readFrom(reader))
                            .data(reader.readFloatSequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Float32MultiArray::writeTo);
    }

    public static Float32MultiArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Float32MultiArray::readFrom);
    }
}
