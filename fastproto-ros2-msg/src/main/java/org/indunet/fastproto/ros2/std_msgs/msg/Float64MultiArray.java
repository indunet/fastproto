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
 * std_msgs/msg/Float64MultiArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Float64MultiArray {
    private MultiArrayLayout layout;
    private double[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getLayout().writeTo(writer);
                    writer.writeDoubleSequence(Ros2CodecSupport.safeDoubleArray(this.getData()));
    }

    public static Float64MultiArray readFrom(Ros2CdrReader reader) {
                    return Float64MultiArray.builder()
                            .layout(MultiArrayLayout.readFrom(reader))
                            .data(reader.readDoubleSequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Float64MultiArray::writeTo);
    }

    public static Float64MultiArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Float64MultiArray::readFrom);
    }
}
