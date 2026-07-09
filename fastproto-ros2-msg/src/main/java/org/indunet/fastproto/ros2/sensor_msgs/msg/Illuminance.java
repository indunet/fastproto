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
 * sensor_msgs/msg/Illuminance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Illuminance {
    private Header header;
    private double illuminance;
    private double variance;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeDouble(this.getIlluminance());
                    writer.writeDouble(this.getVariance());
    }

    public static Illuminance readFrom(Ros2CdrReader reader) {
                    return Illuminance.builder()
                            .header(Header.readFrom(reader))
                            .illuminance(reader.readDouble())
                            .variance(reader.readDouble())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Illuminance::writeTo);
    }

    public static Illuminance decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Illuminance::readFrom);
    }
}
