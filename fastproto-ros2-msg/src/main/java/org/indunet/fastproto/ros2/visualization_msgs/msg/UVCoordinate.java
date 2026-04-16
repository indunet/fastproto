package org.indunet.fastproto.ros2.visualization_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * visualization_msgs/msg/UVCoordinate
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UVCoordinate {
    public static final int SIZE = 8;

    private float u;
    private float v;

    public void writeTo(Ros2CdrWriter writer) {
                    writer.writeFloat(this.getU());
                    writer.writeFloat(this.getV());
    }

    public static UVCoordinate readFrom(Ros2CdrReader reader) {
                    return UVCoordinate.builder()
                            .u(reader.readFloat())
                            .v(reader.readFloat())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, UVCoordinate::writeTo);
    }

    public static UVCoordinate decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, UVCoordinate::readFrom);
    }
}
