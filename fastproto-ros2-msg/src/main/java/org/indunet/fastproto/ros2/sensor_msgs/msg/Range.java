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
 * sensor_msgs/msg/Range
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Range {
    public static final int ULTRASOUND = 0;
    public static final int INFRARED = 1;

    private Header header;
    private int radiationType;
    private float fieldOfView;
    private float minRange;
    private float maxRange;
    private float range;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeUInt8(this.getRadiationType());
                    writer.writeFloat(this.getFieldOfView());
                    writer.writeFloat(this.getMinRange());
                    writer.writeFloat(this.getMaxRange());
                    writer.writeFloat(this.getRange());
    }

    public static Range readFrom(Ros2CdrReader reader) {
                    return Range.builder()
                            .header(Header.readFrom(reader))
                            .radiationType(reader.readUInt8())
                            .fieldOfView(reader.readFloat())
                            .minRange(reader.readFloat())
                            .maxRange(reader.readFloat())
                            .range(reader.readFloat())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, Range::writeTo);
    }

    public static Range decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, Range::readFrom);
    }
}
