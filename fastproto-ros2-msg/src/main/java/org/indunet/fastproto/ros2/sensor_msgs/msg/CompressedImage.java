package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/CompressedImage
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompressedImage {
    private Header header;
    private String format;
    private byte[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeString(this.getFormat());
                    writer.writeByteSequence(this.getData());
    }

    public static CompressedImage readFrom(Ros2CdrReader reader) {
                    return CompressedImage.builder()
                            .header(Header.readFrom(reader))
                            .format(reader.readString())
                            .data(reader.readByteSequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, CompressedImage::writeTo);
    }

    public static CompressedImage decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, CompressedImage::readFrom);
    }
}
