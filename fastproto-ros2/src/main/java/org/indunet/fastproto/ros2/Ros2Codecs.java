package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.internal.FixedSizeRos2Codec;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Duration;
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
import org.indunet.fastproto.ros2.std_msgs.msg.ColorRGBA;
import org.indunet.fastproto.ros2.std_msgs.msg.Float32MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.Float64MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;
import org.indunet.fastproto.ros2.std_msgs.msg.Int32MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.MultiArrayDimension;
import org.indunet.fastproto.ros2.std_msgs.msg.MultiArrayLayout;
import org.indunet.fastproto.ros2.std_msgs.msg.UInt8MultiArray;
import org.indunet.fastproto.ros2.tf2_msgs.msg.TFMessage;

/**
 * Built-in codecs for a few classic ROS2 message types.
 */
public final class Ros2Codecs {
    public static final Ros2Codec<Time> TIME =
            new FixedSizeRos2Codec<>(Time.class, 4, Time.SIZE);

    public static final Ros2Codec<Duration> DURATION =
            new FixedSizeRos2Codec<>(Duration.class, 4, Duration.SIZE);

    public static final Ros2Codec<Point> POINT =
            new FixedSizeRos2Codec<>(Point.class, 8, Point.SIZE);

    public static final Ros2Codec<Point32> POINT32 =
            new FixedSizeRos2Codec<>(Point32.class, 4, Point32.SIZE);

    public static final Ros2Codec<Pose2D> POSE2D =
            new FixedSizeRos2Codec<>(Pose2D.class, 8, Pose2D.SIZE);

    public static final Ros2Codec<Quaternion> QUATERNION =
            new FixedSizeRos2Codec<>(Quaternion.class, 8, Quaternion.SIZE);

    public static final Ros2Codec<Vector3> VECTOR3 =
            new FixedSizeRos2Codec<>(Vector3.class, 8, Vector3.SIZE);

    public static final Ros2Codec<ColorRGBA> COLOR_RGBA =
            new FixedSizeRos2Codec<>(ColorRGBA.class, 4, ColorRGBA.SIZE);

