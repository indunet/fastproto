package org.indunet.fastproto.ros2.sensor_msgs.msg;

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
 * sensor_msgs/msg/PointCloud2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointCloud2 {
    private Header header;
    private long height;
    private long width;
    private PointField[] fields;
    private boolean isBigendian;
    private long pointStep;
    private long rowStep;
    private byte[] data;
    private boolean isDense;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeUInt32(this.getHeight());
                    writer.writeUInt32(this.getWidth());
                    Ros2CodecSupport.writePointFieldArray(writer, this.getFields());
                    writer.writeBool(this.isBigendian());
                    writer.writeUInt32(this.getPointStep());
                    writer.writeUInt32(this.getRowStep());
                    writer.writeByteSequence(this.getData());
                    writer.writeBool(this.isDense());
    }

    public static PointCloud2 readFrom(Ros2CdrReader reader) {
                    return PointCloud2.builder()
                            .header(Header.readFrom(reader))
                            .height(reader.readUInt32())
                            .width(reader.readUInt32())
                            .fields(Ros2CodecSupport.readPointFieldArray(reader))
                            .isBigendian(reader.readBool())
                            .pointStep(reader.readUInt32())
                            .rowStep(reader.readUInt32())
                            .data(reader.readByteSequence())
                            .isDense(reader.readBool())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, PointCloud2::writeTo);
    }

    public static PointCloud2 decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, PointCloud2::readFrom);
    }
}
