package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point32;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/PointCloud
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointCloud {
    private Header header;
    private Point32[] points;
    private ChannelFloat32[] channels;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    Ros2CodecSupport.writePoint32Array(writer, this.getPoints());
                    Ros2CodecSupport.writeChannelFloat32Array(writer, this.getChannels());
    }

    public static PointCloud readFrom(Ros2CdrReader reader) {
                    return PointCloud.builder()
                            .header(Header.readFrom(reader))
                            .points(Ros2CodecSupport.readPoint32Array(reader))
                            .channels(Ros2CodecSupport.readChannelFloat32Array(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, PointCloud::writeTo);
    }

    public static PointCloud decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, PointCloud::readFrom);
    }
}
