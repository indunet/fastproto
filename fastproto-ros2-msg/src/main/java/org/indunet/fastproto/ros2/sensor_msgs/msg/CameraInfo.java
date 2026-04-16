package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/CameraInfo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CameraInfo {
    private Header header;
    private long height;
    private long width;
    private String distortionModel;
    private double[] d;
    private double[] k;
    private double[] r;
    private double[] p;
    private long binningX;
    private long binningY;
    private RegionOfInterest roi;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeUInt32(this.getHeight());
                    writer.writeUInt32(this.getWidth());
                    writer.writeString(this.getDistortionModel());
                    writer.writeDoubleSequence(this.getD());
                    Ros2CodecSupport.writeFixedDoubleArray(writer, this.getK(), 9, "k");
                    Ros2CodecSupport.writeFixedDoubleArray(writer, this.getR(), 9, "r");
                    Ros2CodecSupport.writeFixedDoubleArray(writer, this.getP(), 12, "p");
                    writer.writeUInt32(this.getBinningX());
                    writer.writeUInt32(this.getBinningY());
                    this.getRoi().writeTo(writer);
    }

    public static CameraInfo readFrom(Ros2CdrReader reader) {
                    return CameraInfo.builder()
                            .header(Header.readFrom(reader))
                            .height(reader.readUInt32())
                            .width(reader.readUInt32())
                            .distortionModel(reader.readString())
                            .d(reader.readDoubleSequence())
                            .k(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                            .r(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                            .p(Ros2CodecSupport.readFixedDoubleArray(reader, 12))
                            .binningX(reader.readUInt32())
                            .binningY(reader.readUInt32())
                            .roi(RegionOfInterest.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, CameraInfo::writeTo);
    }

    public static CameraInfo decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, CameraInfo::readFrom);
    }
}
