package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/ChannelFloat32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelFloat32 {
    private String name;
    private float[] values;

    public void writeTo(Ros2CdrWriter writer) {
                    writer.writeString(this.getName());
                    writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(this.getValues()));
    }

    public static ChannelFloat32 readFrom(Ros2CdrReader reader) {
                    return ChannelFloat32.builder()
                            .name(reader.readString())
                            .values(reader.readFloatSequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, ChannelFloat32::writeTo);
    }

    public static ChannelFloat32 decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, ChannelFloat32::readFrom);
    }
}
