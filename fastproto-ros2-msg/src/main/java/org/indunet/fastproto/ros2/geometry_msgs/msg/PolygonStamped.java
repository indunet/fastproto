package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * geometry_msgs/msg/PolygonStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolygonStamped {
    private Header header;
    private Polygon polygon;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getPolygon().writeTo(writer);
    }

    public static PolygonStamped readFrom(Ros2CdrReader reader) {
                    return PolygonStamped.builder()
                            .header(Header.readFrom(reader))
                            .polygon(Polygon.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, PolygonStamped::writeTo);
    }

    public static PolygonStamped decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, PolygonStamped::readFrom);
    }
}
