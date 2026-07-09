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
 * geometry_msgs/msg/Vector3Stamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vector3Stamped {
    private Header header;
    private Vector3 vector;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getVector().writeTo(writer);
    }

    public static Vector3Stamped readFrom(Ros2CdrReader reader) {
                    return Vector3Stamped.builder()
                            .header(Header.readFrom(reader))
                            .vector(Vector3.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Vector3Stamped::writeTo);
    }

    public static Vector3Stamped decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Vector3Stamped::readFrom);
    }
}
