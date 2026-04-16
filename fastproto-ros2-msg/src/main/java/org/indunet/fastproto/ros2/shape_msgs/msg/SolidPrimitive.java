package org.indunet.fastproto.ros2.shape_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Polygon;

/**
 * shape_msgs/msg/SolidPrimitive
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolidPrimitive {
    public static final int BOX = 1;
    public static final int SPHERE = 2;
    public static final int CYLINDER = 3;
    public static final int CONE = 4;
    public static final int PRISM = 5;

    public static final int BOX_X = 0;
    public static final int BOX_Y = 1;
    public static final int BOX_Z = 2;
    public static final int SPHERE_RADIUS = 0;
    public static final int CYLINDER_HEIGHT = 0;
    public static final int CYLINDER_RADIUS = 1;
    public static final int CONE_HEIGHT = 0;
    public static final int CONE_RADIUS = 1;
    public static final int PRISM_HEIGHT = 0;

    private int type;
    private double[] dimensions;
    private Polygon polygon;

    public void writeTo(Ros2CdrWriter writer) {
                    double[] dimensions = Ros2CodecSupport.safeDoubleArray(this.getDimensions());
                    if (dimensions.length > 3) {
                        throw new IllegalArgumentException("dimensions must contain at most 3 values.");
                    }
        
                    writer.writeUInt8(this.getType());
                    writer.writeDoubleSequence(dimensions);
                    this.getPolygon().writeTo(writer);
    }

    public static SolidPrimitive readFrom(Ros2CdrReader reader) {
                    return SolidPrimitive.builder()
                            .type(reader.readUInt8())
                            .dimensions(reader.readDoubleSequence())
                            .polygon(Polygon.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, SolidPrimitive::writeTo);
    }

    public static SolidPrimitive decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, SolidPrimitive::readFrom);
    }
}
