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
 * sensor_msgs/msg/MultiEchoLaserScan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiEchoLaserScan {
    private Header header;
    private float angleMin;
    private float angleMax;
    private float angleIncrement;
    private float timeIncrement;
    private float scanTime;
    private float rangeMin;
    private float rangeMax;
    private LaserEcho[] ranges;
    private LaserEcho[] intensities;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeFloat(this.getAngleMin());
                    writer.writeFloat(this.getAngleMax());
                    writer.writeFloat(this.getAngleIncrement());
                    writer.writeFloat(this.getTimeIncrement());
                    writer.writeFloat(this.getScanTime());
                    writer.writeFloat(this.getRangeMin());
                    writer.writeFloat(this.getRangeMax());
                    Ros2CodecSupport.writeLaserEchoArray(writer, this.getRanges());
                    Ros2CodecSupport.writeLaserEchoArray(writer, this.getIntensities());
    }

    public static MultiEchoLaserScan readFrom(Ros2CdrReader reader) {
                    return MultiEchoLaserScan.builder()
                            .header(Header.readFrom(reader))
                            .angleMin(reader.readFloat())
                            .angleMax(reader.readFloat())
                            .angleIncrement(reader.readFloat())
                            .timeIncrement(reader.readFloat())
                            .scanTime(reader.readFloat())
                            .rangeMin(reader.readFloat())
                            .rangeMax(reader.readFloat())
                            .ranges(Ros2CodecSupport.readLaserEchoArray(reader))
                            .intensities(Ros2CodecSupport.readLaserEchoArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, MultiEchoLaserScan::writeTo);
    }

    public static MultiEchoLaserScan decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, MultiEchoLaserScan::readFrom);
    }
}
