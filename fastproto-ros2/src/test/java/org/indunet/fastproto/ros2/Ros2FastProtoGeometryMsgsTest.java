package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.geometry_msgs.msg.*;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Ros2FastProtoGeometryMsgsTest extends Ros2FastProtoTestSupport {
    @Test
    void testPoseRoundTrip() {
        Pose pose = Pose.builder()
                .position(Point.builder().x(1.5).y(-2.25).z(3.75).build())
                .orientation(Quaternion.builder().x(0.0).y(0.1).z(0.2).w(1.0).build())
                .build();

        byte[] bytes = Ros2FastProto.encode(pose, Ros2Codecs.POSE);
        assertEquals(64, bytes.length);
        assertEquals(pose, Ros2FastProto.decode(bytes, Ros2Codecs.POSE));
    }

    @Test
    void testPose2DRoundTrip() {
        Pose2D pose2D = Pose2D.builder().x(2.5).y(-1.25).theta(0.78539816339).build();
        byte[] bytes = Ros2FastProto.encode(pose2D, Ros2Codecs.POSE2D);

        assertEquals(32, bytes.length);
        assertEquals(pose2D, Ros2FastProto.decode(bytes, Ros2Codecs.POSE2D));
    }

    @Test
    void testTwistRoundTrip() {
        Twist twist = Twist.builder()
                .linear(Vector3.builder().x(1.0).y(2.0).z(3.0).build())
                .angular(Vector3.builder().x(-0.5).y(0.25).z(0.75).build())
                .build();

        byte[] bytes = Ros2FastProto.encode(twist, Ros2Codecs.TWIST);
        assertEquals(56, bytes.length);
        assertEquals(twist, Ros2FastProto.decode(bytes, Ros2Codecs.TWIST));
    }

    @Test
    void testPoseStampedRoundTrip() {
        PoseStamped poseStamped = PoseStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(11).nanosec(12).build()).frameId("map").build())
                .pose(Pose.builder()
                        .position(Point.builder().x(7.0).y(8.0).z(9.0).build())
                        .orientation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build())
                        .build())
                .build();

        assertEquals(poseStamped, Ros2FastProto.decode(Ros2FastProto.encode(poseStamped, Ros2Codecs.POSE_STAMPED), Ros2Codecs.POSE_STAMPED));
    }

    @Test
    void testPolygonStampedRoundTrip() {
        PolygonStamped polygonStamped = PolygonStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(15).nanosec(16).build()).frameId("base_link").build())
                .polygon(Polygon.builder()
                        .points(new Point32[]{
                                Point32.builder().x(0.0f).y(0.0f).z(0.0f).build(),
                                Point32.builder().x(1.0f).y(0.0f).z(0.0f).build(),
                                Point32.builder().x(1.0f).y(1.0f).z(0.0f).build(),
                                Point32.builder().x(0.0f).y(1.0f).z(0.0f).build()
                        })
                        .build())
                .build();

        assertEquals(polygonStamped, Ros2FastProto.decode(Ros2FastProto.encode(polygonStamped, Ros2Codecs.POLYGON_STAMPED), Ros2Codecs.POLYGON_STAMPED));
    }

    @Test
    void testTransformStampedRoundTrip() {
        TransformStamped transformStamped = TransformStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(13).nanosec(14).build()).frameId("world").build())
                .childFrameId("base_link")
                .transform(Transform.builder()
                        .translation(Vector3.builder().x(1.0).y(2.0).z(3.0).build())
                        .rotation(Quaternion.builder().x(0.0).y(0.0).z(0.5).w(0.5).build())
                        .build())
                .build();

        assertEquals(transformStamped, Ros2FastProto.decode(Ros2FastProto.encode(transformStamped, Ros2Codecs.TRANSFORM_STAMPED), Ros2Codecs.TRANSFORM_STAMPED));
    }

    @Test
    void testStampedMessagesRoundTrip() {
        PointStamped pointStamped = PointStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(31).nanosec(32).build()).frameId("map").build())
                .point(Point.builder().x(1.0).y(2.0).z(3.0).build())
                .build();
        Vector3Stamped vector3Stamped = Vector3Stamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(33).nanosec(34).build()).frameId("base_link").build())
                .vector(Vector3.builder().x(4.0).y(5.0).z(6.0).build())
                .build();
        QuaternionStamped quaternionStamped = QuaternionStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(35).nanosec(36).build()).frameId("imu").build())
                .quaternion(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build())
                .build();

        assertEquals(pointStamped, Ros2FastProto.decode(Ros2FastProto.encode(pointStamped, Ros2Codecs.POINT_STAMPED), Ros2Codecs.POINT_STAMPED));
        assertEquals(vector3Stamped, Ros2FastProto.decode(Ros2FastProto.encode(vector3Stamped, Ros2Codecs.VECTOR3_STAMPED), Ros2Codecs.VECTOR3_STAMPED));
        assertEquals(quaternionStamped, Ros2FastProto.decode(Ros2FastProto.encode(quaternionStamped, Ros2Codecs.QUATERNION_STAMPED), Ros2Codecs.QUATERNION_STAMPED));
    }

    @Test
    void testAccelStampedRoundTrip() {
        AccelStamped accelStamped = AccelStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(41).nanosec(42).build()).frameId("base_link").build())
                .accel(Accel.builder()
                        .linear(Vector3.builder().x(0.1).y(0.2).z(0.3).build())
                        .angular(Vector3.builder().x(0.4).y(0.5).z(0.6).build())
                        .build())
                .build();

        assertEquals(accelStamped, Ros2FastProto.decode(Ros2FastProto.encode(accelStamped, Ros2Codecs.ACCEL_STAMPED), Ros2Codecs.ACCEL_STAMPED));
    }

    @Test
    void testAccelWithCovarianceStampedRoundTrip() {
        AccelWithCovarianceStamped accelStamped = AccelWithCovarianceStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(49).nanosec(50).build()).frameId("imu_link").build())
                .accel(AccelWithCovariance.builder()
                        .accel(Accel.builder()
                                .linear(Vector3.builder().x(0.3).y(0.2).z(0.1).build())
                                .angular(Vector3.builder().x(0.01).y(0.02).z(0.03).build())
                                .build())
                        .covariance(fixedCovariance(0.005))
                        .build())
                .build();

        assertEquals(accelStamped, Ros2FastProto.decode(Ros2FastProto.encode(accelStamped, Ros2Codecs.ACCEL_WITH_COVARIANCE_STAMPED), Ros2Codecs.ACCEL_WITH_COVARIANCE_STAMPED));
    }

    @Test
    void testTwistStampedRoundTrip() {
        TwistStamped twistStamped = TwistStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(45).nanosec(46).build()).frameId("base_link").build())
                .twist(Twist.builder()
                        .linear(Vector3.builder().x(1.2).y(0.0).z(0.0).build())
                        .angular(Vector3.builder().x(0.0).y(0.0).z(0.35).build())
                        .build())
                .build();

        assertEquals(twistStamped, Ros2FastProto.decode(Ros2FastProto.encode(twistStamped, Ros2Codecs.TWIST_STAMPED), Ros2Codecs.TWIST_STAMPED));
    }

    @Test
    void testTwistWithCovarianceStampedRoundTrip() {
        TwistWithCovarianceStamped twistStamped = TwistWithCovarianceStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(47).nanosec(48).build()).frameId("base_link").build())
                .twist(TwistWithCovariance.builder()
                        .twist(Twist.builder()
                                .linear(Vector3.builder().x(0.8).y(0.0).z(0.0).build())
                                .angular(Vector3.builder().x(0.0).y(0.0).z(0.12).build())
                                .build())
                        .covariance(fixedCovariance(0.004))
                        .build())
                .build();

        assertEquals(twistStamped, Ros2FastProto.decode(Ros2FastProto.encode(twistStamped, Ros2Codecs.TWIST_WITH_COVARIANCE_STAMPED), Ros2Codecs.TWIST_WITH_COVARIANCE_STAMPED));
    }

    @Test
    void testInertiaStampedRoundTrip() {
        InertiaStamped inertiaStamped = InertiaStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(43).nanosec(44).build()).frameId("center_of_mass").build())
                .inertia(Inertia.builder()
                        .m(12.5).com(Vector3.builder().x(0.1).y(-0.2).z(0.3).build())
                        .ixx(1.1).ixy(0.01).ixz(0.02).iyy(1.2).iyz(0.03).izz(1.3)
                        .build())
                .build();

        assertEquals(inertiaStamped, Ros2FastProto.decode(Ros2FastProto.encode(inertiaStamped, Ros2Codecs.INERTIA_STAMPED), Ros2Codecs.INERTIA_STAMPED));
    }

    @Test
    void testPoseWithCovarianceStampedRoundTrip() {
        PoseWithCovarianceStamped poseStamped = PoseWithCovarianceStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(61).nanosec(62).build()).frameId("map").build())
                .pose(PoseWithCovariance.builder()
                        .pose(Pose.builder()
                                .position(Point.builder().x(3.0).y(4.0).z(5.0).build())
                                .orientation(Quaternion.builder().x(0.0).y(0.0).z(0.2).w(0.98).build())
                                .build())
                        .covariance(fixedCovariance(0.003))
                        .build())
                .build();

        assertEquals(poseStamped, Ros2FastProto.decode(Ros2FastProto.encode(poseStamped, Ros2Codecs.POSE_WITH_COVARIANCE_STAMPED), Ros2Codecs.POSE_WITH_COVARIANCE_STAMPED));
    }

    @Test
    void testWrenchStampedRoundTrip() {
        WrenchStamped wrenchStamped = WrenchStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(81).nanosec(82).build()).frameId("tool0").build())
                .wrench(Wrench.builder()
                        .force(Vector3.builder().x(1.0).y(2.0).z(3.0).build())
                        .torque(Vector3.builder().x(0.1).y(0.2).z(0.3).build())
                        .build())
                .build();

        assertEquals(wrenchStamped, Ros2FastProto.decode(Ros2FastProto.encode(wrenchStamped, Ros2Codecs.WRENCH_STAMPED), Ros2Codecs.WRENCH_STAMPED));
    }

    @Test
    void testPoseArrayRoundTrip() {
        Pose[] poses = new Pose[]{
                Pose.builder().position(Point.builder().x(1.0).y(1.0).z(0.0).build()).orientation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build()).build(),
                Pose.builder().position(Point.builder().x(2.0).y(2.0).z(0.0).build()).orientation(Quaternion.builder().x(0.0).y(0.0).z(0.1).w(0.99).build()).build()
        };
        PoseArray poseArray = PoseArray.builder()
                .header(Header.builder().stamp(Time.builder().sec(91).nanosec(92).build()).frameId("map").build())
                .poses(poses)
                .build();

        assertEquals(poseArray, Ros2FastProto.decode(Ros2FastProto.encode(poseArray, Ros2Codecs.POSE_ARRAY), Ros2Codecs.POSE_ARRAY));
    }
}
