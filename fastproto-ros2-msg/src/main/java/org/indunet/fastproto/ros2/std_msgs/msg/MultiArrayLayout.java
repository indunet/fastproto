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
 * std_msgs/msg/MultiArrayLayout
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiArrayLayout {
    private MultiArrayDimension[] dim;
    private long dataOffset;

    public void writeTo(Ros2CdrWriter writer) {
                    Ros2CodecSupport.writeMultiArrayDimensionArray(writer, this.getDim());
                    writer.writeUInt32(this.getDataOffset());
    }

    public static MultiArrayLayout readFrom(Ros2CdrReader reader) {
                    return MultiArrayLayout.builder()
                            .dim(Ros2CodecSupport.readMultiArrayDimensionArray(reader))
                            .dataOffset(reader.readUInt32())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, MultiArrayLayout::writeTo);
    }

    public static MultiArrayLayout decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, MultiArrayLayout::readFrom);
    }
}
