package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Int64MultiArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Int64MultiArray {
    private MultiArrayLayout layout;
    private long[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getLayout().writeTo(writer);
                    writer.writeInt64Sequence(this.getData() == null ? new long[0] : this.getData());
    }

    public static Int64MultiArray readFrom(Ros2CdrReader reader) {
                    return Int64MultiArray.builder()
                            .layout(MultiArrayLayout.readFrom(reader))
                            .data(reader.readInt64Sequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Int64MultiArray::writeTo);
    }

    public static Int64MultiArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Int64MultiArray::readFrom);
    }
}
