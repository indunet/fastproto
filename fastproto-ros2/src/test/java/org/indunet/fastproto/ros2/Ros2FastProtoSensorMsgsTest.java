package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point32;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Quaternion;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Transform;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TransformStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Vector3;
import org.indunet.fastproto.ros2.sensor_msgs.msg.*;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Ros2FastProtoSensorMsgsTest extends Ros2FastProtoTestSupport {
    @Test
    void testImuRoundTrip() {
        Imu imu = Imu.builder()
                .header(Header.builder().stamp(Time.builder().sec(9).nanosec(10).build()).frameId("base_link").build())
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
    void testImageRoundTrip() {
        Image image = Image.builder()
                .header(Header.builder().stamp(Time.builder().sec(3).nanosec(4).build()).frameId("camera").build())
                .height(480).width(640).encoding("rgb8").isBigendian(0).step(1920)
                .data(new byte[]{1, 2, 3, 4, 5, 6})
                .build();

        assertEquals(image, Ros2FastProto.decode(Ros2FastProto.encode(image, Ros2Codecs.IMAGE), Ros2Codecs.IMAGE));
    }

    @Test
    void testCompressedImageRoundTrip() {
        CompressedImage image = CompressedImage.builder()
                .header(Header.builder().stamp(Time.builder().sec(5).nanosec(6).build()).frameId("camera_compressed").build())
                .format("jpeg")
                .data(new byte[]{9, 8, 7, 6, 5, 4, 3})
                .build();

        assertEquals(image, Ros2FastProto.decode(Ros2FastProto.encode(image, Ros2Codecs.COMPRESSED_IMAGE), Ros2Codecs.COMPRESSED_IMAGE));
    }

    @Test
    void testCameraInfoRoundTrip() {
        CameraInfo cameraInfo = CameraInfo.builder()
                .header(Header.builder().stamp(Time.builder().sec(7).nanosec(8).build()).frameId("camera_optical").build())
                .height(1080).width(1920).distortionModel("plumb_bob")
                .d(new double[]{0.1, 0.2, 0.3, 0.4, 0.5})
                .k(new double[]{1, 0, 960, 0, 1, 540, 0, 0, 1})
                .r(new double[]{1, 0, 0, 0, 1, 0, 0, 0, 1})
                .p(new double[]{1, 0, 960, 0, 0, 1, 540, 0, 0, 0, 1, 0})
                .binningX(2).binningY(2)
                .roi(RegionOfInterest.builder().xOffset(10).yOffset(20).height(720).width(1280).doRectify(true).build())
                .build();

        assertEquals(cameraInfo, Ros2FastProto.decode(Ros2FastProto.encode(cameraInfo, Ros2Codecs.CAMERA_INFO), Ros2Codecs.CAMERA_INFO));
    }

    @Test
    void testMagneticFieldRoundTrip() {
        MagneticField magneticField = MagneticField.builder()
                .header(Header.builder().stamp(Time.builder().sec(17).nanosec(18).build()).frameId("imu_link").build())
                .magneticField(Vector3.builder().x(0.01).y(-0.02).z(0.03).build())
                .magneticFieldCovariance(covariance(0.0001))
                .build();

        assertEquals(magneticField, Ros2FastProto.decode(Ros2FastProto.encode(magneticField, Ros2Codecs.MAGNETIC_FIELD), Ros2Codecs.MAGNETIC_FIELD));
    }

    @Test
    void testTemperatureAndFluidPressureRoundTrip() {
        Temperature temperature = Temperature.builder()
                .header(Header.builder().stamp(Time.builder().sec(19).nanosec(20).build()).frameId("temp_sensor").build())
                .temperature(36.75).variance(0.15).build();
        FluidPressure fluidPressure = FluidPressure.builder()
                .header(Header.builder().stamp(Time.builder().sec(21).nanosec(22).build()).frameId("barometer").build())
                .fluidPressure(101325.0).variance(12.5).build();

        assertEquals(temperature, Ros2FastProto.decode(Ros2FastProto.encode(temperature, Ros2Codecs.TEMPERATURE), Ros2Codecs.TEMPERATURE));
        assertEquals(fluidPressure, Ros2FastProto.decode(Ros2FastProto.encode(fluidPressure, Ros2Codecs.FLUID_PRESSURE), Ros2Codecs.FLUID_PRESSURE));
    }

    @Test
    void testPointCloud2RoundTrip() {
        PointCloud2 pointCloud2 = PointCloud2.builder()
                .header(Header.builder().stamp(Time.builder().sec(21).nanosec(22).build()).frameId("lidar").build())
                .height(1).width(2)
                .fields(new PointField[]{
                        PointField.builder().name("x").offset(0).datatype(PointField.FLOAT32).count(1).build(),
                        PointField.builder().name("y").offset(4).datatype(PointField.FLOAT32).count(1).build(),
                        PointField.builder().name("z").offset(8).datatype(PointField.FLOAT32).count(1).build(),
                        PointField.builder().name("intensity").offset(12).datatype(PointField.FLOAT32).count(1).build()
                })
                .isBigendian(false).pointStep(16).rowStep(32)
                .data(new byte[]{
                        0, 0, 0, 63, 0, 0, 0, 64, 0, 0, 64, 64, 0, 0, -128, 64,
                        0, 0, -96, 64, 0, 0, -64, 64, 0, 0, -32, 64, 0, 0, 0, 65
                })
                .isDense(true)
                .build();

        assertEquals(pointCloud2, Ros2FastProto.decode(Ros2FastProto.encode(pointCloud2, Ros2Codecs.POINT_CLOUD2), Ros2Codecs.POINT_CLOUD2));
    }

    @Test
    void testPointCloudRoundTrip() {
        PointCloud pointCloud = PointCloud.builder()
                .header(Header.builder().stamp(Time.builder().sec(23).nanosec(24).build()).frameId("laser_frame").build())
                .points(new Point32[]{
                        Point32.builder().x(1.0f).y(2.0f).z(0.0f).build(),
                        Point32.builder().x(3.0f).y(4.0f).z(0.5f).build()
                })
                .channels(new ChannelFloat32[]{
                        ChannelFloat32.builder().name("intensity").values(new float[]{10.0f, 20.0f}).build(),
                        ChannelFloat32.builder().name("distance").values(new float[]{2.2f, 5.1f}).build()
                })
                .build();

        assertEquals(pointCloud, Ros2FastProto.decode(Ros2FastProto.encode(pointCloud, Ros2Codecs.POINT_CLOUD), Ros2Codecs.POINT_CLOUD));
    }

    @Test
    void testLaserScanRoundTrip() {
        LaserScan laserScan = LaserScan.builder()
                .header(Header.builder().stamp(Time.builder().sec(71).nanosec(72).build()).frameId("laser").build())
                .angleMin(-1.57f).angleMax(1.57f).angleIncrement(0.01f).timeIncrement(0.001f).scanTime(0.1f)
                .rangeMin(0.2f).rangeMax(30.0f)
                .ranges(new float[]{1.0f, 1.5f, 2.0f, 2.5f})
                .intensities(new float[]{10.0f, 20.0f, 30.0f, 40.0f})
                .build();

        assertEquals(laserScan, Ros2FastProto.decode(Ros2FastProto.encode(laserScan, Ros2Codecs.LASER_SCAN), Ros2Codecs.LASER_SCAN));
    }

    @Test
    void testMultiEchoLaserScanRoundTrip() {
        MultiEchoLaserScan laserScan = MultiEchoLaserScan.builder()
                .header(Header.builder().stamp(Time.builder().sec(75).nanosec(76).build()).frameId("multi_laser").build())
                .angleMin(-1.0f)
                .angleMax(1.0f)
                .angleIncrement(0.02f)
                .timeIncrement(0.001f)
                .scanTime(0.2f)
                .rangeMin(0.1f)
                .rangeMax(40.0f)
                .ranges(new LaserEcho[]{
                        LaserEcho.builder().echoes(new float[]{1.0f, 0.95f}).build(),
                        LaserEcho.builder().echoes(new float[]{1.5f, 1.45f, 1.4f}).build()
                })
                .intensities(new LaserEcho[]{
                        LaserEcho.builder().echoes(new float[]{10.0f, 11.0f}).build(),
                        LaserEcho.builder().echoes(new float[]{20.0f, 19.0f, 18.0f}).build()
                })
                .build();

        assertEquals(laserScan, Ros2FastProto.decode(Ros2FastProto.encode(laserScan, Ros2Codecs.MULTI_ECHO_LASER_SCAN), Ros2Codecs.MULTI_ECHO_LASER_SCAN));
    }

    @Test
    void testRangeRoundTrip() {
        Range range = Range.builder()
                .header(Header.builder().stamp(Time.builder().sec(73).nanosec(74).build()).frameId("ultrasonic").build())
                .radiationType(Range.ULTRASOUND).fieldOfView(0.52f).minRange(0.02f).maxRange(4.0f).range(1.35f)
                .build();

        assertEquals(range, Ros2FastProto.decode(Ros2FastProto.encode(range, Ros2Codecs.RANGE), Ros2Codecs.RANGE));
    }

    @Test
    void testNavSatFixRoundTrip() {
        NavSatFix navSatFix = NavSatFix.builder()
                .header(Header.builder().stamp(Time.builder().sec(101).nanosec(102).build()).frameId("gps").build())
                .status(NavSatStatus.builder()
                        .status(NavSatStatus.STATUS_FIX)
                        .service(NavSatStatus.SERVICE_GPS | NavSatStatus.SERVICE_GALILEO)
                        .build())
                .latitude(31.2304).longitude(121.4737).altitude(12.5)
                .positionCovariance(covariance(0.1))
                .positionCovarianceType(NavSatFix.COVARIANCE_TYPE_KNOWN)
                .build();

        assertEquals(navSatFix, Ros2FastProto.decode(Ros2FastProto.encode(navSatFix, Ros2Codecs.NAV_SAT_FIX), Ros2Codecs.NAV_SAT_FIX));
    }

    @Test
    void testEnvironmentalSensorRoundTrip() {
        Illuminance illuminance = Illuminance.builder()
                .header(Header.builder().stamp(Time.builder().sec(103).nanosec(104).build()).frameId("lux_sensor").build())
                .illuminance(523.8).variance(1.2).build();
        RelativeHumidity relativeHumidity = RelativeHumidity.builder()
                .header(Header.builder().stamp(Time.builder().sec(105).nanosec(106).build()).frameId("humidity_sensor").build())
                .relativeHumidity(0.48).variance(0.01).build();

        assertEquals(illuminance, Ros2FastProto.decode(Ros2FastProto.encode(illuminance, Ros2Codecs.ILLUMINANCE), Ros2Codecs.ILLUMINANCE));
        assertEquals(relativeHumidity, Ros2FastProto.decode(Ros2FastProto.encode(relativeHumidity, Ros2Codecs.RELATIVE_HUMIDITY), Ros2Codecs.RELATIVE_HUMIDITY));
    }

    @Test
    void testJoyAndJointStateRoundTrip() {
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
    void testJoyFeedbackArrayRoundTrip() {
        JoyFeedbackArray feedbackArray = JoyFeedbackArray.builder()
                .array(new JoyFeedback[]{
                        JoyFeedback.builder().type(JoyFeedback.TYPE_RUMBLE).id(0).intensity(0.8f).build(),
                        JoyFeedback.builder().type(JoyFeedback.TYPE_LED).id(2).intensity(1.0f).build(),
                        JoyFeedback.builder().type(JoyFeedback.TYPE_BUZZER).id(1).intensity(0.35f).build()
                })
                .build();

        assertEquals(feedbackArray, Ros2FastProto.decode(Ros2FastProto.encode(feedbackArray, Ros2Codecs.JOY_FEEDBACK_ARRAY), Ros2Codecs.JOY_FEEDBACK_ARRAY));
    }

    @Test
    void testBatteryStateRoundTrip() {
        BatteryState batteryState = BatteryState.builder()
                .header(Header.builder().stamp(Time.builder().sec(115).nanosec(116).build()).frameId("battery").build())
                .voltage(24.6f).temperature(31.5f).current(-3.2f).charge(4.8f).capacity(6.0f).designCapacity(6.4f).percentage(0.75f)
                .powerSupplyStatus(BatteryState.POWER_SUPPLY_STATUS_DISCHARGING)
                .powerSupplyHealth(BatteryState.POWER_SUPPLY_HEALTH_GOOD)
                .powerSupplyTechnology(BatteryState.POWER_SUPPLY_TECHNOLOGY_LION)
                .present(true)
                .cellVoltage(new float[]{4.1f, 4.1f, 4.09f, 4.08f, 4.12f, 4.11f})
                .cellTemperature(new float[]{31.0f, 31.2f, 31.4f})
                .location("rear_compartment")
                .serialNumber("BAT-0001")
                .build();

        assertEquals(batteryState, Ros2FastProto.decode(Ros2FastProto.encode(batteryState, Ros2Codecs.BATTERY_STATE), Ros2Codecs.BATTERY_STATE));
    }

    @Test
    void testTimeReferenceRoundTrip() {
        TimeReference timeReference = TimeReference.builder()
                .header(Header.builder().stamp(Time.builder().sec(131).nanosec(132).build()).frameId("gps").build())
                .timeRef(Time.builder().sec(1_700_000_000).nanosec(123_456_789L).build())
                .source("gnss")
                .build();

        assertEquals(timeReference, Ros2FastProto.decode(Ros2FastProto.encode(timeReference, Ros2Codecs.TIME_REFERENCE), Ros2Codecs.TIME_REFERENCE));
    }
}
