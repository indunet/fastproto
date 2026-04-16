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
 * sensor_msgs/msg/Temperature
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Temperature {
    private Header header;
    private double temperature;
    private double variance;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeDouble(this.getTemperature());
                    writer.writeDouble(this.getVariance());
    }

    public static Temperature readFrom(Ros2CdrReader reader) {
                    return Temperature.builder()
                            .header(Header.readFrom(reader))
                            .temperature(reader.readDouble())
                            .variance(reader.readDouble())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Temperature::writeTo);
    }

    public static Temperature decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Temperature::readFrom);
    }
}
