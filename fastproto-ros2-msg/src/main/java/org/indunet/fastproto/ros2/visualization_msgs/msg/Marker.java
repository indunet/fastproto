package org.indunet.fastproto.ros2.visualization_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Duration;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Pose;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Vector3;
import org.indunet.fastproto.ros2.sensor_msgs.msg.CompressedImage;
import org.indunet.fastproto.ros2.std_msgs.msg.ColorRGBA;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * visualization_msgs/msg/Marker
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Marker {
    public static final int ARROW = 0;
    public static final int CUBE = 1;
    public static final int SPHERE = 2;
    public static final int CYLINDER = 3;
    public static final int LINE_STRIP = 4;
    public static final int LINE_LIST = 5;
    public static final int CUBE_LIST = 6;
    public static final int SPHERE_LIST = 7;
    public static final int POINTS = 8;
    public static final int TEXT_VIEW_FACING = 9;
    public static final int MESH_RESOURCE = 10;
    public static final int TRIANGLE_LIST = 11;
    public static final int ARROW_STRIP = 12;

    public static final int ADD = 0;
    public static final int MODIFY = 0;
    public static final int DELETE = 2;
    public static final int DELETEALL = 3;

    private Header header;
    private String ns;
    private int id;
    private int type;
    private int action;
    private Pose pose;
    private Vector3 scale;
    private ColorRGBA color;
    private Duration lifetime;
    private boolean frameLocked;
    private Point[] points;
    private ColorRGBA[] colors;
    private String textureResource;
    private CompressedImage texture;
    private UVCoordinate[] uvCoordinates;
    private String text;
    private String meshResource;
    private MeshFile meshFile;
    private boolean meshUseEmbeddedMaterials;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeString(this.getNs());
                    writer.writeInt32(this.getId());
                    writer.writeInt32(this.getType());
                    writer.writeInt32(this.getAction());
                    this.getPose().writeTo(writer);
                    this.getScale().writeTo(writer);
                    this.getColor().writeTo(writer);
                    this.getLifetime().writeTo(writer);
                    writer.writeBool(this.isFrameLocked());
                    Ros2CodecSupport.writePointArray(writer, this.getPoints());
                    Ros2CodecSupport.writeColorRGBAArray(writer, this.getColors());
                    writer.writeString(this.getTextureResource());
                    this.getTexture().writeTo(writer);
                    Ros2CodecSupport.writeUVCoordinateArray(writer, this.getUvCoordinates());
                    writer.writeString(this.getText());
                    writer.writeString(this.getMeshResource());
                    this.getMeshFile().writeTo(writer);
                    writer.writeBool(this.isMeshUseEmbeddedMaterials());
    }

    public static Marker readFrom(Ros2CdrReader reader) {
                    return Marker.builder()
                            .header(Header.readFrom(reader))
                            .ns(reader.readString())
                            .id(reader.readInt32())
                            .type(reader.readInt32())
                            .action(reader.readInt32())
                            .pose(Pose.readFrom(reader))
                            .scale(Vector3.readFrom(reader))
                            .color(ColorRGBA.readFrom(reader))
                            .lifetime(Duration.readFrom(reader))
                            .frameLocked(reader.readBool())
                            .points(Ros2CodecSupport.readPointArray(reader))
                            .colors(Ros2CodecSupport.readColorRGBAArray(reader))
                            .textureResource(reader.readString())
                            .texture(CompressedImage.readFrom(reader))
                            .uvCoordinates(Ros2CodecSupport.readUVCoordinateArray(reader))
                            .text(reader.readString())
                            .meshResource(reader.readString())
                            .meshFile(MeshFile.readFrom(reader))
                            .meshUseEmbeddedMaterials(reader.readBool())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Marker::writeTo);
    }

    public static Marker decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Marker::readFrom);
    }
}
