package org.indunet.fastproto.ros2.codecs;

import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point32;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Pose;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TransformStamped;
import org.indunet.fastproto.ros2.sensor_msgs.msg.ChannelFloat32;
import org.indunet.fastproto.ros2.sensor_msgs.msg.JoyFeedback;
import org.indunet.fastproto.ros2.sensor_msgs.msg.LaserEcho;
import org.indunet.fastproto.ros2.sensor_msgs.msg.PointField;
import org.indunet.fastproto.ros2.std_msgs.msg.MultiArrayDimension;

import java.math.BigInteger;

final class Ros2CodecSupport {
    private Ros2CodecSupport() {
    }

    static void writeFixedDoubleArray(Ros2CdrWriter writer, double[] values, int expectedLength, String fieldName) {
        if (values == null || values.length != expectedLength) {
            throw new IllegalArgumentException(fieldName + " must contain exactly " + expectedLength + " values.");
        }

        for (double value : values) {
            writer.writeDouble(value);
        }
    }

    static double[] readFixedDoubleArray(Ros2CdrReader reader, int length) {
        double[] values = new double[length];
        for (int i = 0; i < length; i++) {
            values[i] = reader.readDouble();
        }

        return values;
    }

    static void writePointFieldArray(Ros2CdrWriter writer, PointField[] fields) {
        PointField[] safeFields = fields == null ? new PointField[0] : fields;
        writer.writeUInt32(safeFields.length);
        for (PointField field : safeFields) {
            SensorRos2Codecs.POINT_FIELD.serialize(writer, field);
        }
    }

    static PointField[] readPointFieldArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        PointField[] fields = new PointField[(int) length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = SensorRos2Codecs.POINT_FIELD.deserialize(reader);
        }

        return fields;
    }

    static void writeMultiArrayDimensionArray(Ros2CdrWriter writer, MultiArrayDimension[] values) {
        MultiArrayDimension[] safeValues = values == null ? new MultiArrayDimension[0] : values;
        writer.writeUInt32(safeValues.length);
        for (MultiArrayDimension value : safeValues) {
            StdRos2Codecs.MULTI_ARRAY_DIMENSION.serialize(writer, value);
        }
    }

    static MultiArrayDimension[] readMultiArrayDimensionArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        MultiArrayDimension[] values = new MultiArrayDimension[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = StdRos2Codecs.MULTI_ARRAY_DIMENSION.deserialize(reader);
        }

        return values;
    }

    static void writePoint32Array(Ros2CdrWriter writer, Point32[] values) {
        Point32[] safeValues = values == null ? new Point32[0] : values;
        writer.writeUInt32(safeValues.length);
        for (Point32 value : safeValues) {
            GeometryRos2Codecs.POINT32.serialize(writer, value);
        }
    }

    static Point32[] readPoint32Array(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        Point32[] values = new Point32[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = GeometryRos2Codecs.POINT32.deserialize(reader);
        }

        return values;
    }

    static void writePointArray(Ros2CdrWriter writer, Point[] values) {
        Point[] safeValues = values == null ? new Point[0] : values;
        writer.writeUInt32(safeValues.length);
        for (Point value : safeValues) {
            GeometryRos2Codecs.POINT.serialize(writer, value);
        }
    }

    static Point[] readPointArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        Point[] values = new Point[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = GeometryRos2Codecs.POINT.deserialize(reader);
        }

        return values;
    }

    static void writeChannelFloat32Array(Ros2CdrWriter writer, ChannelFloat32[] values) {
        ChannelFloat32[] safeValues = values == null ? new ChannelFloat32[0] : values;
        writer.writeUInt32(safeValues.length);
        for (ChannelFloat32 value : safeValues) {
            SensorRos2Codecs.CHANNEL_FLOAT32.serialize(writer, value);
        }
    }

    static ChannelFloat32[] readChannelFloat32Array(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        ChannelFloat32[] values = new ChannelFloat32[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = SensorRos2Codecs.CHANNEL_FLOAT32.deserialize(reader);
        }

        return values;
    }

    static void writeLaserEchoArray(Ros2CdrWriter writer, LaserEcho[] values) {
        LaserEcho[] safeValues = values == null ? new LaserEcho[0] : values;
        writer.writeUInt32(safeValues.length);
        for (LaserEcho value : safeValues) {
            SensorRos2Codecs.LASER_ECHO.serialize(writer, value);
        }
    }

    static LaserEcho[] readLaserEchoArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        LaserEcho[] values = new LaserEcho[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = SensorRos2Codecs.LASER_ECHO.deserialize(reader);
        }

        return values;
    }

    static void writeJoyFeedbackArray(Ros2CdrWriter writer, JoyFeedback[] values) {
        JoyFeedback[] safeValues = values == null ? new JoyFeedback[0] : values;
        writer.writeUInt32(safeValues.length);
        for (JoyFeedback value : safeValues) {
            SensorRos2Codecs.JOY_FEEDBACK.serialize(writer, value);
        }
    }

    static JoyFeedback[] readJoyFeedbackArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        JoyFeedback[] values = new JoyFeedback[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = SensorRos2Codecs.JOY_FEEDBACK.deserialize(reader);
        }

        return values;
    }

    static float[] safeFloatArray(float[] values) {
        return values == null ? new float[0] : values;
    }

    static int[] safeIntArray(int[] values) {
        return values == null ? new int[0] : values;
    }

    static double[] safeDoubleArray(double[] values) {
        return values == null ? new double[0] : values;
    }

    static String[] safeStringArray(String[] values) {
        return values == null ? new String[0] : values;
    }

    static BigInteger[] safeBigIntegerArray(BigInteger[] values) {
        return values == null ? new BigInteger[0] : values;
    }

    static void writePoseStampedArray(Ros2CdrWriter writer, PoseStamped[] values) {
        PoseStamped[] safeValues = values == null ? new PoseStamped[0] : values;
        writer.writeUInt32(safeValues.length);
        for (PoseStamped value : safeValues) {
            GeometryRos2Codecs.POSE_STAMPED.serialize(writer, value);
        }
    }

    static PoseStamped[] readPoseStampedArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        PoseStamped[] values = new PoseStamped[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = GeometryRos2Codecs.POSE_STAMPED.deserialize(reader);
        }

        return values;
    }

    static void writeTransformStampedArray(Ros2CdrWriter writer, TransformStamped[] values) {
        TransformStamped[] safeValues = values == null ? new TransformStamped[0] : values;
        writer.writeUInt32(safeValues.length);
        for (TransformStamped value : safeValues) {
            GeometryRos2Codecs.TRANSFORM_STAMPED.serialize(writer, value);
        }
    }

    static TransformStamped[] readTransformStampedArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        TransformStamped[] values = new TransformStamped[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = GeometryRos2Codecs.TRANSFORM_STAMPED.deserialize(reader);
        }

        return values;
    }

    static void writePoseArray(Ros2CdrWriter writer, Pose[] values) {
        Pose[] safeValues = values == null ? new Pose[0] : values;
        writer.writeUInt32(safeValues.length);
        for (Pose value : safeValues) {
            GeometryRos2Codecs.POSE.serialize(writer, value);
        }
    }

    static Pose[] readPoseArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        Pose[] values = new Pose[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = GeometryRos2Codecs.POSE.deserialize(reader);
        }

        return values;
    }
}
