package org.indunet.fastproto.ros2.nav_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseStamped;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * nav_msgs/msg/Path
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Path {
    private Header header;
    private PoseStamped[] poses;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    Ros2CodecSupport.writePoseStampedArray(writer, this.getPoses());
    }

    public static Path readFrom(Ros2CdrReader reader) {
                    return Path.builder()
                            .header(Header.readFrom(reader))
                            .poses(Ros2CodecSupport.readPoseStampedArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Path::writeTo);
    }

    public static Path decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Path::readFrom);
    }
}