    public static final Ros2Codec<Header> HEADER = new Ros2Codec<Header>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Header value) {
            TIME.serialize(writer, value.getStamp());
            writer.writeString(value.getFrameId());
        }

        @Override
        public Header deserialize(Ros2CdrReader reader) {
            return Header.builder()
                    .stamp(TIME.deserialize(reader))
                    .frameId(reader.readString())
                    .build();
        }
    };

    public static final Ros2Codec<MultiArrayDimension> MULTI_ARRAY_DIMENSION = new Ros2Codec<MultiArrayDimension>() {
        @Override
        public void serialize(Ros2CdrWriter writer, MultiArrayDimension value) {
            writer.writeString(value.getLabel());
            writer.writeUInt32(value.getSize());
            writer.writeUInt32(value.getStride());
        }

        @Override
        public MultiArrayDimension deserialize(Ros2CdrReader reader) {
            return MultiArrayDimension.builder()
                    .label(reader.readString())
                    .size(reader.readUInt32())
                    .stride(reader.readUInt32())
                    .build();
        }
    };

    public static final Ros2Codec<MultiArrayLayout> MULTI_ARRAY_LAYOUT = new Ros2Codec<MultiArrayLayout>() {
        @Override
        public void serialize(Ros2CdrWriter writer, MultiArrayLayout value) {
            writeMultiArrayDimensionArray(writer, value.getDim());
            writer.writeUInt32(value.getDataOffset());
        }

        @Override
        public MultiArrayLayout deserialize(Ros2CdrReader reader) {
            return MultiArrayLayout.builder()
                    .dim(readMultiArrayDimensionArray(reader))
                    .dataOffset(reader.readUInt32())
                    .build();
        }
    };

    public static final Ros2Codec<Float32MultiArray> FLOAT32_MULTI_ARRAY = new Ros2Codec<Float32MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Float32MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeFloatSequence(safeFloatArray(value.getData()));
        }

        @Override
        public Float32MultiArray deserialize(Ros2CdrReader reader) {
            return Float32MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readFloatSequence())
                    .build();
        }
    };

    public static final Ros2Codec<Float64MultiArray> FLOAT64_MULTI_ARRAY = new Ros2Codec<Float64MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Float64MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeDoubleSequence(safeDoubleArray(value.getData()));
        }

        @Override
        public Float64MultiArray deserialize(Ros2CdrReader reader) {
            return Float64MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readDoubleSequence())
                    .build();
        }
    };

    public static final Ros2Codec<Int32MultiArray> INT32_MULTI_ARRAY = new Ros2Codec<Int32MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Int32MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeInt32Sequence(safeIntArray(value.getData()));
        }

        @Override
        public Int32MultiArray deserialize(Ros2CdrReader reader) {
            return Int32MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readInt32Sequence())
                    .build();
        }
    };

    public static final Ros2Codec<UInt8MultiArray> UINT8_MULTI_ARRAY = new Ros2Codec<UInt8MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, UInt8MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeByteSequence(value.getData() == null ? new byte[0] : value.getData());
        }

        @Override
        public UInt8MultiArray deserialize(Ros2CdrReader reader) {
            return UInt8MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readByteSequence())
                    .build();
        }
    };

    public static final Ros2Codec<Pose> POSE = new Ros2Codec<Pose>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Pose value) {
            POINT.serialize(writer, value.getPosition());
            QUATERNION.serialize(writer, value.getOrientation());
        }

        @Override
        public Pose deserialize(Ros2CdrReader reader) {
            return Pose.builder()
                    .position(POINT.deserialize(reader))
                    .orientation(QUATERNION.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<Polygon> POLYGON = new Ros2Codec<Polygon>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Polygon value) {
            writePoint32Array(writer, value.getPoints());
        }

        @Override
        public Polygon deserialize(Ros2CdrReader reader) {
            return Polygon.builder()
                    .points(readPoint32Array(reader))
                    .build();
        }
    };

    public static final Ros2Codec<PointStamped> POINT_STAMPED = new Ros2Codec<PointStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PointStamped value) {
            HEADER.serialize(writer, value.getHeader());
            POINT.serialize(writer, value.getPoint());
        }

        @Override
        public PointStamped deserialize(Ros2CdrReader reader) {
            return PointStamped.builder()
                    .header(HEADER.deserialize(reader))
                    .point(POINT.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<Vector3Stamped> VECTOR3_STAMPED = new Ros2Codec<Vector3Stamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Vector3Stamped value) {
            HEADER.serialize(writer, value.getHeader());
            VECTOR3.serialize(writer, value.getVector());
        }

        @Override
        public Vector3Stamped deserialize(Ros2CdrReader reader) {
            return Vector3Stamped.builder()
                    .header(HEADER.deserialize(reader))
                    .vector(VECTOR3.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<QuaternionStamped> QUATERNION_STAMPED = new Ros2Codec<QuaternionStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, QuaternionStamped value) {
            HEADER.serialize(writer, value.getHeader());
            QUATERNION.serialize(writer, value.getQuaternion());
        }

        @Override
        public QuaternionStamped deserialize(Ros2CdrReader reader) {
            return QuaternionStamped.builder()
                    .header(HEADER.deserialize(reader))
                    .quaternion(QUATERNION.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<Twist> TWIST = new Ros2Codec<Twist>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Twist value) {
            VECTOR3.serialize(writer, value.getLinear());
            VECTOR3.serialize(writer, value.getAngular());
        }

        @Override
        public Twist deserialize(Ros2CdrReader reader) {
            return Twist.builder()
                    .linear(VECTOR3.deserialize(reader))
                    .angular(VECTOR3.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<Transform> TRANSFORM = new Ros2Codec<Transform>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Transform value) {
            VECTOR3.serialize(writer, value.getTranslation());
            QUATERNION.serialize(writer, value.getRotation());
        }

        @Override
        public Transform deserialize(Ros2CdrReader reader) {
            return Transform.builder()
                    .translation(VECTOR3.deserialize(reader))
                    .rotation(QUATERNION.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<Accel> ACCEL = new Ros2Codec<Accel>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Accel value) {
            VECTOR3.serialize(writer, value.getLinear());
            VECTOR3.serialize(writer, value.getAngular());
        }

        @Override
        public Accel deserialize(Ros2CdrReader reader) {
            return Accel.builder()
                    .linear(VECTOR3.deserialize(reader))
                    .angular(VECTOR3.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<Wrench> WRENCH = new Ros2Codec<Wrench>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Wrench value) {
            VECTOR3.serialize(writer, value.getForce());
            VECTOR3.serialize(writer, value.getTorque());
        }

        @Override
        public Wrench deserialize(Ros2CdrReader reader) {
            return Wrench.builder()
                    .force(VECTOR3.deserialize(reader))
                    .torque(VECTOR3.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<Inertia> INERTIA = new Ros2Codec<Inertia>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Inertia value) {
            writer.writeDouble(value.getM());
            VECTOR3.serialize(writer, value.getCom());
            writer.writeDouble(value.getIxx());
            writer.writeDouble(value.getIxy());
            writer.writeDouble(value.getIxz());
            writer.writeDouble(value.getIyy());
            writer.writeDouble(value.getIyz());
            writer.writeDouble(value.getIzz());
        }

        @Override
        public Inertia deserialize(Ros2CdrReader reader) {
            return Inertia.builder()
                    .m(reader.readDouble())
                    .com(VECTOR3.deserialize(reader))
                    .ixx(reader.readDouble())
                    .ixy(reader.readDouble())
                    .ixz(reader.readDouble())
                    .iyy(reader.readDouble())
                    .iyz(reader.readDouble())
                    .izz(reader.readDouble())
                    .build();
        }
    };

    public static final Ros2Codec<PoseStamped> POSE_STAMPED = new Ros2Codec<PoseStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PoseStamped value) {
            HEADER.serialize(writer, value.getHeader());
            POSE.serialize(writer, value.getPose());
        }

        @Override
        public PoseStamped deserialize(Ros2CdrReader reader) {
            return PoseStamped.builder()
                    .header(HEADER.deserialize(reader))
                    .pose(POSE.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<PolygonStamped> POLYGON_STAMPED = new Ros2Codec<PolygonStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PolygonStamped value) {
            HEADER.serialize(writer, value.getHeader());
            POLYGON.serialize(writer, value.getPolygon());
        }

        @Override
        public PolygonStamped deserialize(Ros2CdrReader reader) {
            return PolygonStamped.builder()
                    .header(HEADER.deserialize(reader))
                    .polygon(POLYGON.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<AccelStamped> ACCEL_STAMPED = new Ros2Codec<AccelStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, AccelStamped value) {
            HEADER.serialize(writer, value.getHeader());
            ACCEL.serialize(writer, value.getAccel());
        }

        @Override
        public AccelStamped deserialize(Ros2CdrReader reader) {
            return AccelStamped.builder()
                    .header(HEADER.deserialize(reader))
                    .accel(ACCEL.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<TwistStamped> TWIST_STAMPED = new Ros2Codec<TwistStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, TwistStamped value) {
            HEADER.serialize(writer, value.getHeader());
            TWIST.serialize(writer, value.getTwist());
        }

        @Override
        public TwistStamped deserialize(Ros2CdrReader reader) {
            return TwistStamped.builder()
                    .header(HEADER.deserialize(reader))
                    .twist(TWIST.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<WrenchStamped> WRENCH_STAMPED = new Ros2Codec<WrenchStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, WrenchStamped value) {
            HEADER.serialize(writer, value.getHeader());
            WRENCH.serialize(writer, value.getWrench());
        }

        @Override
        public WrenchStamped deserialize(Ros2CdrReader reader) {
            return WrenchStamped.builder()
                    .header(HEADER.deserialize(reader))
                    .wrench(WRENCH.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<InertiaStamped> INERTIA_STAMPED = new Ros2Codec<InertiaStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, InertiaStamped value) {
            HEADER.serialize(writer, value.getHeader());
            INERTIA.serialize(writer, value.getInertia());
        }

        @Override
        public InertiaStamped deserialize(Ros2CdrReader reader) {
            return InertiaStamped.builder()
                    .header(HEADER.deserialize(reader))
                    .inertia(INERTIA.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<TransformStamped> TRANSFORM_STAMPED = new Ros2Codec<TransformStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, TransformStamped value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeString(value.getChildFrameId());
            TRANSFORM.serialize(writer, value.getTransform());
        }

        @Override
        public TransformStamped deserialize(Ros2CdrReader reader) {
            return TransformStamped.builder()
                    .header(HEADER.deserialize(reader))
                    .childFrameId(reader.readString())
                    .transform(TRANSFORM.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<Imu> IMU = new Ros2Codec<Imu>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Imu value) {
            HEADER.serialize(writer, value.getHeader());
            QUATERNION.serialize(writer, value.getOrientation());
            writeFixedDoubleArray(writer, value.getOrientationCovariance(), 9, "orientation_covariance");
            VECTOR3.serialize(writer, value.getAngularVelocity());
            writeFixedDoubleArray(writer, value.getAngularVelocityCovariance(), 9, "angular_velocity_covariance");
            VECTOR3.serialize(writer, value.getLinearAcceleration());
            writeFixedDoubleArray(writer, value.getLinearAccelerationCovariance(), 9, "linear_acceleration_covariance");
        }

        @Override
        public Imu deserialize(Ros2CdrReader reader) {
            return Imu.builder()
                    .header(HEADER.deserialize(reader))
                    .orientation(QUATERNION.deserialize(reader))
                    .orientationCovariance(readFixedDoubleArray(reader, 9))
                    .angularVelocity(VECTOR3.deserialize(reader))
                    .angularVelocityCovariance(readFixedDoubleArray(reader, 9))
                    .linearAcceleration(VECTOR3.deserialize(reader))
                    .linearAccelerationCovariance(readFixedDoubleArray(reader, 9))
                    .build();
        }
    };

    public static final Ros2Codec<PoseWithCovariance> POSE_WITH_COVARIANCE = new Ros2Codec<PoseWithCovariance>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PoseWithCovariance value) {
            POSE.serialize(writer, value.getPose());
            writeFixedDoubleArray(writer, value.getCovariance(), 36, "covariance");
        }

        @Override
        public PoseWithCovariance deserialize(Ros2CdrReader reader) {
            return PoseWithCovariance.builder()
                    .pose(POSE.deserialize(reader))
                    .covariance(readFixedDoubleArray(reader, 36))
                    .build();
        }
    };

    public static final Ros2Codec<PoseWithCovarianceStamped> POSE_WITH_COVARIANCE_STAMPED = new Ros2Codec<PoseWithCovarianceStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PoseWithCovarianceStamped value) {
            HEADER.serialize(writer, value.getHeader());
            POSE_WITH_COVARIANCE.serialize(writer, value.getPose());
        }

        @Override
        public PoseWithCovarianceStamped deserialize(Ros2CdrReader reader) {
            return PoseWithCovarianceStamped.builder()
                    .header(HEADER.deserialize(reader))
                    .pose(POSE_WITH_COVARIANCE.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<PoseArray> POSE_ARRAY = new Ros2Codec<PoseArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PoseArray value) {
            HEADER.serialize(writer, value.getHeader());
            writePoseArray(writer, value.getPoses());
        }

        @Override
        public PoseArray deserialize(Ros2CdrReader reader) {
            return PoseArray.builder()
                    .header(HEADER.deserialize(reader))
                    .poses(readPoseArray(reader))
                    .build();
        }
    };

    public static final Ros2Codec<TwistWithCovariance> TWIST_WITH_COVARIANCE = new Ros2Codec<TwistWithCovariance>() {
        @Override
        public void serialize(Ros2CdrWriter writer, TwistWithCovariance value) {
            TWIST.serialize(writer, value.getTwist());
            writeFixedDoubleArray(writer, value.getCovariance(), 36, "covariance");
        }

        @Override
        public TwistWithCovariance deserialize(Ros2CdrReader reader) {
            return TwistWithCovariance.builder()
                    .twist(TWIST.deserialize(reader))
                    .covariance(readFixedDoubleArray(reader, 36))
                    .build();
        }
    };

    public static final Ros2Codec<Image> IMAGE = new Ros2Codec<Image>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Image value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeUInt32(value.getHeight());
            writer.writeUInt32(value.getWidth());
            writer.writeString(value.getEncoding());
            writer.writeUInt8(value.getIsBigendian());
            writer.writeUInt32(value.getStep());
            writer.writeByteSequence(value.getData());
        }

        @Override
        public Image deserialize(Ros2CdrReader reader) {
            return Image.builder()
                    .header(HEADER.deserialize(reader))
                    .height(reader.readUInt32())
                    .width(reader.readUInt32())
                    .encoding(reader.readString())
                    .isBigendian(reader.readUInt8())
                    .step(reader.readUInt32())
                    .data(reader.readByteSequence())
                    .build();
        }
    };

    public static final Ros2Codec<CompressedImage> COMPRESSED_IMAGE = new Ros2Codec<CompressedImage>() {
        @Override
        public void serialize(Ros2CdrWriter writer, CompressedImage value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeString(value.getFormat());
            writer.writeByteSequence(value.getData());
        }

        @Override
        public CompressedImage deserialize(Ros2CdrReader reader) {
            return CompressedImage.builder()
                    .header(HEADER.deserialize(reader))
                    .format(reader.readString())
                    .data(reader.readByteSequence())
                    .build();
        }
    };

    public static final Ros2Codec<ChannelFloat32> CHANNEL_FLOAT32 = new Ros2Codec<ChannelFloat32>() {
        @Override
        public void serialize(Ros2CdrWriter writer, ChannelFloat32 value) {
            writer.writeString(value.getName());
            writer.writeFloatSequence(safeFloatArray(value.getValues()));
        }

        @Override
        public ChannelFloat32 deserialize(Ros2CdrReader reader) {
            return ChannelFloat32.builder()
                    .name(reader.readString())
                    .values(reader.readFloatSequence())
                    .build();
        }
    };

    public static final Ros2Codec<RegionOfInterest> REGION_OF_INTEREST = new Ros2Codec<RegionOfInterest>() {
        @Override
        public void serialize(Ros2CdrWriter writer, RegionOfInterest value) {
            writer.writeUInt32(value.getXOffset());
            writer.writeUInt32(value.getYOffset());
            writer.writeUInt32(value.getHeight());
            writer.writeUInt32(value.getWidth());
            writer.writeBool(value.isDoRectify());
        }

        @Override
        public RegionOfInterest deserialize(Ros2CdrReader reader) {
            return RegionOfInterest.builder()
                    .xOffset(reader.readUInt32())
                    .yOffset(reader.readUInt32())
                    .height(reader.readUInt32())
                    .width(reader.readUInt32())
                    .doRectify(reader.readBool())
                    .build();
        }
    };

    public static final Ros2Codec<PointField> POINT_FIELD = new Ros2Codec<PointField>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PointField value) {
            writer.writeString(value.getName());
            writer.writeUInt32(value.getOffset());
            writer.writeUInt8(value.getDatatype());
            writer.writeUInt32(value.getCount());
        }

        @Override
        public PointField deserialize(Ros2CdrReader reader) {
            return PointField.builder()
                    .name(reader.readString())
                    .offset(reader.readUInt32())
                    .datatype(reader.readUInt8())
                    .count(reader.readUInt32())
                    .build();
        }
    };

    public static final Ros2Codec<NavSatStatus> NAV_SAT_STATUS = new Ros2Codec<NavSatStatus>() {
        @Override
        public void serialize(Ros2CdrWriter writer, NavSatStatus value) {
            writer.writeInt8(value.getStatus());
            writer.writeUInt16(value.getService());
        }

        @Override
        public NavSatStatus deserialize(Ros2CdrReader reader) {
            return NavSatStatus.builder()
                    .status(reader.readInt8())
                    .service(reader.readUInt16())
                    .build();
        }
    };

    public static final Ros2Codec<CameraInfo> CAMERA_INFO = new Ros2Codec<CameraInfo>() {
        @Override
        public void serialize(Ros2CdrWriter writer, CameraInfo value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeUInt32(value.getHeight());
            writer.writeUInt32(value.getWidth());
            writer.writeString(value.getDistortionModel());
            writer.writeDoubleSequence(value.getD());
            writeFixedDoubleArray(writer, value.getK(), 9, "k");
            writeFixedDoubleArray(writer, value.getR(), 9, "r");
            writeFixedDoubleArray(writer, value.getP(), 12, "p");
            writer.writeUInt32(value.getBinningX());
            writer.writeUInt32(value.getBinningY());
            REGION_OF_INTEREST.serialize(writer, value.getRoi());
        }

        @Override
        public CameraInfo deserialize(Ros2CdrReader reader) {
            return CameraInfo.builder()
                    .header(HEADER.deserialize(reader))
                    .height(reader.readUInt32())
                    .width(reader.readUInt32())
                    .distortionModel(reader.readString())
                    .d(reader.readDoubleSequence())
                    .k(readFixedDoubleArray(reader, 9))
                    .r(readFixedDoubleArray(reader, 9))
                    .p(readFixedDoubleArray(reader, 12))
                    .binningX(reader.readUInt32())
                    .binningY(reader.readUInt32())
                    .roi(REGION_OF_INTEREST.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<MagneticField> MAGNETIC_FIELD = new Ros2Codec<MagneticField>() {
        @Override
        public void serialize(Ros2CdrWriter writer, MagneticField value) {
            HEADER.serialize(writer, value.getHeader());
            VECTOR3.serialize(writer, value.getMagneticField());
            writeFixedDoubleArray(writer, value.getMagneticFieldCovariance(), 9, "magnetic_field_covariance");
        }

        @Override
        public MagneticField deserialize(Ros2CdrReader reader) {
            return MagneticField.builder()
                    .header(HEADER.deserialize(reader))
                    .magneticField(VECTOR3.deserialize(reader))
                    .magneticFieldCovariance(readFixedDoubleArray(reader, 9))
                    .build();
        }
    };

    public static final Ros2Codec<Temperature> TEMPERATURE = new Ros2Codec<Temperature>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Temperature value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeDouble(value.getTemperature());
            writer.writeDouble(value.getVariance());
        }

        @Override
        public Temperature deserialize(Ros2CdrReader reader) {
            return Temperature.builder()
                    .header(HEADER.deserialize(reader))
                    .temperature(reader.readDouble())
                    .variance(reader.readDouble())
                    .build();
        }
    };

    public static final Ros2Codec<FluidPressure> FLUID_PRESSURE = new Ros2Codec<FluidPressure>() {
        @Override
        public void serialize(Ros2CdrWriter writer, FluidPressure value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeDouble(value.getFluidPressure());
            writer.writeDouble(value.getVariance());
        }

        @Override
        public FluidPressure deserialize(Ros2CdrReader reader) {
            return FluidPressure.builder()
                    .header(HEADER.deserialize(reader))
                    .fluidPressure(reader.readDouble())
                    .variance(reader.readDouble())
                    .build();
        }
    };

    public static final Ros2Codec<Illuminance> ILLUMINANCE = new Ros2Codec<Illuminance>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Illuminance value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeDouble(value.getIlluminance());
            writer.writeDouble(value.getVariance());
        }

        @Override
        public Illuminance deserialize(Ros2CdrReader reader) {
            return Illuminance.builder()
                    .header(HEADER.deserialize(reader))
                    .illuminance(reader.readDouble())
                    .variance(reader.readDouble())
                    .build();
        }
    };

    public static final Ros2Codec<RelativeHumidity> RELATIVE_HUMIDITY = new Ros2Codec<RelativeHumidity>() {
        @Override
        public void serialize(Ros2CdrWriter writer, RelativeHumidity value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeDouble(value.getRelativeHumidity());
            writer.writeDouble(value.getVariance());
        }

        @Override
        public RelativeHumidity deserialize(Ros2CdrReader reader) {
            return RelativeHumidity.builder()
                    .header(HEADER.deserialize(reader))
                    .relativeHumidity(reader.readDouble())
                    .variance(reader.readDouble())
                    .build();
        }
    };

    public static final Ros2Codec<PointCloud2> POINT_CLOUD2 = new Ros2Codec<PointCloud2>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PointCloud2 value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeUInt32(value.getHeight());
            writer.writeUInt32(value.getWidth());
            writePointFieldArray(writer, value.getFields());
            writer.writeBool(value.isBigendian());
            writer.writeUInt32(value.getPointStep());
            writer.writeUInt32(value.getRowStep());
            writer.writeByteSequence(value.getData());
            writer.writeBool(value.isDense());
        }

        @Override
        public PointCloud2 deserialize(Ros2CdrReader reader) {
            return PointCloud2.builder()
                    .header(HEADER.deserialize(reader))
                    .height(reader.readUInt32())
                    .width(reader.readUInt32())
                    .fields(readPointFieldArray(reader))
                    .isBigendian(reader.readBool())
                    .pointStep(reader.readUInt32())
                    .rowStep(reader.readUInt32())
                    .data(reader.readByteSequence())
                    .isDense(reader.readBool())
                    .build();
        }
    };

    public static final Ros2Codec<PointCloud> POINT_CLOUD = new Ros2Codec<PointCloud>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PointCloud value) {
            HEADER.serialize(writer, value.getHeader());
            writePoint32Array(writer, value.getPoints());
            writeChannelFloat32Array(writer, value.getChannels());
        }

        @Override
        public PointCloud deserialize(Ros2CdrReader reader) {
            return PointCloud.builder()
                    .header(HEADER.deserialize(reader))
                    .points(readPoint32Array(reader))
                    .channels(readChannelFloat32Array(reader))
                    .build();
        }
    };

    public static final Ros2Codec<NavSatFix> NAV_SAT_FIX = new Ros2Codec<NavSatFix>() {
        @Override
        public void serialize(Ros2CdrWriter writer, NavSatFix value) {
            HEADER.serialize(writer, value.getHeader());
            NAV_SAT_STATUS.serialize(writer, value.getStatus());
            writer.writeDouble(value.getLatitude());
            writer.writeDouble(value.getLongitude());
            writer.writeDouble(value.getAltitude());
            writeFixedDoubleArray(writer, value.getPositionCovariance(), 9, "position_covariance");
            writer.writeUInt8(value.getPositionCovarianceType());
        }

        @Override
        public NavSatFix deserialize(Ros2CdrReader reader) {
            return NavSatFix.builder()
                    .header(HEADER.deserialize(reader))
                    .status(NAV_SAT_STATUS.deserialize(reader))
                    .latitude(reader.readDouble())
                    .longitude(reader.readDouble())
                    .altitude(reader.readDouble())
                    .positionCovariance(readFixedDoubleArray(reader, 9))
                    .positionCovarianceType(reader.readUInt8())
                    .build();
        }
    };

    public static final Ros2Codec<Odometry> ODOMETRY = new Ros2Codec<Odometry>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Odometry value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeString(value.getChildFrameId());
            POSE_WITH_COVARIANCE.serialize(writer, value.getPose());
            TWIST_WITH_COVARIANCE.serialize(writer, value.getTwist());
        }

        @Override
        public Odometry deserialize(Ros2CdrReader reader) {
            return Odometry.builder()
                    .header(HEADER.deserialize(reader))
                    .childFrameId(reader.readString())
                    .pose(POSE_WITH_COVARIANCE.deserialize(reader))
                    .twist(TWIST_WITH_COVARIANCE.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<MapMetaData> MAP_META_DATA = new Ros2Codec<MapMetaData>() {
        @Override
        public void serialize(Ros2CdrWriter writer, MapMetaData value) {
            TIME.serialize(writer, value.getMapLoadTime());
            writer.writeFloat(value.getResolution());
            writer.writeUInt32(value.getWidth());
            writer.writeUInt32(value.getHeight());
            POSE.serialize(writer, value.getOrigin());
        }

        @Override
        public MapMetaData deserialize(Ros2CdrReader reader) {
            return MapMetaData.builder()
                    .mapLoadTime(TIME.deserialize(reader))
                    .resolution(reader.readFloat())
                    .width(reader.readUInt32())
                    .height(reader.readUInt32())
                    .origin(POSE.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<Path> PATH = new Ros2Codec<Path>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Path value) {
            HEADER.serialize(writer, value.getHeader());
            writePoseStampedArray(writer, value.getPoses());
        }

        @Override
        public Path deserialize(Ros2CdrReader reader) {
            return Path.builder()
                    .header(HEADER.deserialize(reader))
                    .poses(readPoseStampedArray(reader))
                    .build();
        }
    };

    public static final Ros2Codec<OccupancyGrid> OCCUPANCY_GRID = new Ros2Codec<OccupancyGrid>() {
        @Override
        public void serialize(Ros2CdrWriter writer, OccupancyGrid value) {
            HEADER.serialize(writer, value.getHeader());
            MAP_META_DATA.serialize(writer, value.getInfo());
            writer.writeByteSequence(value.getData());
        }

        @Override
        public OccupancyGrid deserialize(Ros2CdrReader reader) {
            return OccupancyGrid.builder()
                    .header(HEADER.deserialize(reader))
                    .info(MAP_META_DATA.deserialize(reader))
                    .data(reader.readByteSequence())
                    .build();
        }
    };

    public static final Ros2Codec<LaserScan> LASER_SCAN = new Ros2Codec<LaserScan>() {
        @Override
        public void serialize(Ros2CdrWriter writer, LaserScan value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeFloat(value.getAngleMin());
            writer.writeFloat(value.getAngleMax());
            writer.writeFloat(value.getAngleIncrement());
            writer.writeFloat(value.getTimeIncrement());
            writer.writeFloat(value.getScanTime());
            writer.writeFloat(value.getRangeMin());
            writer.writeFloat(value.getRangeMax());
            writer.writeFloatSequence(safeFloatArray(value.getRanges()));
            writer.writeFloatSequence(safeFloatArray(value.getIntensities()));
        }

        @Override
        public LaserScan deserialize(Ros2CdrReader reader) {
            return LaserScan.builder()
                    .header(HEADER.deserialize(reader))
                    .angleMin(reader.readFloat())
                    .angleMax(reader.readFloat())
                    .angleIncrement(reader.readFloat())
                    .timeIncrement(reader.readFloat())
                    .scanTime(reader.readFloat())
                    .rangeMin(reader.readFloat())
                    .rangeMax(reader.readFloat())
                    .ranges(reader.readFloatSequence())
                    .intensities(reader.readFloatSequence())
                    .build();
        }
    };

    public static final Ros2Codec<Range> RANGE = new Ros2Codec<Range>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Range value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeUInt8(value.getRadiationType());
            writer.writeFloat(value.getFieldOfView());
            writer.writeFloat(value.getMinRange());
            writer.writeFloat(value.getMaxRange());
            writer.writeFloat(value.getRange());
        }

        @Override
        public Range deserialize(Ros2CdrReader reader) {
            return Range.builder()
                    .header(HEADER.deserialize(reader))
                    .radiationType(reader.readUInt8())
                    .fieldOfView(reader.readFloat())
                    .minRange(reader.readFloat())
                    .maxRange(reader.readFloat())
                    .range(reader.readFloat())
                    .build();
        }
    };

    public static final Ros2Codec<Joy> JOY = new Ros2Codec<Joy>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Joy value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeFloatSequence(safeFloatArray(value.getAxes()));
            writer.writeInt32Sequence(safeIntArray(value.getButtons()));
        }

        @Override
        public Joy deserialize(Ros2CdrReader reader) {
            return Joy.builder()
                    .header(HEADER.deserialize(reader))
                    .axes(reader.readFloatSequence())
                    .buttons(reader.readInt32Sequence())
                    .build();
        }
    };

    public static final Ros2Codec<JointState> JOINT_STATE = new Ros2Codec<JointState>() {
        @Override
        public void serialize(Ros2CdrWriter writer, JointState value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeStringSequence(safeStringArray(value.getName()));
            writer.writeDoubleSequence(safeDoubleArray(value.getPosition()));
            writer.writeDoubleSequence(safeDoubleArray(value.getVelocity()));
            writer.writeDoubleSequence(safeDoubleArray(value.getEffort()));
        }

        @Override
        public JointState deserialize(Ros2CdrReader reader) {
            return JointState.builder()
                    .header(HEADER.deserialize(reader))
                    .name(reader.readStringSequence())
                    .position(reader.readDoubleSequence())
                    .velocity(reader.readDoubleSequence())
                    .effort(reader.readDoubleSequence())
                    .build();
        }
    };

    public static final Ros2Codec<BatteryState> BATTERY_STATE = new Ros2Codec<BatteryState>() {
        @Override
        public void serialize(Ros2CdrWriter writer, BatteryState value) {
            HEADER.serialize(writer, value.getHeader());
            writer.writeFloat(value.getVoltage());
            writer.writeFloat(value.getTemperature());
            writer.writeFloat(value.getCurrent());
            writer.writeFloat(value.getCharge());
            writer.writeFloat(value.getCapacity());
            writer.writeFloat(value.getDesignCapacity());
            writer.writeFloat(value.getPercentage());
            writer.writeUInt8(value.getPowerSupplyStatus());
            writer.writeUInt8(value.getPowerSupplyHealth());
            writer.writeUInt8(value.getPowerSupplyTechnology());
            writer.writeBool(value.isPresent());
            writer.writeFloatSequence(safeFloatArray(value.getCellVoltage()));
            writer.writeFloatSequence(safeFloatArray(value.getCellTemperature()));
            writer.writeString(value.getLocation() == null ? "" : value.getLocation());
            writer.writeString(value.getSerialNumber() == null ? "" : value.getSerialNumber());
        }

        @Override
        public BatteryState deserialize(Ros2CdrReader reader) {
            return BatteryState.builder()
                    .header(HEADER.deserialize(reader))
                    .voltage(reader.readFloat())
                    .temperature(reader.readFloat())
                    .current(reader.readFloat())
                    .charge(reader.readFloat())
                    .capacity(reader.readFloat())
                    .designCapacity(reader.readFloat())
                    .percentage(reader.readFloat())
                    .powerSupplyStatus(reader.readUInt8())
                    .powerSupplyHealth(reader.readUInt8())
                    .powerSupplyTechnology(reader.readUInt8())
                    .present(reader.readBool())
                    .cellVoltage(reader.readFloatSequence())
                    .cellTemperature(reader.readFloatSequence())
                    .location(reader.readString())
                    .serialNumber(reader.readString())
                    .build();
        }
    };

    public static final Ros2Codec<TFMessage> TF_MESSAGE = new Ros2Codec<TFMessage>() {
        @Override
        public void serialize(Ros2CdrWriter writer, TFMessage value) {
            writeTransformStampedArray(writer, value.getTransforms());
        }

        @Override
        public TFMessage deserialize(Ros2CdrReader reader) {
            return TFMessage.builder()
                    .transforms(readTransformStampedArray(reader))
                    .build();
        }
    };

    private Ros2Codecs() {
    }

    private static void writeFixedDoubleArray(Ros2CdrWriter writer, double[] values, int expectedLength, String fieldName) {
        if (values == null || values.length != expectedLength) {
            throw new IllegalArgumentException(fieldName + " must contain exactly " + expectedLength + " values.");
        }

        for (double value : values) {
            writer.writeDouble(value);
        }
    }

    private static double[] readFixedDoubleArray(Ros2CdrReader reader, int length) {
        double[] values = new double[length];
        for (int i = 0; i < length; i++) {
            values[i] = reader.readDouble();
        }

        return values;
    }

    private static void writePointFieldArray(Ros2CdrWriter writer, PointField[] fields) {
        PointField[] safeFields = fields == null ? new PointField[0] : fields;
        writer.writeUInt32(safeFields.length);
        for (PointField field : safeFields) {
            POINT_FIELD.serialize(writer, field);
        }
    }

    private static PointField[] readPointFieldArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        PointField[] fields = new PointField[(int) length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = POINT_FIELD.deserialize(reader);
        }

        return fields;
    }

    private static void writeMultiArrayDimensionArray(Ros2CdrWriter writer, MultiArrayDimension[] values) {
        MultiArrayDimension[] safeValues = values == null ? new MultiArrayDimension[0] : values;
        writer.writeUInt32(safeValues.length);
        for (MultiArrayDimension value : safeValues) {
            MULTI_ARRAY_DIMENSION.serialize(writer, value);
        }
    }

    private static MultiArrayDimension[] readMultiArrayDimensionArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        MultiArrayDimension[] values = new MultiArrayDimension[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = MULTI_ARRAY_DIMENSION.deserialize(reader);
        }

        return values;
    }

    private static void writePoint32Array(Ros2CdrWriter writer, Point32[] values) {
        Point32[] safeValues = values == null ? new Point32[0] : values;
        writer.writeUInt32(safeValues.length);
        for (Point32 value : safeValues) {
            POINT32.serialize(writer, value);
        }
    }

    private static Point32[] readPoint32Array(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        Point32[] values = new Point32[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = POINT32.deserialize(reader);
        }

        return values;
    }

    private static void writeChannelFloat32Array(Ros2CdrWriter writer, ChannelFloat32[] values) {
        ChannelFloat32[] safeValues = values == null ? new ChannelFloat32[0] : values;
        writer.writeUInt32(safeValues.length);
        for (ChannelFloat32 value : safeValues) {
            CHANNEL_FLOAT32.serialize(writer, value);
        }
    }

    private static ChannelFloat32[] readChannelFloat32Array(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        ChannelFloat32[] values = new ChannelFloat32[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = CHANNEL_FLOAT32.deserialize(reader);
        }

        return values;
    }

    private static float[] safeFloatArray(float[] values) {
        return values == null ? new float[0] : values;
    }

    private static int[] safeIntArray(int[] values) {
        return values == null ? new int[0] : values;
    }

    private static double[] safeDoubleArray(double[] values) {
        return values == null ? new double[0] : values;
    }

    private static String[] safeStringArray(String[] values) {
        return values == null ? new String[0] : values;
    }

    private static void writePoseStampedArray(Ros2CdrWriter writer, PoseStamped[] values) {
        PoseStamped[] safeValues = values == null ? new PoseStamped[0] : values;
        writer.writeUInt32(safeValues.length);
        for (PoseStamped value : safeValues) {
            POSE_STAMPED.serialize(writer, value);
        }
    }

    private static PoseStamped[] readPoseStampedArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        PoseStamped[] values = new PoseStamped[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = POSE_STAMPED.deserialize(reader);
        }

        return values;
    }

    private static void writeTransformStampedArray(Ros2CdrWriter writer, TransformStamped[] values) {
        TransformStamped[] safeValues = values == null ? new TransformStamped[0] : values;
        writer.writeUInt32(safeValues.length);
        for (TransformStamped value : safeValues) {
            TRANSFORM_STAMPED.serialize(writer, value);
        }
    }

    private static TransformStamped[] readTransformStampedArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        TransformStamped[] values = new TransformStamped[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = TRANSFORM_STAMPED.deserialize(reader);
        }

        return values;
    }

    private static void writePoseArray(Ros2CdrWriter writer, Pose[] values) {
        Pose[] safeValues = values == null ? new Pose[0] : values;
        writer.writeUInt32(safeValues.length);
        for (Pose value : safeValues) {
            POSE.serialize(writer, value);
        }
    }

    private static Pose[] readPoseArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        Pose[] values = new Pose[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = POSE.deserialize(reader);
        }

        return values;
    }
}
