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
 * nav_msgs/msg/Goals
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goals {
    private Header header;
    private PoseStamped[] goals;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    Ros2CodecSupport.writePoseStampedArray(writer, this.getGoals());
    }

    public static Goals readFrom(Ros2CdrReader reader) {
                    return Goals.builder()
                            .header(Header.readFrom(reader))
                            .goals(Ros2CodecSupport.readPoseStampedArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Goals::writeTo);
    }

    public static Goals decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Goals::readFrom);
    }
}
