package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/MultiArrayDimension
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiArrayDimension {
    private java.lang.String label;
    private long size;
    private long stride;

    public void writeTo(Ros2CdrWriter writer) {
                    writer.writeString(this.getLabel());
                    writer.writeUInt32(this.getSize());
                    writer.writeUInt32(this.getStride());
    }

    public static MultiArrayDimension readFrom(Ros2CdrReader reader) {
                    return MultiArrayDimension.builder()
                            .label(reader.readString())
                            .size(reader.readUInt32())
                            .stride(reader.readUInt32())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, MultiArrayDimension::writeTo);
    }

    public static MultiArrayDimension decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, MultiArrayDimension::readFrom);
    }
}
