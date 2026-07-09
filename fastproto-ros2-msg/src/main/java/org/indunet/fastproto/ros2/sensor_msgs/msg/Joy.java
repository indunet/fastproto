package org.indunet.fastproto.ros2.sensor_msgs.msg;

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
 * sensor_msgs/msg/Joy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Joy {
    private Header header;
    private float[] axes;
    private int[] buttons;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(this.getAxes()));
                    writer.writeInt32Sequence(Ros2CodecSupport.safeIntArray(this.getButtons()));
    }

    public static Joy readFrom(Ros2CdrReader reader) {
                    return Joy.builder()
                            .header(Header.readFrom(reader))
                            .axes(reader.readFloatSequence())
                            .buttons(reader.readInt32Sequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Joy::writeTo);
    }

    public static Joy decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Joy::readFrom);
    }
}
