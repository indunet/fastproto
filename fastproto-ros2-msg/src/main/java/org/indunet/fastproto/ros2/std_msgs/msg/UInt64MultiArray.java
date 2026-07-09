package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * std_msgs/msg/UInt64MultiArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UInt64MultiArray {
    private MultiArrayLayout layout;
    private BigInteger[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getLayout().writeTo(writer);
                    writer.writeUInt64Sequence(Ros2CodecSupport.safeBigIntegerArray(this.getData()));
    }

    public static UInt64MultiArray readFrom(Ros2CdrReader reader) {
                    return UInt64MultiArray.builder()
                            .layout(MultiArrayLayout.readFrom(reader))
                            .data(reader.readUInt64Sequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, UInt64MultiArray::writeTo);
    }

    public static UInt64MultiArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, UInt64MultiArray::readFrom);
    }
}
