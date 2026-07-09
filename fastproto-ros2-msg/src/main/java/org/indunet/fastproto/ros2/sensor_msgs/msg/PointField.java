package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/PointField
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointField {
    public static final int INT8 = 1;
    public static final int UINT8 = 2;
    public static final int INT16 = 3;
    public static final int UINT16 = 4;
    public static final int INT32 = 5;
    public static final int UINT32 = 6;
    public static final int FLOAT32 = 7;
    public static final int FLOAT64 = 8;

    private String name;
    private long offset;
    private int datatype;
    private long count;

    public void writeTo(Ros2CdrWriter writer) {
                    writer.writeString(this.getName());
                    writer.writeUInt32(this.getOffset());
                    writer.writeUInt8(this.getDatatype());
                    writer.writeUInt32(this.getCount());
    }

    public static PointField readFrom(Ros2CdrReader reader) {
                    return PointField.builder()
                            .name(reader.readString())
                            .offset(reader.readUInt32())
                            .datatype(reader.readUInt8())
                            .count(reader.readUInt32())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, PointField::writeTo);
    }

    public static PointField decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, PointField::readFrom);
    }
}
