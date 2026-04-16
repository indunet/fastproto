package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/RelativeHumidity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelativeHumidity {
    private Header header;
    private double relativeHumidity;
    private double variance;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeDouble(this.getRelativeHumidity());
                    writer.writeDouble(this.getVariance());
    }

    public static RelativeHumidity readFrom(Ros2CdrReader reader) {
                    return RelativeHumidity.builder()
                            .header(Header.readFrom(reader))
                            .relativeHumidity(reader.readDouble())
                            .variance(reader.readDouble())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, RelativeHumidity::writeTo);
    }

    public static RelativeHumidity decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, RelativeHumidity::readFrom);
    }
}
