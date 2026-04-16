package org.indunet.fastproto.ros2.shape_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point;

/**
 * shape_msgs/msg/Mesh
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mesh {
    private MeshTriangle[] triangles;
    private Point[] vertices;

    public void writeTo(Ros2CdrWriter writer) {
                    Ros2CodecSupport.writeMeshTriangleArray(writer, this.getTriangles());
                    Ros2CodecSupport.writePointArray(writer, this.getVertices());
    }

    public static Mesh readFrom(Ros2CdrReader reader) {
                    return Mesh.builder()
                            .triangles(Ros2CodecSupport.readMeshTriangleArray(reader))
                            .vertices(Ros2CodecSupport.readPointArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Mesh::writeTo);
    }

    public static Mesh decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Mesh::readFrom);
    }
}
