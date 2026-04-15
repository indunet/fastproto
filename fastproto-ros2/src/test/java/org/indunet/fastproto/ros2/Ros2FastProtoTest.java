package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Duration;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Accel;
import org.indunet.fastproto.ros2.geometry_msgs.msg.AccelStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Inertia;
import org.indunet.fastproto.ros2.geometry_msgs.msg.InertiaStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point32;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PointStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Pose;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Pose2D;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseArray;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseWithCovariance;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseWithCovarianceStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Polygon;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PolygonStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Quaternion;
import org.indunet.fastproto.ros2.geometry_msgs.msg.QuaternionStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Transform;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TransformStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Twist;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TwistStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TwistWithCovariance;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Vector3;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Vector3Stamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Wrench;
import org.indunet.fastproto.ros2.geometry_msgs.msg.WrenchStamped;
import org.indunet.fastproto.ros2.nav_msgs.msg.MapMetaData;
import org.indunet.fastproto.ros2.nav_msgs.msg.Odometry;
import org.indunet.fastproto.ros2.nav_msgs.msg.OccupancyGrid;
import org.indunet.fastproto.ros2.nav_msgs.msg.Path;
import org.indunet.fastproto.ros2.sensor_msgs.msg.BatteryState;
import org.indunet.fastproto.ros2.sensor_msgs.msg.CameraInfo;
import org.indunet.fastproto.ros2.sensor_msgs.msg.ChannelFloat32;
import org.indunet.fastproto.ros2.sensor_msgs.msg.CompressedImage;
import org.indunet.fastproto.ros2.sensor_msgs.msg.FluidPressure;
import org.indunet.fastproto.ros2.sensor_msgs.msg.Image;
import org.indunet.fastproto.ros2.sensor_msgs.msg.Illuminance;
import org.indunet.fastproto.ros2.sensor_msgs.msg.Imu;
import org.indunet.fastproto.ros2.sensor_msgs.msg.JointState;
import org.indunet.fastproto.ros2.sensor_msgs.msg.Joy;
import org.indunet.fastproto.ros2.sensor_msgs.msg.LaserScan;
import org.indunet.fastproto.ros2.sensor_msgs.msg.MagneticField;
import org.indunet.fastproto.ros2.sensor_msgs.msg.NavSatFix;
import org.indunet.fastproto.ros2.sensor_msgs.msg.NavSatStatus;
import org.indunet.fastproto.ros2.sensor_msgs.msg.PointCloud;
import org.indunet.fastproto.ros2.sensor_msgs.msg.PointCloud2;
import org.indunet.fastproto.ros2.sensor_msgs.msg.PointField;
import org.indunet.fastproto.ros2.sensor_msgs.msg.Range;
import org.indunet.fastproto.ros2.sensor_msgs.msg.RegionOfInterest;
import org.indunet.fastproto.ros2.sensor_msgs.msg.RelativeHumidity;
import org.indunet.fastproto.ros2.sensor_msgs.msg.Temperature;
import org.indunet.fastproto.ros2.std_msgs.msg.Bool;
import org.indunet.fastproto.ros2.std_msgs.msg.Byte;
import org.indunet.fastproto.ros2.std_msgs.msg.ColorRGBA;
import org.indunet.fastproto.ros2.std_msgs.msg.Float32;
import org.indunet.fastproto.ros2.std_msgs.msg.Float32MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.Float64;
import org.indunet.fastproto.ros2.std_msgs.msg.Float64MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;
import org.indunet.fastproto.ros2.std_msgs.msg.Int32;
import org.indunet.fastproto.ros2.std_msgs.msg.Int32MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.MultiArrayDimension;
import org.indunet.fastproto.ros2.std_msgs.msg.MultiArrayLayout;
import org.indunet.fastproto.ros2.std_msgs.msg.UInt32;
import org.indunet.fastproto.ros2.std_msgs.msg.UInt8;
import org.indunet.fastproto.ros2.std_msgs.msg.UInt8MultiArray;
import org.indunet.fastproto.ros2.tf2_msgs.msg.TFMessage;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ros2FastProtoTest {
    @Test
    public void testTimeExactBytes() {
        Time time = Time.builder()
                .sec(12)
                .nanosec(345)
                .build();

        byte[] bytes = Ros2FastProto.encode(time, Ros2Codecs.TIME);

        assertArrayEquals(new byte[]{
                0x00, 0x01, 0x00, 0x00,
                0x0c, 0x00, 0x00, 0x00,
                0x59, 0x01, 0x00, 0x00
        }, bytes);
        assertEquals(time, Ros2FastProto.decode(bytes, Ros2Codecs.TIME));
    }

    @Test
    public void testDurationExactBytes() {
        Duration duration = Duration.builder()
                .sec(-2)
                .nanosec(300_000_000L)
                .build();

        byte[] bytes = Ros2FastProto.encode(duration, Ros2Codecs.DURATION);

        assertArrayEquals(new byte[]{
                0x00, 0x01, 0x00, 0x00,
                (byte) 0xfe, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                0x00, (byte) 0xa3, (byte) 0xe1, 0x11
        }, bytes);
        assertEquals(duration, Ros2FastProto.decode(bytes, Ros2Codecs.DURATION));
    }

    @Test
    public void testHeaderExactBytes() {
        Header header = Header.builder()
                .stamp(Time.builder().sec(42).nanosec(123).build())
                .frameId("map")
                .build();

        byte[] bytes = Ros2FastProto.encode(header, Ros2Codecs.HEADER);

        assertArrayEquals(new byte[]{
                0x00, 0x01, 0x00, 0x00,
                0x2a, 0x00, 0x00, 0x00,
                0x7b, 0x00, 0x00, 0x00,
                0x04, 0x00, 0x00, 0x00,
                0x6d, 0x61, 0x70, 0x00
        }, bytes);
        assertEquals(header, Ros2FastProto.decode(bytes, Ros2Codecs.HEADER));
    }

    @Test
    public void testStdScalarMessagesRoundTrip() {
        Bool boolValue = Bool.builder().data(true).build();
        Byte byteValue = Byte.builder().data(-12).build();
        Int32 int32Value = Int32.builder().data(-123456).build();
        UInt8 uint8Value = UInt8.builder().data(250).build();
        UInt32 uint32Value = UInt32.builder().data(4_000_000_000L).build();
        Float32 float32Value = Float32.builder().data(12.5f).build();
        Float64 float64Value = Float64.builder().data(-98.7654321).build();
        org.indunet.fastproto.ros2.std_msgs.msg.String stringValue =
                org.indunet.fastproto.ros2.std_msgs.msg.String.builder().data("hello ros2").build();

        assertEquals(boolValue, Ros2FastProto.decode(Ros2FastProto.encode(boolValue, Ros2Codecs.BOOL), Ros2Codecs.BOOL));
        assertEquals(byteValue, Ros2FastProto.decode(Ros2FastProto.encode(byteValue, Ros2Codecs.BYTE), Ros2Codecs.BYTE));
        assertEquals(int32Value, Ros2FastProto.decode(Ros2FastProto.encode(int32Value, Ros2Codecs.INT32), Ros2Codecs.INT32));
        assertEquals(uint8Value, Ros2FastProto.decode(Ros2FastProto.encode(uint8Value, Ros2Codecs.UINT8), Ros2Codecs.UINT8));
        assertEquals(uint32Value, Ros2FastProto.decode(Ros2FastProto.encode(uint32Value, Ros2Codecs.UINT32), Ros2Codecs.UINT32));
        assertEquals(float32Value, Ros2FastProto.decode(Ros2FastProto.encode(float32Value, Ros2Codecs.FLOAT32), Ros2Codecs.FLOAT32));
        assertEquals(float64Value, Ros2FastProto.decode(Ros2FastProto.encode(float64Value, Ros2Codecs.FLOAT64), Ros2Codecs.FLOAT64));
        assertEquals(stringValue, Ros2FastProto.decode(Ros2FastProto.encode(stringValue, Ros2Codecs.STD_STRING), Ros2Codecs.STD_STRING));
    }

    @Test
    public void testPoseRoundTrip() {
        Pose pose = Pose.builder()
                .position(Point.builder()
                        .x(1.5)
                        .y(-2.25)
                        .z(3.75)
                        .build())
                .orientation(Quaternion.builder()
                        .x(0.0)
                        .y(0.1)
                        .z(0.2)
                        .w(1.0)
                        .build())
                .build();

        byte[] bytes = Ros2FastProto.encode(pose, Ros2Codecs.POSE);

        assertEquals(64, bytes.length);
        assertEquals(pose, Ros2FastProto.decode(bytes, Ros2Codecs.POSE));
    }

    @Test
    public void testPose2DRoundTrip() {
        Pose2D pose2D = Pose2D.builder()
                .x(2.5)
                .y(-1.25)
                .theta(0.78539816339)
                .build();

        byte[] bytes = Ros2FastProto.encode(pose2D, Ros2Codecs.POSE2D);

        assertEquals(32, bytes.length);
        assertEquals(pose2D, Ros2FastProto.decode(bytes, Ros2Codecs.POSE2D));
    }

    @Test
    public void testColorRgbARoundTrip() {
        ColorRGBA color = ColorRGBA.builder()
                .r(0.2f)
                .g(0.4f)
                .b(0.6f)
                .a(0.8f)
                .build();

        byte[] bytes = Ros2FastProto.encode(color, Ros2Codecs.COLOR_RGBA);

        assertEquals(20, bytes.length);
        assertEquals(color, Ros2FastProto.decode(bytes, Ros2Codecs.COLOR_RGBA));
    }

    @Test
    public void testMultiArrayRoundTrip() {
        MultiArrayLayout layout = MultiArrayLayout.builder()
                .dim(new MultiArrayDimension[]{
                        MultiArrayDimension.builder().label("rows").size(2).stride(6).build(),
                        MultiArrayDimension.builder().label("cols").size(3).stride(3).build()
                })
                .dataOffset(0)
                .build();
        Float32MultiArray float32MultiArray = Float32MultiArray.builder()
                .layout(layout)
                .data(new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f})
                .build();
        Float64MultiArray float64MultiArray = Float64MultiArray.builder()
                .layout(layout)
                .data(new double[]{0.1, 0.2, 0.3, 0.4})
                .build();
        Int32MultiArray int32MultiArray = Int32MultiArray.builder()
                .layout(layout)
                .data(new int[]{10, 20, 30, 40, 50, 60})
                .build();
        UInt8MultiArray uint8MultiArray = UInt8MultiArray.builder()
                .layout(layout)
                .data(new byte[]{1, 2, 3, 4, 5, 6})
                .build();

        assertEquals(float32MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(float32MultiArray, Ros2Codecs.FLOAT32_MULTI_ARRAY), Ros2Codecs.FLOAT32_MULTI_ARRAY));
        assertEquals(float64MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(float64MultiArray, Ros2Codecs.FLOAT64_MULTI_ARRAY), Ros2Codecs.FLOAT64_MULTI_ARRAY));
        assertEquals(int32MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(int32MultiArray, Ros2Codecs.INT32_MULTI_ARRAY), Ros2Codecs.INT32_MULTI_ARRAY));
        assertEquals(uint8MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(uint8MultiArray, Ros2Codecs.UINT8_MULTI_ARRAY), Ros2Codecs.UINT8_MULTI_ARRAY));
    }

    @Test
    public void testTwistRoundTrip() {
        Twist twist = Twist.builder()
                .linear(Vector3.builder()
                        .x(1.0)
                        .y(2.0)
                        .z(3.0)
                        .build())
                .angular(Vector3.builder()
                        .x(-0.5)
                        .y(0.25)
                        .z(0.75)
                        .build())
                .build();

        byte[] bytes = Ros2FastProto.encode(twist, Ros2Codecs.TWIST);

        assertEquals(56, bytes.length);
        assertEquals(twist, Ros2FastProto.decode(bytes, Ros2Codecs.TWIST));
    }

    @Test
    public void testHeaderStringAlignment() {
        Header header = Header.builder()
                .stamp(Time.builder().sec(1).nanosec(2).build())
                .frameId("odom")
                .build();

        byte[] bytes = Ros2FastProto.encode(header, Ros2Codecs.HEADER);
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        assertEquals(1, buffer.getInt(4));
        assertEquals(2L, Integer.toUnsignedLong(buffer.getInt(8)));
        assertEquals(5L, Integer.toUnsignedLong(buffer.getInt(12)));
        assertEquals('o', bytes[16]);
        assertEquals(0, bytes[20]);
        assertEquals(header, Ros2FastProto.decode(bytes, Ros2Codecs.HEADER));
    }

    @Test
    public void testImuRoundTrip() {
        Imu imu = Imu.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(9).nanosec(10).build())
                        .frameId("base_link")
                        .build())
                .orientation(Quaternion.builder().x(0.0).y(0.1).z(0.2).w(1.0).build())
                .orientationCovariance(covariance(0.01))
                .angularVelocity(Vector3.builder().x(1.0).y(2.0).z(3.0).build())
                .angularVelocityCovariance(covariance(0.02))
                .linearAcceleration(Vector3.builder().x(4.0).y(5.0).z(6.0).build())
                .linearAccelerationCovariance(covariance(0.03))
                .build();

        byte[] bytes = Ros2FastProto.encode(imu, Ros2Codecs.IMU);

        assertEquals(328, bytes.length);
        assertEquals(imu, Ros2FastProto.decode(bytes, Ros2Codecs.IMU));
    }

    @Test
    public void testImageRoundTrip() {
        Image image = Image.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(3).nanosec(4).build())
                        .frameId("camera")
                        .build())
                .height(480)
                .width(640)
                .encoding("rgb8")
                .isBigendian(0)
                .step(1920)
                .data(new byte[]{1, 2, 3, 4, 5, 6})
                .build();

        byte[] bytes = Ros2FastProto.encode(image, Ros2Codecs.IMAGE);

        assertEquals(image, Ros2FastProto.decode(bytes, Ros2Codecs.IMAGE));
    }

    @Test
    public void testCompressedImageRoundTrip() {
        CompressedImage image = CompressedImage.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(5).nanosec(6).build())
                        .frameId("camera_compressed")
                        .build())
                .format("jpeg")
                .data(new byte[]{9, 8, 7, 6, 5, 4, 3})
                .build();

        byte[] bytes = Ros2FastProto.encode(image, Ros2Codecs.COMPRESSED_IMAGE);

        assertEquals(image, Ros2FastProto.decode(bytes, Ros2Codecs.COMPRESSED_IMAGE));
    }

    @Test
    public void testCameraInfoRoundTrip() {
        CameraInfo cameraInfo = CameraInfo.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(7).nanosec(8).build())
                        .frameId("camera_optical")
                        .build())
                .height(1080)
                .width(1920)
                .distortionModel("plumb_bob")
                .d(new double[]{0.1, 0.2, 0.3, 0.4, 0.5})
                .k(new double[]{1, 0, 960, 0, 1, 540, 0, 0, 1})
                .r(new double[]{1, 0, 0, 0, 1, 0, 0, 0, 1})
                .p(new double[]{1, 0, 960, 0, 0, 1, 540, 0, 0, 0, 1, 0})
                .binningX(2)
                .binningY(2)
                .roi(RegionOfInterest.builder()
                        .xOffset(10)
                        .yOffset(20)
                        .height(720)
                        .width(1280)
                        .doRectify(true)
                        .build())
                .build();

        byte[] bytes = Ros2FastProto.encode(cameraInfo, Ros2Codecs.CAMERA_INFO);

        assertEquals(cameraInfo, Ros2FastProto.decode(bytes, Ros2Codecs.CAMERA_INFO));
    }

    @Test
    public void testMagneticFieldRoundTrip() {
        MagneticField magneticField = MagneticField.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(17).nanosec(18).build())
                        .frameId("imu_link")
                        .build())
                .magneticField(Vector3.builder().x(0.01).y(-0.02).z(0.03).build())
                .magneticFieldCovariance(covariance(0.0001))
                .build();

        byte[] bytes = Ros2FastProto.encode(magneticField, Ros2Codecs.MAGNETIC_FIELD);

        assertEquals(magneticField, Ros2FastProto.decode(bytes, Ros2Codecs.MAGNETIC_FIELD));
    }

    @Test
    public void testTemperatureAndFluidPressureRoundTrip() {
        Temperature temperature = Temperature.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(19).nanosec(20).build())
                        .frameId("temp_sensor")
                        .build())
                .temperature(36.75)
                .variance(0.15)
                .build();
        FluidPressure fluidPressure = FluidPressure.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(21).nanosec(22).build())
                        .frameId("barometer")
                        .build())
                .fluidPressure(101325.0)
                .variance(12.5)
                .build();

        assertEquals(temperature, Ros2FastProto.decode(Ros2FastProto.encode(temperature, Ros2Codecs.TEMPERATURE), Ros2Codecs.TEMPERATURE));
        assertEquals(fluidPressure, Ros2FastProto.decode(Ros2FastProto.encode(fluidPressure, Ros2Codecs.FLUID_PRESSURE), Ros2Codecs.FLUID_PRESSURE));
    }

    @Test
    public void testPoseStampedRoundTrip() {
        PoseStamped poseStamped = PoseStamped.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(11).nanosec(12).build())
                        .frameId("map")
                        .build())
                .pose(Pose.builder()
                        .position(Point.builder().x(7.0).y(8.0).z(9.0).build())
                        .orientation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build())
                        .build())
                .build();

        byte[] bytes = Ros2FastProto.encode(poseStamped, Ros2Codecs.POSE_STAMPED);

        assertEquals(poseStamped, Ros2FastProto.decode(bytes, Ros2Codecs.POSE_STAMPED));
    }

    @Test
    public void testPolygonStampedRoundTrip() {
        PolygonStamped polygonStamped = PolygonStamped.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(15).nanosec(16).build())
                        .frameId("base_link")
                        .build())
                .polygon(Polygon.builder()
                        .points(new Point32[]{
                                Point32.builder().x(0.0f).y(0.0f).z(0.0f).build(),
                                Point32.builder().x(1.0f).y(0.0f).z(0.0f).build(),
                                Point32.builder().x(1.0f).y(1.0f).z(0.0f).build(),
                                Point32.builder().x(0.0f).y(1.0f).z(0.0f).build()
                        })
                        .build())
                .build();

        byte[] bytes = Ros2FastProto.encode(polygonStamped, Ros2Codecs.POLYGON_STAMPED);

        assertEquals(polygonStamped, Ros2FastProto.decode(bytes, Ros2Codecs.POLYGON_STAMPED));
    }

    @Test
    public void testTransformStampedRoundTrip() {
        TransformStamped transformStamped = TransformStamped.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(13).nanosec(14).build())
                        .frameId("world")
                        .build())
                .childFrameId("base_link")
                .transform(Transform.builder()
                        .translation(Vector3.builder().x(1.0).y(2.0).z(3.0).build())
                        .rotation(Quaternion.builder().x(0.0).y(0.0).z(0.5).w(0.5).build())
                        .build())
                .build();

        byte[] bytes = Ros2FastProto.encode(transformStamped, Ros2Codecs.TRANSFORM_STAMPED);

        assertEquals(transformStamped, Ros2FastProto.decode(bytes, Ros2Codecs.TRANSFORM_STAMPED));
    }

    @Test
    public void testPointCloud2RoundTrip() {
        PointCloud2 pointCloud2 = PointCloud2.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(21).nanosec(22).build())
                        .frameId("lidar")
                        .build())
                .height(1)
                .width(2)
                .fields(new PointField[]{
                        PointField.builder().name("x").offset(0).datatype(PointField.FLOAT32).count(1).build(),
                        PointField.builder().name("y").offset(4).datatype(PointField.FLOAT32).count(1).build(),
                        PointField.builder().name("z").offset(8).datatype(PointField.FLOAT32).count(1).build(),
                        PointField.builder().name("intensity").offset(12).datatype(PointField.FLOAT32).count(1).build()
                })
                .isBigendian(false)
                .pointStep(16)
                .rowStep(32)
                .data(new byte[]{
                        0, 0, 0, 63, 0, 0, 0, 64, 0, 0, 64, 64, 0, 0, -128, 64,
                        0, 0, -96, 64, 0, 0, -64, 64, 0, 0, -32, 64, 0, 0, 0, 65
                })
                .isDense(true)
                .build();

        byte[] bytes = Ros2FastProto.encode(pointCloud2, Ros2Codecs.POINT_CLOUD2);

        assertEquals(pointCloud2, Ros2FastProto.decode(bytes, Ros2Codecs.POINT_CLOUD2));
    }

    @Test
    public void testPointCloudRoundTrip() {
        PointCloud pointCloud = PointCloud.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(23).nanosec(24).build())
                        .frameId("laser_frame")
                        .build())
                .points(new Point32[]{
                        Point32.builder().x(1.0f).y(2.0f).z(0.0f).build(),
                        Point32.builder().x(3.0f).y(4.0f).z(0.5f).build()
                })
                .channels(new ChannelFloat32[]{
                        ChannelFloat32.builder().name("intensity").values(new float[]{10.0f, 20.0f}).build(),
                        ChannelFloat32.builder().name("distance").values(new float[]{2.2f, 5.1f}).build()
                })
                .build();

        byte[] bytes = Ros2FastProto.encode(pointCloud, Ros2Codecs.POINT_CLOUD);

        assertEquals(pointCloud, Ros2FastProto.decode(bytes, Ros2Codecs.POINT_CLOUD));
    }

    @Test
    public void testStampedMessagesRoundTrip() {
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
    public void testAccelStampedRoundTrip() {
        AccelStamped accelStamped = AccelStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(41).nanosec(42).build()).frameId("base_link").build())
                .accel(Accel.builder()
                        .linear(Vector3.builder().x(0.1).y(0.2).z(0.3).build())
                        .angular(Vector3.builder().x(0.4).y(0.5).z(0.6).build())
                        .build())
                .build();

        byte[] bytes = Ros2FastProto.encode(accelStamped, Ros2Codecs.ACCEL_STAMPED);
        assertEquals(accelStamped, Ros2FastProto.decode(bytes, Ros2Codecs.ACCEL_STAMPED));
    }

    @Test
    public void testTwistStampedRoundTrip() {
        TwistStamped twistStamped = TwistStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(45).nanosec(46).build()).frameId("base_link").build())
                .twist(Twist.builder()
                        .linear(Vector3.builder().x(1.2).y(0.0).z(0.0).build())
                        .angular(Vector3.builder().x(0.0).y(0.0).z(0.35).build())
                        .build())
                .build();

        byte[] bytes = Ros2FastProto.encode(twistStamped, Ros2Codecs.TWIST_STAMPED);
        assertEquals(twistStamped, Ros2FastProto.decode(bytes, Ros2Codecs.TWIST_STAMPED));
    }

    @Test
    public void testInertiaStampedRoundTrip() {
        InertiaStamped inertiaStamped = InertiaStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(43).nanosec(44).build()).frameId("center_of_mass").build())
                .inertia(Inertia.builder()
                        .m(12.5)
                        .com(Vector3.builder().x(0.1).y(-0.2).z(0.3).build())
                        .ixx(1.1)
                        .ixy(0.01)
                        .ixz(0.02)
                        .iyy(1.2)
                        .iyz(0.03)
                        .izz(1.3)
                        .build())
                .build();

        byte[] bytes = Ros2FastProto.encode(inertiaStamped, Ros2Codecs.INERTIA_STAMPED);
        assertEquals(inertiaStamped, Ros2FastProto.decode(bytes, Ros2Codecs.INERTIA_STAMPED));
    }

    @Test
    public void testOdometryRoundTrip() {
        Odometry odometry = Odometry.builder()
                .header(Header.builder().stamp(Time.builder().sec(51).nanosec(52).build()).frameId("odom").build())
                .childFrameId("base_link")
                .pose(PoseWithCovariance.builder()
                        .pose(Pose.builder()
                                .position(Point.builder().x(1.0).y(2.0).z(0.0).build())
                                .orientation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build())
                                .build())
                        .covariance(fixedCovariance(0.001))
                        .build())
                .twist(TwistWithCovariance.builder()
                        .twist(Twist.builder()
                                .linear(Vector3.builder().x(0.5).y(0.0).z(0.0).build())
                                .angular(Vector3.builder().x(0.0).y(0.0).z(0.1).build())
                                .build())
                        .covariance(fixedCovariance(0.002))
                        .build())
                .build();

        byte[] bytes = Ros2FastProto.encode(odometry, Ros2Codecs.ODOMETRY);
        assertEquals(odometry, Ros2FastProto.decode(bytes, Ros2Codecs.ODOMETRY));
    }

    @Test
    public void testPoseWithCovarianceStampedRoundTrip() {
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

        byte[] bytes = Ros2FastProto.encode(poseStamped, Ros2Codecs.POSE_WITH_COVARIANCE_STAMPED);
        assertEquals(poseStamped, Ros2FastProto.decode(bytes, Ros2Codecs.POSE_WITH_COVARIANCE_STAMPED));
    }

    @Test
    public void testLaserScanRoundTrip() {
        LaserScan laserScan = LaserScan.builder()
                .header(Header.builder().stamp(Time.builder().sec(71).nanosec(72).build()).frameId("laser").build())
                .angleMin(-1.57f)
                .angleMax(1.57f)
                .angleIncrement(0.01f)
                .timeIncrement(0.001f)
                .scanTime(0.1f)
                .rangeMin(0.2f)
                .rangeMax(30.0f)
                .ranges(new float[]{1.0f, 1.5f, 2.0f, 2.5f})
                .intensities(new float[]{10.0f, 20.0f, 30.0f, 40.0f})
                .build();

        byte[] bytes = Ros2FastProto.encode(laserScan, Ros2Codecs.LASER_SCAN);
        assertEquals(laserScan, Ros2FastProto.decode(bytes, Ros2Codecs.LASER_SCAN));
    }

    @Test
    public void testRangeRoundTrip() {
        Range range = Range.builder()
                .header(Header.builder().stamp(Time.builder().sec(73).nanosec(74).build()).frameId("ultrasonic").build())
                .radiationType(Range.ULTRASOUND)
                .fieldOfView(0.52f)
                .minRange(0.02f)
                .maxRange(4.0f)
                .range(1.35f)
                .build();

        byte[] bytes = Ros2FastProto.encode(range, Ros2Codecs.RANGE);
        assertEquals(range, Ros2FastProto.decode(bytes, Ros2Codecs.RANGE));
    }

    @Test
    public void testWrenchStampedRoundTrip() {
        WrenchStamped wrenchStamped = WrenchStamped.builder()
                .header(Header.builder().stamp(Time.builder().sec(81).nanosec(82).build()).frameId("tool0").build())
                .wrench(Wrench.builder()
                        .force(Vector3.builder().x(1.0).y(2.0).z(3.0).build())
                        .torque(Vector3.builder().x(0.1).y(0.2).z(0.3).build())
                        .build())
                .build();

        byte[] bytes = Ros2FastProto.encode(wrenchStamped, Ros2Codecs.WRENCH_STAMPED);
        assertEquals(wrenchStamped, Ros2FastProto.decode(bytes, Ros2Codecs.WRENCH_STAMPED));
    }

    @Test
    public void testPoseArrayAndPathRoundTrip() {
        Pose[] poses = new Pose[]{
                Pose.builder().position(Point.builder().x(1.0).y(1.0).z(0.0).build()).orientation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build()).build(),
                Pose.builder().position(Point.builder().x(2.0).y(2.0).z(0.0).build()).orientation(Quaternion.builder().x(0.0).y(0.0).z(0.1).w(0.99).build()).build()
        };
        PoseArray poseArray = PoseArray.builder()
                .header(Header.builder().stamp(Time.builder().sec(91).nanosec(92).build()).frameId("map").build())
                .poses(poses)
                .build();
        Path path = Path.builder()
                .header(Header.builder().stamp(Time.builder().sec(93).nanosec(94).build()).frameId("map").build())
                .poses(new PoseStamped[]{
                        PoseStamped.builder().header(Header.builder().stamp(Time.builder().sec(95).nanosec(96).build()).frameId("map").build()).pose(poses[0]).build(),
                        PoseStamped.builder().header(Header.builder().stamp(Time.builder().sec(97).nanosec(98).build()).frameId("map").build()).pose(poses[1]).build()
                })
                .build();

        assertEquals(poseArray, Ros2FastProto.decode(Ros2FastProto.encode(poseArray, Ros2Codecs.POSE_ARRAY), Ros2Codecs.POSE_ARRAY));
        assertEquals(path, Ros2FastProto.decode(Ros2FastProto.encode(path, Ros2Codecs.PATH), Ros2Codecs.PATH));
    }

    @Test
    public void testMapMetaDataAndOccupancyGridRoundTrip() {
        MapMetaData mapMetaData = MapMetaData.builder()
                .mapLoadTime(Time.builder().sec(99).nanosec(100).build())
                .resolution(0.05f)
                .width(128)
                .height(64)
                .origin(Pose.builder()
                        .position(Point.builder().x(-10.0).y(-5.0).z(0.0).build())
                        .orientation(Quaternion.builder().x(0.0).y(0.0).z(0.0).w(1.0).build())
                        .build())
                .build();
        OccupancyGrid occupancyGrid = OccupancyGrid.builder()
                .header(Header.builder().stamp(Time.builder().sec(101).nanosec(102).build()).frameId("map").build())
                .info(mapMetaData)
                .data(new byte[]{0, 100, -1, 50, 25, 0, -1, 100})
                .build();

        assertEquals(mapMetaData, Ros2FastProto.decode(Ros2FastProto.encode(mapMetaData, Ros2Codecs.MAP_META_DATA), Ros2Codecs.MAP_META_DATA));
        assertEquals(occupancyGrid, Ros2FastProto.decode(Ros2FastProto.encode(occupancyGrid, Ros2Codecs.OCCUPANCY_GRID), Ros2Codecs.OCCUPANCY_GRID));
    }

    @Test
    public void testNavSatFixRoundTrip() {
        NavSatFix navSatFix = NavSatFix.builder()
                .header(Header.builder().stamp(Time.builder().sec(101).nanosec(102).build()).frameId("gps").build())
                .status(NavSatStatus.builder()
                        .status(NavSatStatus.STATUS_FIX)
                        .service(NavSatStatus.SERVICE_GPS | NavSatStatus.SERVICE_GALILEO)
                        .build())
                .latitude(31.2304)
                .longitude(121.4737)
                .altitude(12.5)
                .positionCovariance(covariance(0.1))
                .positionCovarianceType(NavSatFix.COVARIANCE_TYPE_KNOWN)
                .build();

        byte[] bytes = Ros2FastProto.encode(navSatFix, Ros2Codecs.NAV_SAT_FIX);
        assertEquals(navSatFix, Ros2FastProto.decode(bytes, Ros2Codecs.NAV_SAT_FIX));
    }

    @Test
    public void testEnvironmentalSensorRoundTrip() {
        Illuminance illuminance = Illuminance.builder()
                .header(Header.builder().stamp(Time.builder().sec(103).nanosec(104).build()).frameId("lux_sensor").build())
                .illuminance(523.8)
                .variance(1.2)
                .build();
        RelativeHumidity relativeHumidity = RelativeHumidity.builder()
                .header(Header.builder().stamp(Time.builder().sec(105).nanosec(106).build()).frameId("humidity_sensor").build())
                .relativeHumidity(0.48)
                .variance(0.01)
                .build();

        assertEquals(illuminance, Ros2FastProto.decode(Ros2FastProto.encode(illuminance, Ros2Codecs.ILLUMINANCE), Ros2Codecs.ILLUMINANCE));
        assertEquals(relativeHumidity, Ros2FastProto.decode(Ros2FastProto.encode(relativeHumidity, Ros2Codecs.RELATIVE_HUMIDITY), Ros2Codecs.RELATIVE_HUMIDITY));
    }

    @Test
    public void testJoyAndJointStateRoundTrip() {
        Joy joy = Joy.builder()
                .header(Header.builder().stamp(Time.builder().sec(111).nanosec(112).build()).frameId("joystick").build())
                .axes(new float[]{0.1f, -0.2f, 0.3f, -0.4f})
                .buttons(new int[]{1, 0, 1, 1})
                .build();
        JointState jointState = JointState.builder()
                .header(Header.builder().stamp(Time.builder().sec(113).nanosec(114).build()).frameId("base").build())
                .name(new String[]{"joint_1", "joint_2"})
                .position(new double[]{1.1, 2.2})
                .velocity(new double[]{0.1, 0.2})
                .effort(new double[]{10.0, 20.0})
                .build();

        assertEquals(joy, Ros2FastProto.decode(Ros2FastProto.encode(joy, Ros2Codecs.JOY), Ros2Codecs.JOY));
        assertEquals(jointState, Ros2FastProto.decode(Ros2FastProto.encode(jointState, Ros2Codecs.JOINT_STATE), Ros2Codecs.JOINT_STATE));
    }

    @Test
    public void testBatteryStateRoundTrip() {
        BatteryState batteryState = BatteryState.builder()
                .header(Header.builder().stamp(Time.builder().sec(115).nanosec(116).build()).frameId("battery").build())
                .voltage(24.6f)
                .temperature(31.5f)
                .current(-3.2f)
                .charge(4.8f)
                .capacity(6.0f)
                .designCapacity(6.4f)
                .percentage(0.75f)
                .powerSupplyStatus(BatteryState.POWER_SUPPLY_STATUS_DISCHARGING)
                .powerSupplyHealth(BatteryState.POWER_SUPPLY_HEALTH_GOOD)
                .powerSupplyTechnology(BatteryState.POWER_SUPPLY_TECHNOLOGY_LION)
                .present(true)
                .cellVoltage(new float[]{4.1f, 4.1f, 4.09f, 4.08f, 4.12f, 4.11f})
                .cellTemperature(new float[]{31.0f, 31.2f, 31.4f})
                .location("rear_compartment")
                .serialNumber("BAT-0001")
                .build();

        byte[] bytes = Ros2FastProto.encode(batteryState, Ros2Codecs.BATTERY_STATE);
        assertEquals(batteryState, Ros2FastProto.decode(bytes, Ros2Codecs.BATTERY_STATE));
    }

    @Test
    public void testTFMessageRoundTrip() {
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

        byte[] bytes = Ros2FastProto.encode(tfMessage, Ros2Codecs.TF_MESSAGE);
        assertEquals(tfMessage, Ros2FastProto.decode(bytes, Ros2Codecs.TF_MESSAGE));
    }

    private double[] covariance(double base) {
        double[] values = new double[9];
        for (int i = 0; i < values.length; i++) {
            values[i] = base * (i + 1);
        }

        return values;
    }

    private double[] fixedCovariance(double base) {
        double[] values = new double[36];
        for (int i = 0; i < values.length; i++) {
            values[i] = base * (i + 1);
        }

        return values;
    }
}
