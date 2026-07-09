package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/Image
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private Header header;
    private long height;
    private long width;
    private String encoding;
    private int isBigendian;
    private long step;
    private byte[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeUInt32(this.getHeight());
                    writer.writeUInt32(this.getWidth());
                    writer.writeString(this.getEncoding());
                    writer.writeUInt8(this.getIsBigendian());
                    writer.writeUInt32(this.getStep());
                    writer.writeByteSequence(this.getData());
    }

    public static Image readFrom(Ros2CdrReader reader) {
                    return Image.builder()
                            .header(Header.readFrom(reader))
                            .height(reader.readUInt32())
                            .width(reader.readUInt32())
                            .encoding(reader.readString())
                            .isBigendian(reader.readUInt8())
                            .step(reader.readUInt32())
                            .data(reader.readByteSequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Image::writeTo);
    }

    public static Image decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Image::readFrom);
    }
}
