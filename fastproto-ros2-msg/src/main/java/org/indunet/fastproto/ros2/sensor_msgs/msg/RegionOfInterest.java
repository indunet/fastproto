package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sensor_msgs/msg/RegionOfInterest
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionOfInterest {
    private long xOffset;
    private long yOffset;
    private long height;
    private long width;
    private boolean doRectify;

    public void writeTo(Ros2CdrWriter writer) {
                    writer.writeUInt32(this.getXOffset());
                    writer.writeUInt32(this.getYOffset());
                    writer.writeUInt32(this.getHeight());
                    writer.writeUInt32(this.getWidth());
                    writer.writeBool(this.isDoRectify());
    }

    public static RegionOfInterest readFrom(Ros2CdrReader reader) {
                    return RegionOfInterest.builder()
                            .xOffset(reader.readUInt32())
                            .yOffset(reader.readUInt32())
                            .height(reader.readUInt32())
                            .width(reader.readUInt32())
                            .doRectify(reader.readBool())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, RegionOfInterest::writeTo);
    }

    public static RegionOfInterest decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, RegionOfInterest::readFrom);
    }
}
