package org.indunet.fastproto.ros2.codecs;

import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import org.indunet.fastproto.ros2.Ros2Codec;
import org.indunet.fastproto.ros2.geometry_msgs.msg.*;
import org.indunet.fastproto.ros2.internal.FixedSizeRos2Codec;

public final class GeometryRos2Codecs {
    public static final Ros2Codec<Point> POINT = new FixedSizeRos2Codec<>(Point.class, 8, Point.SIZE);
    public static final Ros2Codec<Point32> POINT32 = new FixedSizeRos2Codec<>(Point32.class, 4, Point32.SIZE);
    public static final Ros2Codec<Pose2D> POSE2D = new FixedSizeRos2Codec<>(Pose2D.class, 8, Pose2D.SIZE);
    public static final Ros2Codec<Quaternion> QUATERNION = new FixedSizeRos2Codec<>(Quaternion.class, 8, Quaternion.SIZE);
    public static final Ros2Codec<Vector3> VECTOR3 = new FixedSizeRos2Codec<>(Vector3.class, 8, Vector3.SIZE);

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
            Ros2CodecSupport.writePoint32Array(writer, value.getPoints());
        }

        @Override
        public Polygon deserialize(Ros2CdrReader reader) {
            return Polygon.builder()
                    .points(Ros2CodecSupport.readPoint32Array(reader))
                    .build();
        }
    };

    public static final Ros2Codec<PointStamped> POINT_STAMPED = new Ros2Codec<PointStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PointStamped value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            POINT.serialize(writer, value.getPoint());
        }

        @Override
        public PointStamped deserialize(Ros2CdrReader reader) {
            return PointStamped.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .point(POINT.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<Vector3Stamped> VECTOR3_STAMPED = new Ros2Codec<Vector3Stamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Vector3Stamped value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            VECTOR3.serialize(writer, value.getVector());
        }

        @Override
        public Vector3Stamped deserialize(Ros2CdrReader reader) {
            return Vector3Stamped.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .vector(VECTOR3.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<QuaternionStamped> QUATERNION_STAMPED = new Ros2Codec<QuaternionStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, QuaternionStamped value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            QUATERNION.serialize(writer, value.getQuaternion());
        }

        @Override
        public QuaternionStamped deserialize(Ros2CdrReader reader) {
            return QuaternionStamped.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
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

    public static final Ros2Codec<AccelWithCovariance> ACCEL_WITH_COVARIANCE = new Ros2Codec<AccelWithCovariance>() {
        @Override
        public void serialize(Ros2CdrWriter writer, AccelWithCovariance value) {
            ACCEL.serialize(writer, value.getAccel());
            Ros2CodecSupport.writeFixedDoubleArray(writer, value.getCovariance(), 36, "covariance");
        }

        @Override
        public AccelWithCovariance deserialize(Ros2CdrReader reader) {
            return AccelWithCovariance.builder()
                    .accel(ACCEL.deserialize(reader))
                    .covariance(Ros2CodecSupport.readFixedDoubleArray(reader, 36))
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
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            POSE.serialize(writer, value.getPose());
        }

        @Override
        public PoseStamped deserialize(Ros2CdrReader reader) {
            return PoseStamped.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .pose(POSE.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<PolygonStamped> POLYGON_STAMPED = new Ros2Codec<PolygonStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PolygonStamped value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            POLYGON.serialize(writer, value.getPolygon());
        }

        @Override
        public PolygonStamped deserialize(Ros2CdrReader reader) {
            return PolygonStamped.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .polygon(POLYGON.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<AccelStamped> ACCEL_STAMPED = new Ros2Codec<AccelStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, AccelStamped value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            ACCEL.serialize(writer, value.getAccel());
        }

        @Override
        public AccelStamped deserialize(Ros2CdrReader reader) {
            return AccelStamped.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .accel(ACCEL.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<AccelWithCovarianceStamped> ACCEL_WITH_COVARIANCE_STAMPED =
            new Ros2Codec<AccelWithCovarianceStamped>() {
                @Override
                public void serialize(Ros2CdrWriter writer, AccelWithCovarianceStamped value) {
                    StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
                    ACCEL_WITH_COVARIANCE.serialize(writer, value.getAccel());
                }

                @Override
                public AccelWithCovarianceStamped deserialize(Ros2CdrReader reader) {
                    return AccelWithCovarianceStamped.builder()
                            .header(StdRos2Codecs.HEADER.deserialize(reader))
                            .accel(ACCEL_WITH_COVARIANCE.deserialize(reader))
                            .build();
                }
            };

    public static final Ros2Codec<TwistStamped> TWIST_STAMPED = new Ros2Codec<TwistStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, TwistStamped value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            TWIST.serialize(writer, value.getTwist());
        }

        @Override
        public TwistStamped deserialize(Ros2CdrReader reader) {
            return TwistStamped.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .twist(TWIST.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<TwistWithCovarianceStamped> TWIST_WITH_COVARIANCE_STAMPED =
            new Ros2Codec<TwistWithCovarianceStamped>() {
                @Override
                public void serialize(Ros2CdrWriter writer, TwistWithCovarianceStamped value) {
                    StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
                    TWIST_WITH_COVARIANCE.serialize(writer, value.getTwist());
                }

                @Override
                public TwistWithCovarianceStamped deserialize(Ros2CdrReader reader) {
                    return TwistWithCovarianceStamped.builder()
                            .header(StdRos2Codecs.HEADER.deserialize(reader))
                            .twist(TWIST_WITH_COVARIANCE.deserialize(reader))
                            .build();
                }
            };

    public static final Ros2Codec<WrenchStamped> WRENCH_STAMPED = new Ros2Codec<WrenchStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, WrenchStamped value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            WRENCH.serialize(writer, value.getWrench());
        }

        @Override
        public WrenchStamped deserialize(Ros2CdrReader reader) {
            return WrenchStamped.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .wrench(WRENCH.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<InertiaStamped> INERTIA_STAMPED = new Ros2Codec<InertiaStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, InertiaStamped value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            INERTIA.serialize(writer, value.getInertia());
        }

        @Override
        public InertiaStamped deserialize(Ros2CdrReader reader) {
            return InertiaStamped.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .inertia(INERTIA.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<TransformStamped> TRANSFORM_STAMPED = new Ros2Codec<TransformStamped>() {
        @Override
        public void serialize(Ros2CdrWriter writer, TransformStamped value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeString(value.getChildFrameId());
            TRANSFORM.serialize(writer, value.getTransform());
        }

        @Override
        public TransformStamped deserialize(Ros2CdrReader reader) {
            return TransformStamped.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .childFrameId(reader.readString())
                    .transform(TRANSFORM.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<PoseWithCovariance> POSE_WITH_COVARIANCE = new Ros2Codec<PoseWithCovariance>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PoseWithCovariance value) {
            POSE.serialize(writer, value.getPose());
            Ros2CodecSupport.writeFixedDoubleArray(writer, value.getCovariance(), 36, "covariance");
        }

        @Override
        public PoseWithCovariance deserialize(Ros2CdrReader reader) {
            return PoseWithCovariance.builder()
                    .pose(POSE.deserialize(reader))
                    .covariance(Ros2CodecSupport.readFixedDoubleArray(reader, 36))
                    .build();
        }
    };

    public static final Ros2Codec<PoseWithCovarianceStamped> POSE_WITH_COVARIANCE_STAMPED =
            new Ros2Codec<PoseWithCovarianceStamped>() {
                @Override
                public void serialize(Ros2CdrWriter writer, PoseWithCovarianceStamped value) {
                    StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
                    POSE_WITH_COVARIANCE.serialize(writer, value.getPose());
                }

                @Override
                public PoseWithCovarianceStamped deserialize(Ros2CdrReader reader) {
                    return PoseWithCovarianceStamped.builder()
                            .header(StdRos2Codecs.HEADER.deserialize(reader))
                            .pose(POSE_WITH_COVARIANCE.deserialize(reader))
                            .build();
                }
            };

    public static final Ros2Codec<PoseArray> POSE_ARRAY = new Ros2Codec<PoseArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PoseArray value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            Ros2CodecSupport.writePoseArray(writer, value.getPoses());
        }

        @Override
        public PoseArray deserialize(Ros2CdrReader reader) {
            return PoseArray.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .poses(Ros2CodecSupport.readPoseArray(reader))
                    .build();
        }
    };

    public static final Ros2Codec<TwistWithCovariance> TWIST_WITH_COVARIANCE = new Ros2Codec<TwistWithCovariance>() {
        @Override
        public void serialize(Ros2CdrWriter writer, TwistWithCovariance value) {
            TWIST.serialize(writer, value.getTwist());
            Ros2CodecSupport.writeFixedDoubleArray(writer, value.getCovariance(), 36, "covariance");
        }

        @Override
        public TwistWithCovariance deserialize(Ros2CdrReader reader) {
            return TwistWithCovariance.builder()
                    .twist(TWIST.deserialize(reader))
                    .covariance(Ros2CodecSupport.readFixedDoubleArray(reader, 36))
                    .build();
        }
    };

    private GeometryRos2Codecs() {
    }
}
