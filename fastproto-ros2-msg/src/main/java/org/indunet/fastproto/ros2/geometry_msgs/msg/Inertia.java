package org.indunet.fastproto.ros2.geometry_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * geometry_msgs/msg/Inertia
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inertia {
    private double m;
    private Vector3 com;
    private double ixx;
    private double ixy;
    private double ixz;
    private double iyy;
    private double iyz;
    private double izz;

    public void writeTo(Ros2CdrWriter writer) {
                    writer.writeDouble(this.getM());
                    this.getCom().writeTo(writer);
                    writer.writeDouble(this.getIxx());
                    writer.writeDouble(this.getIxy());
                    writer.writeDouble(this.getIxz());
                    writer.writeDouble(this.getIyy());
                    writer.writeDouble(this.getIyz());
                    writer.writeDouble(this.getIzz());
    }

    public static Inertia readFrom(Ros2CdrReader reader) {
                    return Inertia.builder()
                            .m(reader.readDouble())
                            .com(Vector3.readFrom(reader))
                            .ixx(reader.readDouble())
                            .ixy(reader.readDouble())
                            .ixz(reader.readDouble())
                            .iyy(reader.readDouble())
                            .iyz(reader.readDouble())
                            .izz(reader.readDouble())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Inertia::writeTo);
    }

    public static Inertia decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Inertia::readFrom);
    }
}
