package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Int8MultiArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Int8MultiArray {
    private MultiArrayLayout layout;
    private byte[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getLayout().writeTo(writer);
                    writer.writeByteSequence(this.getData() == null ? new byte[0] : this.getData());
    }

    public static Int8MultiArray readFrom(Ros2CdrReader reader) {
                    return Int8MultiArray.builder()
                            .layout(MultiArrayLayout.readFrom(reader))
                            .data(reader.readByteSequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Int8MultiArray::writeTo);
    }

    public static Int8MultiArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Int8MultiArray::readFrom);
    }
}
