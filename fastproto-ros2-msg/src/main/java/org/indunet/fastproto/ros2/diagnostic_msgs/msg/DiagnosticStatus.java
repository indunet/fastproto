package org.indunet.fastproto.ros2.diagnostic_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * diagnostic_msgs/msg/DiagnosticStatus
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticStatus {
    public static final int OK = 0;
    public static final int WARN = 1;
    public static final int ERROR = 2;
    public static final int STALE = 3;

    private int level;
    private String name;
    private String message;
    private String hardwareId;
    private KeyValue[] values;

    public void writeTo(Ros2CdrWriter writer) {
                    writer.writeUInt8(this.getLevel());
                    writer.writeString(this.getName());
                    writer.writeString(this.getMessage());
                    writer.writeString(this.getHardwareId());
                    Ros2CodecSupport.writeKeyValueArray(writer, this.getValues());
    }

    public static DiagnosticStatus readFrom(Ros2CdrReader reader) {
                    return DiagnosticStatus.builder()
                            .level(reader.readUInt8())
                            .name(reader.readString())
                            .message(reader.readString())
                            .hardwareId(reader.readString())
                            .values(Ros2CodecSupport.readKeyValueArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, DiagnosticStatus::writeTo);
    }

    public static DiagnosticStatus decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, DiagnosticStatus::readFrom);
    }
}
