package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/TimeReference
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeReference {
    private Header header;
    private Time timeRef;
    private String source;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getTimeRef().writeTo(writer);
                    writer.writeString(this.getSource() == null ? "" : this.getSource());
    }

    public static TimeReference readFrom(Ros2CdrReader reader) {
                    return TimeReference.builder()
                            .header(Header.readFrom(reader))
                            .timeRef(Time.readFrom(reader))
                            .source(reader.readString())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, TimeReference::writeTo);
    }

    public static TimeReference decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, TimeReference::readFrom);
    }
}
