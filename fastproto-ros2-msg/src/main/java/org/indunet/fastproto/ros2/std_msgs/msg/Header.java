package org.indunet.fastproto.ros2.std_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * std_msgs/msg/Header
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Header {
    private Time stamp;
    private java.lang.String frameId;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getStamp().writeTo(writer);
                    writer.writeString(this.getFrameId());
    }

    public static Header readFrom(Ros2CdrReader reader) {
                    return Header.builder()
                            .stamp(Time.readFrom(reader))
                            .frameId(reader.readString())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Header::writeTo);
    }

    public static Header decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Header::readFrom);
    }
}
