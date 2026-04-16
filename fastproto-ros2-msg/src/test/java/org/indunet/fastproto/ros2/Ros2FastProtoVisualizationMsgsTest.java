package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Duration;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Pose;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Quaternion;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Vector3;
import org.indunet.fastproto.ros2.sensor_msgs.msg.CompressedImage;
import org.indunet.fastproto.ros2.std_msgs.msg.ColorRGBA;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;
import org.indunet.fastproto.ros2.visualization_msgs.msg.Marker;
import org.indunet.fastproto.ros2.visualization_msgs.msg.MarkerArray;
import org.indunet.fastproto.ros2.visualization_msgs.msg.MeshFile;
import org.indunet.fastproto.ros2.visualization_msgs.msg.UVCoordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Ros2FastProtoVisualizationMsgsTest {
    @Test
    void testMarkerArrayRoundTrip() {
        Marker marker = Marker.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(41).nanosec(42).build())
                        .frameId("map")
                        .build())
                .ns("demo")
                .id(7)
                .type(Marker.MESH_RESOURCE)
                .action(Marker.ADD)
                .pose(Pose.builder()
                        .position(Point.builder().x(1.0).y(2.0).z(3.0).build())
                        .orientation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build())
                        .build())
                .scale(Vector3.builder().x(1.0).y(1.5).z(2.0).build())
                .color(ColorRGBA.builder().r(0.2f).g(0.4f).b(0.6f).a(0.8f).build())
                .lifetime(Duration.builder().sec(5).nanosec(0).build())
                .frameLocked(true)
                .points(new Point[]{
                        Point.builder().x(0.0).y(0.0).z(0.0).build(),
                        Point.builder().x(1.0).y(0.0).z(0.0).build()
                })
                .colors(new ColorRGBA[]{
                        ColorRGBA.builder().r(1.0f).g(0.0f).b(0.0f).a(1.0f).build(),
                        ColorRGBA.builder().r(0.0f).g(1.0f).b(0.0f).a(1.0f).build()
                })
                .textureResource("embedded://checker")
                .texture(CompressedImage.builder()
                        .header(Header.builder()
                                .stamp(Time.builder().sec(43).nanosec(44).build())
                                .frameId("texture")
                                .build())
                        .format("png")
                        .data(new byte[]{1, 2, 3, 4})
                        .build())
                .uvCoordinates(new UVCoordinate[]{
                        UVCoordinate.builder().u(0.0f).v(0.0f).build(),
                        UVCoordinate.builder().u(1.0f).v(1.0f).build()
                })
                .text("mesh marker")
                .meshResource("embedded://robot_mesh")
                .meshFile(MeshFile.builder().filename("robot.dae").data(new byte[]{9, 8, 7, 6}).build())
                .meshUseEmbeddedMaterials(true)
                .build();

        MarkerArray markerArray = MarkerArray.builder()
                .markers(new Marker[]{marker})
                .build();

        assertEquals(markerArray, MarkerArray.decode(markerArray.encode()));
    }
}
