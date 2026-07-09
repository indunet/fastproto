package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * geometry_msgs/msg/PoseArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoseArray {
    private Header header;
    private Pose[] poses;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    Ros2CodecSupport.writePoseArray(writer, this.getPoses());
    }

    public static PoseArray readFrom(Ros2CdrReader reader) {
                    return PoseArray.builder()
                            .header(Header.readFrom(reader))
                            .poses(Ros2CodecSupport.readPoseArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, PoseArray::writeTo);
    }

    public static PoseArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, PoseArray::readFrom);
    }
}
