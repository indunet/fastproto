package org.indunet.fastproto.ros2.codecs;

import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import org.indunet.fastproto.ros2.diagnostic_msgs.msg.DiagnosticStatus;
import org.indunet.fastproto.ros2.diagnostic_msgs.msg.KeyValue;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point32;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Pose;
import org.indunet.fastproto.ros2.geometry_msgs.msg.PoseStamped;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Point;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Transform;
import org.indunet.fastproto.ros2.geometry_msgs.msg.Twist;
import org.indunet.fastproto.ros2.geometry_msgs.msg.TransformStamped;
import org.indunet.fastproto.ros2.nav_msgs.msg.TrajectoryPoint;
import org.indunet.fastproto.ros2.shape_msgs.msg.MeshTriangle;
import org.indunet.fastproto.ros2.sensor_msgs.msg.ChannelFloat32;
import org.indunet.fastproto.ros2.sensor_msgs.msg.JoyFeedback;
import org.indunet.fastproto.ros2.sensor_msgs.msg.LaserEcho;
import org.indunet.fastproto.ros2.sensor_msgs.msg.PointField;
import org.indunet.fastproto.ros2.std_msgs.msg.ColorRGBA;
import org.indunet.fastproto.ros2.std_msgs.msg.MultiArrayDimension;
import org.indunet.fastproto.ros2.trajectory_msgs.msg.JointTrajectoryPoint;
import org.indunet.fastproto.ros2.trajectory_msgs.msg.MultiDOFJointTrajectoryPoint;
import org.indunet.fastproto.ros2.visualization_msgs.msg.Marker;
import org.indunet.fastproto.ros2.visualization_msgs.msg.UVCoordinate;

import java.math.BigInteger;

public final class Ros2CodecSupport {
    private Ros2CodecSupport() {
    }

    public static void writeFixedDoubleArray(Ros2CdrWriter writer, double[] values, int expectedLength, String fieldName) {
        if (values == null || values.length != expectedLength) {
            throw new IllegalArgumentException(fieldName + " must contain exactly " + expectedLength + " values.");
        }

        for (double value : values) {
            writer.writeDouble(value);
        }
    }

    public static double[] readFixedDoubleArray(Ros2CdrReader reader, int length) {
        double[] values = new double[length];
        for (int i = 0; i < length; i++) {
            values[i] = reader.readDouble();
        }

        return values;
    }

    public static void writeFixedUInt32Array(Ros2CdrWriter writer, long[] values, int expectedLength, String fieldName) {
        if (values == null || values.length != expectedLength) {
            throw new IllegalArgumentException(fieldName + " must contain exactly " + expectedLength + " values.");
        }

        for (long value : values) {
            writer.writeUInt32(value);
        }
    }

    public static long[] readFixedUInt32Array(Ros2CdrReader reader, int length) {
        long[] values = new long[length];
        for (int i = 0; i < length; i++) {
            values[i] = reader.readUInt32();
        }

        return values;
    }

