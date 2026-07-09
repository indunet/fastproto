package org.indunet.fastproto.ros2.diagnostic_msgs.msg;

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
 * diagnostic_msgs/msg/DiagnosticArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticArray {
    private Header header;
    private DiagnosticStatus[] status;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    Ros2CodecSupport.writeDiagnosticStatusArray(writer, this.getStatus());
    }

    public static DiagnosticArray readFrom(Ros2CdrReader reader) {
                    return DiagnosticArray.builder()
                            .header(Header.readFrom(reader))
                            .status(Ros2CodecSupport.readDiagnosticStatusArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, DiagnosticArray::writeTo);
    }

    public static DiagnosticArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, DiagnosticArray::readFrom);
    }
}
