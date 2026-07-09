package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.io.ByteBufferInputStream;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Minimal CDR reader for common ROS2 messages.
 */
public final class Ros2CdrReader {
    private static final byte[] LITTLE_ENDIAN_ENCAPSULATION = new byte[]{0x00, 0x01, 0x00, 0x00};

    private final ByteBufferInputStream inputStream;

    public Ros2CdrReader(byte[] bytes) {
        this.inputStream = new ByteBufferInputStream(bytes);
        byte[] encapsulation = this.inputStream.readBytes(4);

        if (!Arrays.equals(LITTLE_ENDIAN_ENCAPSULATION, encapsulation)) {
            throw new IllegalArgumentException("Unsupported ROS2 CDR encapsulation header.");
        }
    }

    public void align(int alignment) {
        inputStream.align(alignment);
    }

    public int readInt32() {
        inputStream.align(4);
        return inputStream.readInt32(ByteOrder.LITTLE);
    }

    public int readInt16() {
        inputStream.align(2);
        return inputStream.readInt16(ByteOrder.LITTLE);
    }

    public long readInt64() {
        inputStream.align(8);
        return inputStream.readInt64(ByteOrder.LITTLE);
    }

    public long readUInt32() {
        inputStream.align(4);
        return inputStream.readUInt32(ByteOrder.LITTLE);
    }

    public BigInteger readUInt64() {
        inputStream.align(8);
        return inputStream.readUInt64(ByteOrder.LITTLE);
    }

    public int readUInt16() {
        inputStream.align(2);
        return inputStream.readUInt16(ByteOrder.LITTLE);
    }

    public int readUInt8() {
        return inputStream.readUInt8();
    }

    public int readInt8() {
        return inputStream.readInt8();
    }

    public boolean readBool() {
        return readUInt8() != 0;
    }

    public double readDouble() {
        inputStream.align(8);
        return inputStream.readDouble(ByteOrder.LITTLE);
    }

    public float readFloat() {
        inputStream.align(4);
        return inputStream.readFloat(ByteOrder.LITTLE);
    }

    public byte[] readBytes(int length) {
        return inputStream.readBytes(length);
    }

    public String readString() {
        long length = readUInt32();
        if (length <= 0 || length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid ROS2 string length: " + length);
        }

        byte[] bytes = inputStream.readBytes((int) length);
        if (bytes[bytes.length - 1] != 0) {
            throw new IllegalArgumentException("ROS2 string is missing trailing null terminator.");
        }

        return new String(bytes, 0, bytes.length - 1, StandardCharsets.UTF_8);
    }

    public byte[] readByteSequence() {
        long length = readUInt32();
        if (length < 0 || length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid ROS2 byte sequence length: " + length);
        }

        return inputStream.readBytes((int) length);
    }

    public double[] readDoubleSequence() {
        long length = readUInt32();
        if (length < 0 || length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid ROS2 double sequence length: " + length);
        }

        double[] values = new double[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = readDouble();
        }

        return values;
    }

    public float[] readFloatSequence() {
        long length = readUInt32();
        if (length < 0 || length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid ROS2 float sequence length: " + length);
        }

        float[] values = new float[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = readFloat();
        }

        return values;
    }

    public int[] readInt32Sequence() {
        long length = readUInt32();
        if (length < 0 || length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid ROS2 int32 sequence length: " + length);
        }

        int[] values = new int[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = readInt32();
        }

        return values;
    }

    public int[] readInt16Sequence() {
        long length = readUInt32();
        if (length < 0 || length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid ROS2 int16 sequence length: " + length);
        }

        int[] values = new int[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = readInt16();
        }

        return values;
    }

    public long[] readInt64Sequence() {
        long length = readUInt32();
        if (length < 0 || length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid ROS2 int64 sequence length: " + length);
        }

        long[] values = new long[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = readInt64();
        }

        return values;
    }

    public int[] readUInt16Sequence() {
        long length = readUInt32();
        if (length < 0 || length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid ROS2 uint16 sequence length: " + length);
        }

        int[] values = new int[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = readUInt16();
        }

        return values;
    }

    public long[] readUInt32Sequence() {
        long length = readUInt32();
        if (length < 0 || length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid ROS2 uint32 sequence length: " + length);
        }

        long[] values = new long[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = readUInt32();
        }

        return values;
    }

    public BigInteger[] readUInt64Sequence() {
        long length = readUInt32();
        if (length < 0 || length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid ROS2 uint64 sequence length: " + length);
        }

        BigInteger[] values = new BigInteger[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = readUInt64();
        }

        return values;
    }

    public String[] readStringSequence() {
        long length = readUInt32();
        if (length < 0 || length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid ROS2 string sequence length: " + length);
        }

        String[] values = new String[(int) length];
        for (int i = 0; i < values.length; i++) {
            values[i] = readString();
        }

        return values;
    }
}
