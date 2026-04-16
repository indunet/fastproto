package org.indunet.fastproto.ros2.sensor_msgs.msg;

import org.indunet.fastproto.ros2.codecs.Ros2CodecSupport;
import org.indunet.fastproto.ros2.internal.Ros2MessageSupport;
import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;

/**
 * sensor_msgs/msg/JointState
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JointState {
    private Header header;
    private String[] name;
    private double[] position;
    private double[] velocity;
    private double[] effort;

    public void writeTo(Ros2CdrWriter writer) {
                    this.getHeader().writeTo(writer);
                    writer.writeStringSequence(Ros2CodecSupport.safeStringArray(this.getName()));
                    writer.writeDoubleSequence(Ros2CodecSupport.safeDoubleArray(this.getPosition()));
                    writer.writeDoubleSequence(Ros2CodecSupport.safeDoubleArray(this.getVelocity()));
                    writer.writeDoubleSequence(Ros2CodecSupport.safeDoubleArray(this.getEffort()));
    }

    public static JointState readFrom(Ros2CdrReader reader) {
                    return JointState.builder()
                            .header(Header.readFrom(reader))
                            .name(reader.readStringSequence())
                            .position(reader.readDoubleSequence())
                            .velocity(reader.readDoubleSequence())
                            .effort(reader.readDoubleSequence())
                            .build();
    }

    public byte[] encode() {
        return Ros2MessageSupport.encode(this, JointState::writeTo);
    }

    public static JointState decode(byte[] bytes) {
        return Ros2MessageSupport.decode(bytes, JointState::readFrom);
    }
}
