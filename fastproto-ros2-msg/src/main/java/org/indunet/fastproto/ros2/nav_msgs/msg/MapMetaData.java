package org.indunet.fastproto.ros2.nav_msgs.msg;

import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Pose;

/**
 * nav_msgs/msg/MapMetaData
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapMetaData {
    private Time mapLoadTime;
    private float resolution;
    private long width;
    private long height;
    private Pose origin;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getMapLoadTime().writeTo(writer);
                    writer.writeFloat(this.getResolution());
                    writer.writeUInt32(this.getWidth());
                    writer.writeUInt32(this.getHeight());
                    this.getOrigin().writeTo(writer);
    }

    public static MapMetaData readFrom(Ros2CdrReader reader) {
                    return MapMetaData.builder()
                            .mapLoadTime(Time.readFrom(reader))
                            .resolution(reader.readFloat())
                            .width(reader.readUInt32())
                            .height(reader.readUInt32())
                            .origin(Pose.readFrom(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, MapMetaData::writeTo);
    }

    public static MapMetaData decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, MapMetaData::readFrom);
    }
}
