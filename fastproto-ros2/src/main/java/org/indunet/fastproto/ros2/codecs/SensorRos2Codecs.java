package org.indunet.fastproto.ros2.codecs;

import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import org.indunet.fastproto.ros2.Ros2Codec;
import org.indunet.fastproto.ros2.sensor_msgs.msg.*;

public final class SensorRos2Codecs {
    public static final Ros2Codec<Imu> IMU = new Ros2Codec<Imu>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Imu value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            GeometryRos2Codecs.QUATERNION.serialize(writer, value.getOrientation());
            Ros2CodecSupport.writeFixedDoubleArray(writer, value.getOrientationCovariance(), 9, "orientation_covariance");
            GeometryRos2Codecs.VECTOR3.serialize(writer, value.getAngularVelocity());
            Ros2CodecSupport.writeFixedDoubleArray(writer, value.getAngularVelocityCovariance(), 9, "angular_velocity_covariance");
            GeometryRos2Codecs.VECTOR3.serialize(writer, value.getLinearAcceleration());
            Ros2CodecSupport.writeFixedDoubleArray(writer, value.getLinearAccelerationCovariance(), 9, "linear_acceleration_covariance");
        }

        @Override
        public Imu deserialize(Ros2CdrReader reader) {
            return Imu.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .orientation(GeometryRos2Codecs.QUATERNION.deserialize(reader))
                    .orientationCovariance(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                    .angularVelocity(GeometryRos2Codecs.VECTOR3.deserialize(reader))
                    .angularVelocityCovariance(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                    .linearAcceleration(GeometryRos2Codecs.VECTOR3.deserialize(reader))
                    .linearAccelerationCovariance(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                    .build();
        }
    };

    public static final Ros2Codec<Image> IMAGE = new Ros2Codec<Image>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Image value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
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
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
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
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeString(value.getFormat());
            writer.writeByteSequence(value.getData());
        }

        @Override
        public CompressedImage deserialize(Ros2CdrReader reader) {
            return CompressedImage.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .format(reader.readString())
                    .data(reader.readByteSequence())
                    .build();
        }
    };

    public static final Ros2Codec<ChannelFloat32> CHANNEL_FLOAT32 = new Ros2Codec<ChannelFloat32>() {
        @Override
        public void serialize(Ros2CdrWriter writer, ChannelFloat32 value) {
            writer.writeString(value.getName());
            writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(value.getValues()));
        }

        @Override
        public ChannelFloat32 deserialize(Ros2CdrReader reader) {
            return ChannelFloat32.builder()
                    .name(reader.readString())
                    .values(reader.readFloatSequence())
                    .build();
        }
    };

    public static final Ros2Codec<LaserEcho> LASER_ECHO = new Ros2Codec<LaserEcho>() {
        @Override
        public void serialize(Ros2CdrWriter writer, LaserEcho value) {
            writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(value.getEchoes()));
        }

        @Override
        public LaserEcho deserialize(Ros2CdrReader reader) {
            return LaserEcho.builder()
                    .echoes(reader.readFloatSequence())
                    .build();
        }
    };

    public static final Ros2Codec<JoyFeedback> JOY_FEEDBACK = new Ros2Codec<JoyFeedback>() {
        @Override
        public void serialize(Ros2CdrWriter writer, JoyFeedback value) {
            writer.writeUInt8(value.getType());
            writer.writeUInt8(value.getId());
            writer.writeFloat(value.getIntensity());
        }

        @Override
        public JoyFeedback deserialize(Ros2CdrReader reader) {
            return JoyFeedback.builder()
                    .type(reader.readUInt8())
                    .id(reader.readUInt8())
                    .intensity(reader.readFloat())
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
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeUInt32(value.getHeight());
            writer.writeUInt32(value.getWidth());
            writer.writeString(value.getDistortionModel());
            writer.writeDoubleSequence(value.getD());
            Ros2CodecSupport.writeFixedDoubleArray(writer, value.getK(), 9, "k");
            Ros2CodecSupport.writeFixedDoubleArray(writer, value.getR(), 9, "r");
            Ros2CodecSupport.writeFixedDoubleArray(writer, value.getP(), 12, "p");
            writer.writeUInt32(value.getBinningX());
            writer.writeUInt32(value.getBinningY());
            REGION_OF_INTEREST.serialize(writer, value.getRoi());
        }

        @Override
        public CameraInfo deserialize(Ros2CdrReader reader) {
            return CameraInfo.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .height(reader.readUInt32())
                    .width(reader.readUInt32())
                    .distortionModel(reader.readString())
                    .d(reader.readDoubleSequence())
                    .k(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                    .r(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                    .p(Ros2CodecSupport.readFixedDoubleArray(reader, 12))
                    .binningX(reader.readUInt32())
                    .binningY(reader.readUInt32())
                    .roi(REGION_OF_INTEREST.deserialize(reader))
                    .build();
        }
    };

    public static final Ros2Codec<MagneticField> MAGNETIC_FIELD = new Ros2Codec<MagneticField>() {
        @Override
        public void serialize(Ros2CdrWriter writer, MagneticField value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            GeometryRos2Codecs.VECTOR3.serialize(writer, value.getMagneticField());
            Ros2CodecSupport.writeFixedDoubleArray(writer, value.getMagneticFieldCovariance(), 9, "magnetic_field_covariance");
        }

        @Override
        public MagneticField deserialize(Ros2CdrReader reader) {
            return MagneticField.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .magneticField(GeometryRos2Codecs.VECTOR3.deserialize(reader))
                    .magneticFieldCovariance(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                    .build();
        }
    };

    public static final Ros2Codec<Temperature> TEMPERATURE = new Ros2Codec<Temperature>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Temperature value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeDouble(value.getTemperature());
            writer.writeDouble(value.getVariance());
        }

        @Override
        public Temperature deserialize(Ros2CdrReader reader) {
            return Temperature.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .temperature(reader.readDouble())
                    .variance(reader.readDouble())
                    .build();
        }
    };

    public static final Ros2Codec<FluidPressure> FLUID_PRESSURE = new Ros2Codec<FluidPressure>() {
        @Override
        public void serialize(Ros2CdrWriter writer, FluidPressure value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeDouble(value.getFluidPressure());
            writer.writeDouble(value.getVariance());
        }

        @Override
        public FluidPressure deserialize(Ros2CdrReader reader) {
            return FluidPressure.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .fluidPressure(reader.readDouble())
                    .variance(reader.readDouble())
                    .build();
        }
    };

    public static final Ros2Codec<Illuminance> ILLUMINANCE = new Ros2Codec<Illuminance>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Illuminance value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeDouble(value.getIlluminance());
            writer.writeDouble(value.getVariance());
        }

        @Override
        public Illuminance deserialize(Ros2CdrReader reader) {
            return Illuminance.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .illuminance(reader.readDouble())
                    .variance(reader.readDouble())
                    .build();
        }
    };

    public static final Ros2Codec<RelativeHumidity> RELATIVE_HUMIDITY = new Ros2Codec<RelativeHumidity>() {
        @Override
        public void serialize(Ros2CdrWriter writer, RelativeHumidity value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeDouble(value.getRelativeHumidity());
            writer.writeDouble(value.getVariance());
        }

        @Override
        public RelativeHumidity deserialize(Ros2CdrReader reader) {
            return RelativeHumidity.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .relativeHumidity(reader.readDouble())
                    .variance(reader.readDouble())
                    .build();
        }
    };

    public static final Ros2Codec<PointCloud2> POINT_CLOUD2 = new Ros2Codec<PointCloud2>() {
        @Override
        public void serialize(Ros2CdrWriter writer, PointCloud2 value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeUInt32(value.getHeight());
            writer.writeUInt32(value.getWidth());
            Ros2CodecSupport.writePointFieldArray(writer, value.getFields());
            writer.writeBool(value.isBigendian());
            writer.writeUInt32(value.getPointStep());
            writer.writeUInt32(value.getRowStep());
            writer.writeByteSequence(value.getData());
            writer.writeBool(value.isDense());
        }

        @Override
        public PointCloud2 deserialize(Ros2CdrReader reader) {
            return PointCloud2.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .height(reader.readUInt32())
                    .width(reader.readUInt32())
                    .fields(Ros2CodecSupport.readPointFieldArray(reader))
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
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            Ros2CodecSupport.writePoint32Array(writer, value.getPoints());
            Ros2CodecSupport.writeChannelFloat32Array(writer, value.getChannels());
        }

        @Override
        public PointCloud deserialize(Ros2CdrReader reader) {
            return PointCloud.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .points(Ros2CodecSupport.readPoint32Array(reader))
                    .channels(Ros2CodecSupport.readChannelFloat32Array(reader))
                    .build();
        }
    };

    public static final Ros2Codec<LaserScan> LASER_SCAN = new Ros2Codec<LaserScan>() {
        @Override
        public void serialize(Ros2CdrWriter writer, LaserScan value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeFloat(value.getAngleMin());
            writer.writeFloat(value.getAngleMax());
            writer.writeFloat(value.getAngleIncrement());
            writer.writeFloat(value.getTimeIncrement());
            writer.writeFloat(value.getScanTime());
            writer.writeFloat(value.getRangeMin());
            writer.writeFloat(value.getRangeMax());
            writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(value.getRanges()));
            writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(value.getIntensities()));
        }

        @Override
        public LaserScan deserialize(Ros2CdrReader reader) {
            return LaserScan.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
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

    public static final Ros2Codec<MultiEchoLaserScan> MULTI_ECHO_LASER_SCAN = new Ros2Codec<MultiEchoLaserScan>() {
        @Override
        public void serialize(Ros2CdrWriter writer, MultiEchoLaserScan value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeFloat(value.getAngleMin());
            writer.writeFloat(value.getAngleMax());
            writer.writeFloat(value.getAngleIncrement());
            writer.writeFloat(value.getTimeIncrement());
            writer.writeFloat(value.getScanTime());
            writer.writeFloat(value.getRangeMin());
            writer.writeFloat(value.getRangeMax());
            Ros2CodecSupport.writeLaserEchoArray(writer, value.getRanges());
            Ros2CodecSupport.writeLaserEchoArray(writer, value.getIntensities());
        }

        @Override
        public MultiEchoLaserScan deserialize(Ros2CdrReader reader) {
            return MultiEchoLaserScan.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .angleMin(reader.readFloat())
                    .angleMax(reader.readFloat())
                    .angleIncrement(reader.readFloat())
                    .timeIncrement(reader.readFloat())
                    .scanTime(reader.readFloat())
                    .rangeMin(reader.readFloat())
                    .rangeMax(reader.readFloat())
                    .ranges(Ros2CodecSupport.readLaserEchoArray(reader))
                    .intensities(Ros2CodecSupport.readLaserEchoArray(reader))
                    .build();
        }
    };

    public static final Ros2Codec<Range> RANGE = new Ros2Codec<Range>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Range value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeUInt8(value.getRadiationType());
            writer.writeFloat(value.getFieldOfView());
            writer.writeFloat(value.getMinRange());
            writer.writeFloat(value.getMaxRange());
            writer.writeFloat(value.getRange());
        }

        @Override
        public Range deserialize(Ros2CdrReader reader) {
            return Range.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .radiationType(reader.readUInt8())
                    .fieldOfView(reader.readFloat())
                    .minRange(reader.readFloat())
                    .maxRange(reader.readFloat())
                    .range(reader.readFloat())
                    .build();
        }
    };

    public static final Ros2Codec<NavSatFix> NAV_SAT_FIX = new Ros2Codec<NavSatFix>() {
        @Override
        public void serialize(Ros2CdrWriter writer, NavSatFix value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            NAV_SAT_STATUS.serialize(writer, value.getStatus());
            writer.writeDouble(value.getLatitude());
            writer.writeDouble(value.getLongitude());
            writer.writeDouble(value.getAltitude());
            Ros2CodecSupport.writeFixedDoubleArray(writer, value.getPositionCovariance(), 9, "position_covariance");
            writer.writeUInt8(value.getPositionCovarianceType());
        }

        @Override
        public NavSatFix deserialize(Ros2CdrReader reader) {
            return NavSatFix.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .status(NAV_SAT_STATUS.deserialize(reader))
                    .latitude(reader.readDouble())
                    .longitude(reader.readDouble())
                    .altitude(reader.readDouble())
                    .positionCovariance(Ros2CodecSupport.readFixedDoubleArray(reader, 9))
                    .positionCovarianceType(reader.readUInt8())
                    .build();
        }
    };

    public static final Ros2Codec<Joy> JOY = new Ros2Codec<Joy>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Joy value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(value.getAxes()));
            writer.writeInt32Sequence(Ros2CodecSupport.safeIntArray(value.getButtons()));
        }

        @Override
        public Joy deserialize(Ros2CdrReader reader) {
            return Joy.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .axes(reader.readFloatSequence())
                    .buttons(reader.readInt32Sequence())
                    .build();
        }
    };

    public static final Ros2Codec<JoyFeedbackArray> JOY_FEEDBACK_ARRAY = new Ros2Codec<JoyFeedbackArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, JoyFeedbackArray value) {
            Ros2CodecSupport.writeJoyFeedbackArray(writer, value.getArray());
        }

        @Override
        public JoyFeedbackArray deserialize(Ros2CdrReader reader) {
            return JoyFeedbackArray.builder()
                    .array(Ros2CodecSupport.readJoyFeedbackArray(reader))
                    .build();
        }
    };

    public static final Ros2Codec<JointState> JOINT_STATE = new Ros2Codec<JointState>() {
        @Override
        public void serialize(Ros2CdrWriter writer, JointState value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            writer.writeStringSequence(Ros2CodecSupport.safeStringArray(value.getName()));
            writer.writeDoubleSequence(Ros2CodecSupport.safeDoubleArray(value.getPosition()));
            writer.writeDoubleSequence(Ros2CodecSupport.safeDoubleArray(value.getVelocity()));
            writer.writeDoubleSequence(Ros2CodecSupport.safeDoubleArray(value.getEffort()));
        }

        @Override
        public JointState deserialize(Ros2CdrReader reader) {
            return JointState.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
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
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
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
            writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(value.getCellVoltage()));
            writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(value.getCellTemperature()));
            writer.writeString(value.getLocation() == null ? "" : value.getLocation());
            writer.writeString(value.getSerialNumber() == null ? "" : value.getSerialNumber());
        }

        @Override
        public BatteryState deserialize(Ros2CdrReader reader) {
            return BatteryState.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
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

    public static final Ros2Codec<TimeReference> TIME_REFERENCE = new Ros2Codec<TimeReference>() {
        @Override
        public void serialize(Ros2CdrWriter writer, TimeReference value) {
            StdRos2Codecs.HEADER.serialize(writer, value.getHeader());
            BuiltinRos2Codecs.TIME.serialize(writer, value.getTimeRef());
            writer.writeString(value.getSource() == null ? "" : value.getSource());
        }

        @Override
        public TimeReference deserialize(Ros2CdrReader reader) {
            return TimeReference.builder()
                    .header(StdRos2Codecs.HEADER.deserialize(reader))
                    .timeRef(BuiltinRos2Codecs.TIME.deserialize(reader))
                    .source(reader.readString())
                    .build();
        }
    };

    private SensorRos2Codecs() {
    }

}
