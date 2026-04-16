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
 * std_msgs/msg/UInt16MultiArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UInt16MultiArray {
    private MultiArrayLayout layout;
    private int[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getLayout().writeTo(writer);
                    writer.writeUInt16Sequence(Ros2CodecSupport.safeIntArray(this.getData()));
    }

    public static UInt16MultiArray readFrom(Ros2CdrReader reader) {
                    return UInt16MultiArray.builder()
                            .layout(MultiArrayLayout.readFrom(reader))
                            .data(reader.readUInt16Sequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, UInt16MultiArray::writeTo);
    }

    public static UInt16MultiArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, UInt16MultiArray::readFrom);
    }
}
