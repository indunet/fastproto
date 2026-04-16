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
 * geometry_msgs/msg/AccelStamped
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccelStamped {
    private Header header;
    private Accel accel;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getAccel().writeTo(writer);
    }

    public static AccelStamped readFrom(Ros2CdrReader reader) {
                    return AccelStamped.builder()
                            .header(Header.readFrom(reader))
                            .accel(Accel.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, AccelStamped::writeTo);
    }

    public static AccelStamped decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, AccelStamped::readFrom);
    }
}
