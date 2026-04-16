package org.indunet.fastproto.ros2.shape_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * shape_msgs/msg/MeshTriangle
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeshTriangle {
    public static final int SIZE = 12;

    private long[] vertexIndices;

    public void writeTo(Ros2CdrWriter writer) {
                    Ros2CodecSupport.writeFixedUInt32Array(writer, this.getVertexIndices(), 3, "vertex_indices");
    }

    public static MeshTriangle readFrom(Ros2CdrReader reader) {
                    return MeshTriangle.builder()
                            .vertexIndices(Ros2CodecSupport.readFixedUInt32Array(reader, 3))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, MeshTriangle::writeTo);
    }

    public static MeshTriangle decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, MeshTriangle::readFrom);
    }
}
