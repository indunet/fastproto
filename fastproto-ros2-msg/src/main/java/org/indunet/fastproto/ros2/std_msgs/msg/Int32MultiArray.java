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
 * std_msgs/msg/Int32MultiArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Int32MultiArray {
    private MultiArrayLayout layout;
    private int[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getLayout().writeTo(writer);
                    writer.writeInt32Sequence(Ros2CodecSupport.safeIntArray(this.getData()));
    }

    public static Int32MultiArray readFrom(Ros2CdrReader reader) {
                    return Int32MultiArray.builder()
                            .layout(MultiArrayLayout.readFrom(reader))
                            .data(reader.readInt32Sequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Int32MultiArray::writeTo);
    }

    public static Int32MultiArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Int32MultiArray::readFrom);
    }
}
