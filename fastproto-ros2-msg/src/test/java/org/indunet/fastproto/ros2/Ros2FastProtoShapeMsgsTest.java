package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.geometry_msgs.msg.Point;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point32;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Polygon;
import org.indunet.fastproto.ros2.shape_msgs.msg.Mesh;
import org.indunet.fastproto.ros2.shape_msgs.msg.MeshTriangle;
import org.indunet.fastproto.ros2.shape_msgs.msg.Plane;
import org.indunet.fastproto.ros2.shape_msgs.msg.SolidPrimitive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Ros2FastProtoShapeMsgsTest {
    @Test
    void testMeshRoundTrip() {
        Mesh mesh = Mesh.builder()
                .triangles(new MeshTriangle[]{
                        MeshTriangle.builder().vertexIndices(new long[]{0, 1, 2}).build(),
                        MeshTriangle.builder().vertexIndices(new long[]{0, 2, 3}).build()
                })
                .vertices(new Point[]{
                        Point.builder().x(0.0).y(0.0).z(0.0).build(),
                        Point.builder().x(1.0).y(0.0).z(0.0).build(),
                        Point.builder().x(1.0).y(1.0).z(0.0).build(),
                        Point.builder().x(0.0).y(1.0).z(0.0).build()
                })
                .build();

        assertEquals(mesh, Mesh.decode(mesh.encode()));
    }

    @Test
    void testPlaneRoundTrip() {
        Plane plane = Plane.builder()
                .coef(new double[]{0.0, 0.0, 1.0, -0.5})
                .build();

        assertEquals(plane, Plane.decode(plane.encode()));
    }

    @Test
    void testSolidPrimitiveRoundTrip() {
        SolidPrimitive solidPrimitive = SolidPrimitive.builder()
                .type(SolidPrimitive.PRISM)
                .dimensions(new double[]{1.5})
                .polygon(Polygon.builder()
                        .points(new Point32[]{
                                Point32.builder().x(0.0f).y(0.0f).z(0.0f).build(),
                                Point32.builder().x(1.0f).y(0.0f).z(0.0f).build(),
                                Point32.builder().x(0.5f).y(1.0f).z(0.0f).build()
                        })
                        .build())
                .build();

        assertEquals(solidPrimitive, SolidPrimitive.decode(solidPrimitive.encode()));
    }
}
