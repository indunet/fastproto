package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * geometry_msgs/msg/TransformStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransformStamped {
    private Header header;
    private String childFrameId;
    private Transform transform;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeString(this.getChildFrameId());
                    this.getTransform().writeTo(writer);
    }

    public static TransformStamped readFrom(Ros2CdrReader reader) {
                    return TransformStamped.builder()
                            .header(Header.readFrom(reader))
                            .childFrameId(reader.readString())
                            .transform(Transform.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, TransformStamped::writeTo);
    }

    public static TransformStamped decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, TransformStamped::readFrom);
    }
}
