package org.indunet.fastproto.ros2.tf2_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TransformStamped;

/**
 * tf2_msgs/msg/TFMessage
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TFMessage {
    private TransformStamped[] transforms;

    public void writeTo(Ros2CdrWriter writer) {
                    Ros2CodecSupport.writeTransformStampedArray(writer, this.getTransforms());
    }

    public static TFMessage readFrom(Ros2CdrReader reader) {
                    return TFMessage.builder()
                            .transforms(Ros2CodecSupport.readTransformStampedArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, TFMessage::writeTo);
    }

    public static TFMessage decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, TFMessage::readFrom);
    }
}
