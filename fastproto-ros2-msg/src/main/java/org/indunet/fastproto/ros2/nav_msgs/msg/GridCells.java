package org.indunet.fastproto.ros2.nav_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * nav_msgs/msg/GridCells
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GridCells {
    private Header header;
    private float cellWidth;
    private float cellHeight;
    private Point[] cells;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeFloat(this.getCellWidth());
                    writer.writeFloat(this.getCellHeight());
                    Ros2CodecSupport.writePointArray(writer, this.getCells());
    }

    public static GridCells readFrom(Ros2CdrReader reader) {
                    return GridCells.builder()
                            .header(Header.readFrom(reader))
                            .cellWidth(reader.readFloat())
                            .cellHeight(reader.readFloat())
                            .cells(Ros2CodecSupport.readPointArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, GridCells::writeTo);
    }

    public static GridCells decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, GridCells::readFrom);
    }
}
