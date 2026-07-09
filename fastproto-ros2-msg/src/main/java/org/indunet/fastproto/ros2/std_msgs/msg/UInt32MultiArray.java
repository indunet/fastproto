package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/UInt32MultiArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UInt32MultiArray {
    private MultiArrayLayout layout;
    private long[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getLayout().writeTo(writer);
                    writer.writeUInt32Sequence(this.getData() == null ? new long[0] : this.getData());
    }

    public static UInt32MultiArray readFrom(Ros2CdrReader reader) {
                    return UInt32MultiArray.builder()
                            .layout(MultiArrayLayout.readFrom(reader))
                            .data(reader.readUInt32Sequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, UInt32MultiArray::writeTo);
    }

    public static UInt32MultiArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, UInt32MultiArray::readFrom);
    }
}
