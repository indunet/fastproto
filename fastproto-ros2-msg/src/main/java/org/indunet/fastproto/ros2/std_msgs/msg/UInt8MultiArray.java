package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/UInt8MultiArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UInt8MultiArray {
    private MultiArrayLayout layout;
    private byte[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getLayout().writeTo(writer);
                    writer.writeByteSequence(this.getData() == null ? new byte[0] : this.getData());
    }

    public static UInt8MultiArray readFrom(Ros2CdrReader reader) {
                    return UInt8MultiArray.builder()
                            .layout(MultiArrayLayout.readFrom(reader))
                            .data(reader.readByteSequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, UInt8MultiArray::writeTo);
    }

    public static UInt8MultiArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, UInt8MultiArray::readFrom);
    }
}