    public static void writeKeyValueArray(Ros2CdrWriter writer, KeyValue[] values) {
        KeyValue[] safeValues = values == null ? new KeyValue[0] : values;
        writer.writeUInt32(safeValues.length);
        for (KeyValue value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static KeyValue[] readKeyValueArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        KeyValue[] values = new KeyValue[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = KeyValue.readFrom(reader);
        }

        return values;
    }

    public static void writeDiagnosticStatusArray(Ros2CdrWriter writer, DiagnosticStatus[] values) {
        DiagnosticStatus[] safeValues = values == null ? new DiagnosticStatus[0] : values;
        writer.writeUInt32(safeValues.length);
        for (DiagnosticStatus value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static DiagnosticStatus[] readDiagnosticStatusArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        DiagnosticStatus[] values = new DiagnosticStatus[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = DiagnosticStatus.readFrom(reader);
        }

        return values;
    }

    public static void writePointFieldArray(Ros2CdrWriter writer, PointField[] fields) {
        PointField[] safeFields = fields == null ? new PointField[0] : fields;
        writer.writeUInt32(safeFields.length);
        for (PointField field : safeFields) {
            field.writeTo(writer);
        }
    }

    public static PointField[] readPointFieldArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        PointField[] fields = new PointField[(int) length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = PointField.readFrom(reader);
        }

        return fields;
    }

    public static void writeMultiArrayDimensionArray(Ros2CdrWriter writer, MultiArrayDimension[] values) {
        MultiArrayDimension[] safeValues = values == null ? new MultiArrayDimension[0] : values;
        writer.writeUInt32(safeValues.length);
        for (MultiArrayDimension value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static MultiArrayDimension[] readMultiArrayDimensionArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        MultiArrayDimension[] values = new MultiArrayDimension[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = MultiArrayDimension.readFrom(reader);
        }

        return values;
    }

    public static void writePoint32Array(Ros2CdrWriter writer, Point32[] values) {
        Point32[] safeValues = values == null ? new Point32[0] : values;
        writer.writeUInt32(safeValues.length);
        for (Point32 value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static Point32[] readPoint32Array(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        Point32[] values = new Point32[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = Point32.readFrom(reader);
        }

        return values;
    }

    public static void writePointArray(Ros2CdrWriter writer, Point[] values) {
        Point[] safeValues = values == null ? new Point[0] : values;
        writer.writeUInt32(safeValues.length);
        for (Point value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static Point[] readPointArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        Point[] values = new Point[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = Point.readFrom(reader);
        }

        return values;
    }

    public static void writeTransformArray(Ros2CdrWriter writer, Transform[] values) {
        Transform[] safeValues = values == null ? new Transform[0] : values;
        writer.writeUInt32(safeValues.length);
        for (Transform value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static Transform[] readTransformArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        Transform[] values = new Transform[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = Transform.readFrom(reader);
        }

        return values;
    }

    public static void writeTwistArray(Ros2CdrWriter writer, Twist[] values) {
        Twist[] safeValues = values == null ? new Twist[0] : values;
        writer.writeUInt32(safeValues.length);
        for (Twist value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static Twist[] readTwistArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        Twist[] values = new Twist[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = Twist.readFrom(reader);
        }

        return values;
    }

    public static void writeChannelFloat32Array(Ros2CdrWriter writer, ChannelFloat32[] values) {
        ChannelFloat32[] safeValues = values == null ? new ChannelFloat32[0] : values;
        writer.writeUInt32(safeValues.length);
        for (ChannelFloat32 value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static ChannelFloat32[] readChannelFloat32Array(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        ChannelFloat32[] values = new ChannelFloat32[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = ChannelFloat32.readFrom(reader);
        }

        return values;
    }

    public static void writeLaserEchoArray(Ros2CdrWriter writer, LaserEcho[] values) {
        LaserEcho[] safeValues = values == null ? new LaserEcho[0] : values;
        writer.writeUInt32(safeValues.length);
        for (LaserEcho value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static LaserEcho[] readLaserEchoArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        LaserEcho[] values = new LaserEcho[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = LaserEcho.readFrom(reader);
        }

        return values;
    }

    public static void writeJoyFeedbackArray(Ros2CdrWriter writer, JoyFeedback[] values) {
        JoyFeedback[] safeValues = values == null ? new JoyFeedback[0] : values;
        writer.writeUInt32(safeValues.length);
        for (JoyFeedback value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static JoyFeedback[] readJoyFeedbackArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        JoyFeedback[] values = new JoyFeedback[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = JoyFeedback.readFrom(reader);
        }

        return values;
    }

    public static float[] safeFloatArray(float[] values) {
        return values == null ? new float[0] : values;
    }

    public static int[] safeIntArray(int[] values) {
        return values == null ? new int[0] : values;
    }

    public static double[] safeDoubleArray(double[] values) {
        return values == null ? new double[0] : values;
    }

    public static String[] safeStringArray(String[] values) {
        return values == null ? new String[0] : values;
    }

    public static BigInteger[] safeBigIntegerArray(BigInteger[] values) {
        return values == null ? new BigInteger[0] : values;
    }

    public static void writePoseStampedArray(Ros2CdrWriter writer, PoseStamped[] values) {
        PoseStamped[] safeValues = values == null ? new PoseStamped[0] : values;
        writer.writeUInt32(safeValues.length);
        for (PoseStamped value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static PoseStamped[] readPoseStampedArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        PoseStamped[] values = new PoseStamped[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = PoseStamped.readFrom(reader);
        }

        return values;
    }

    public static void writeTrajectoryPointArray(Ros2CdrWriter writer, TrajectoryPoint[] values) {
        TrajectoryPoint[] safeValues = values == null ? new TrajectoryPoint[0] : values;
        writer.writeUInt32(safeValues.length);
        for (TrajectoryPoint value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static TrajectoryPoint[] readTrajectoryPointArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        TrajectoryPoint[] values = new TrajectoryPoint[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = TrajectoryPoint.readFrom(reader);
        }

        return values;
    }

    public static void writeJointTrajectoryPointArray(Ros2CdrWriter writer, JointTrajectoryPoint[] values) {
        JointTrajectoryPoint[] safeValues = values == null ? new JointTrajectoryPoint[0] : values;
        writer.writeUInt32(safeValues.length);
        for (JointTrajectoryPoint value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static JointTrajectoryPoint[] readJointTrajectoryPointArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        JointTrajectoryPoint[] values = new JointTrajectoryPoint[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = JointTrajectoryPoint.readFrom(reader);
        }

        return values;
    }

    public static void writeMultiDOFJointTrajectoryPointArray(Ros2CdrWriter writer, MultiDOFJointTrajectoryPoint[] values) {
        MultiDOFJointTrajectoryPoint[] safeValues = values == null ? new MultiDOFJointTrajectoryPoint[0] : values;
        writer.writeUInt32(safeValues.length);
        for (MultiDOFJointTrajectoryPoint value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static MultiDOFJointTrajectoryPoint[] readMultiDOFJointTrajectoryPointArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        MultiDOFJointTrajectoryPoint[] values = new MultiDOFJointTrajectoryPoint[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = MultiDOFJointTrajectoryPoint.readFrom(reader);
        }

        return values;
    }

    public static void writeTransformStampedArray(Ros2CdrWriter writer, TransformStamped[] values) {
        TransformStamped[] safeValues = values == null ? new TransformStamped[0] : values;
        writer.writeUInt32(safeValues.length);
        for (TransformStamped value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static TransformStamped[] readTransformStampedArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        TransformStamped[] values = new TransformStamped[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = TransformStamped.readFrom(reader);
        }

        return values;
    }

    public static void writePoseArray(Ros2CdrWriter writer, Pose[] values) {
        Pose[] safeValues = values == null ? new Pose[0] : values;
        writer.writeUInt32(safeValues.length);
        for (Pose value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static Pose[] readPoseArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        Pose[] values = new Pose[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = Pose.readFrom(reader);
        }

        return values;
    }

    public static void writeMeshTriangleArray(Ros2CdrWriter writer, MeshTriangle[] values) {
        MeshTriangle[] safeValues = values == null ? new MeshTriangle[0] : values;
        writer.writeUInt32(safeValues.length);
        for (MeshTriangle value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static MeshTriangle[] readMeshTriangleArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        MeshTriangle[] values = new MeshTriangle[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = MeshTriangle.readFrom(reader);
        }

        return values;
    }

    public static void writeColorRGBAArray(Ros2CdrWriter writer, ColorRGBA[] values) {
        ColorRGBA[] safeValues = values == null ? new ColorRGBA[0] : values;
        writer.writeUInt32(safeValues.length);
        for (ColorRGBA value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static ColorRGBA[] readColorRGBAArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        ColorRGBA[] values = new ColorRGBA[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = ColorRGBA.readFrom(reader);
        }

        return values;
    }

    public static void writeUVCoordinateArray(Ros2CdrWriter writer, UVCoordinate[] values) {
        UVCoordinate[] safeValues = values == null ? new UVCoordinate[0] : values;
        writer.writeUInt32(safeValues.length);
        for (UVCoordinate value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static UVCoordinate[] readUVCoordinateArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        UVCoordinate[] values = new UVCoordinate[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = UVCoordinate.readFrom(reader);
        }

        return values;
    }

    public static void writeMarkerArray(Ros2CdrWriter writer, Marker[] values) {
        Marker[] safeValues = values == null ? new Marker[0] : values;
        writer.writeUInt32(safeValues.length);
        for (Marker value : safeValues) {
            value.writeTo(writer);
        }
    }

    public static Marker[] readMarkerArray(Ros2CdrReader reader) {
        long length = reader.readUInt32();
        Marker[] values = new Marker[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = Marker.readFrom(reader);
        }

        return values;
    }
}
