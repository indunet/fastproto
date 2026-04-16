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
 * sensor_msgs/msg/LaserEcho
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaserEcho {
    private float[] echoes;

    public void writeTo(Ros2CdrWriter writer) {
                    writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(this.getEchoes()));
    }

    public static LaserEcho readFrom(Ros2CdrReader reader) {
                    return LaserEcho.builder()
                            .echoes(reader.readFloatSequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, LaserEcho::writeTo);
    }

    public static LaserEcho decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, LaserEcho::readFrom);
    }
}
