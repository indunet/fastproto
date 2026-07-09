package org.indunet.fastproto.ros2.visualization_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * visualization_msgs/msg/MeshFile
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeshFile {
    private String filename;
    private byte[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    writer.writeString(this.getFilename());
                    writer.writeByteSequence(this.getData() == null ? new byte[0] : this.getData());
    }

    public static MeshFile readFrom(Ros2CdrReader reader) {
                    return MeshFile.builder()
                            .filename(reader.readString())
                            .data(reader.readByteSequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, MeshFile::writeTo);
    }

    public static MeshFile decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, MeshFile::readFrom);
    }
}
