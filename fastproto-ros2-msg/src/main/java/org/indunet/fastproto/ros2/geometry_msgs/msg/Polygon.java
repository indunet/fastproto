package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/Polygon
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Polygon {
    private Point32[] points;

    public void writeTo(Ros2CdrWriter writer) {
                    Ros2CodecSupport.writePoint32Array(writer, this.getPoints());
    }

    public static Polygon readFrom(Ros2CdrReader reader) {
                    return Polygon.builder()
                            .points(Ros2CodecSupport.readPoint32Array(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Polygon::writeTo);
    }

    public static Polygon decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Polygon::readFrom);
    }
}
