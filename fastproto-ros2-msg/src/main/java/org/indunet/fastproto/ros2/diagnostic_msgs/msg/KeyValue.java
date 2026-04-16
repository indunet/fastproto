package org.indunet.fastproto.ros2.diagnostic_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * diagnostic_msgs/msg/KeyValue
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyValue {
    private String key;
    private String value;

    public void writeTo(Ros2CdrWriter writer) {
                    writer.writeString(this.getKey());
                    writer.writeString(this.getValue());
    }

    public static KeyValue readFrom(Ros2CdrReader reader) {
                    return KeyValue.builder()
                            .key(reader.readString())
                            .value(reader.readString())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, KeyValue::writeTo);
    }

    public static KeyValue decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, KeyValue::readFrom);
    }
}
