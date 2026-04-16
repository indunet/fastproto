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
 * sensor_msgs/msg/FluidPressure
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FluidPressure {
    private Header header;
    private double fluidPressure;
    private double variance;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeDouble(this.getFluidPressure());
                    writer.writeDouble(this.getVariance());
    }

    public static FluidPressure readFrom(Ros2CdrReader reader) {
                    return FluidPressure.builder()
                            .header(Header.readFrom(reader))
                            .fluidPressure(reader.readDouble())
                            .variance(reader.readDouble())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, FluidPressure::writeTo);
    }

    public static FluidPressure decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, FluidPressure::readFrom);
    }
}
