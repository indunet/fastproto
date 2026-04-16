package org.indunet.fastproto.ros2.visualization_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * visualization_msgs/msg/MarkerArray
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkerArray {
    private Marker[] markers;

    public void writeTo(Ros2CdrWriter writer) {
                    Ros2CodecSupport.writeMarkerArray(writer, this.getMarkers());
    }

    public static MarkerArray readFrom(Ros2CdrReader reader) {
                    return MarkerArray.builder()
                            .markers(Ros2CodecSupport.readMarkerArray(reader))
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, MarkerArray::writeTo);
    }

    public static MarkerArray decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, MarkerArray::readFrom);
    }
}
