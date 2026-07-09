package org.indunet.fastproto.ros2.nav_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * nav_msgs/msg/OccupancyGrid
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OccupancyGrid {
    private Header header;
    private MapMetaData info;
    private byte[] data;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    this.getInfo().writeTo(writer);
                    writer.writeByteSequence(this.getData());
    }

    public static OccupancyGrid readFrom(Ros2CdrReader reader) {
                    return OccupancyGrid.builder()
                            .header(Header.readFrom(reader))
                            .info(MapMetaData.readFrom(reader))
                            .data(reader.readByteSequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, OccupancyGrid::writeTo);
    }

    public static OccupancyGrid decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, OccupancyGrid::readFrom);
    }
}
