package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Quaternion;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Transform;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TransformStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Vector3;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;
import org.indunet.fastproto.ros2.tf2_msgs.msg.TFMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Ros2FastProtoTf2MsgsTest {
    @Test
    void testTFMessageRoundTrip() {
        TFMessage tfMessage = TFMessage.builder()
                .transforms(new TransformStamped[]{
                        TransformStamped.builder()
                                .header(Header.builder().stamp(Time.builder().sec(121).nanosec(122).build()).frameId("world").build())
                                .childFrameId("base_link")
                                .transform(Transform.builder()
                                        .translation(Vector3.builder().x(1.0).y(2.0).z(3.0).build())
                                        .rotation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build())
                                        .build())
                                .build(),
                        TransformStamped.builder()
                                .header(Header.builder().stamp(Time.builder().sec(123).nanosec(124).build()).frameId("base_link").build())
                                .childFrameId("lidar")
                                .transform(Transform.builder()
                                        .translation(Vector3.builder().x(0.2).y(0.0).z(0.4).build())
                                        .rotation(Quaternion.builder().x(0.0).y(0.0).z(0.1).w(0.99).build())
                                        .build())
                                .build()
                })
                .build();

        assertEquals(tfMessage, Ros2FastProto.decode(Ros2FastProto.encode(tfMessage, Ros2Codecs.TF_MESSAGE), Ros2Codecs.TF_MESSAGE));
    }
}
