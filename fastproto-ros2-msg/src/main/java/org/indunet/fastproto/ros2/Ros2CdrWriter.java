package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.io.ByteBufferOutputStream;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * Minimal CDR writer for common ROS2 messages.
 */
public final class Ros2CdrWriter {
    private static final byte[] LITTLE_ENDIAN_ENCAPSULATION = new byte[]{0x00, 0x01, 0x00, 0x00};

    private final ByteBufferOutputStream outputStream = new ByteBufferOutputStream();

    public Ros2CdrWriter() {
        outputStream.writeBytes(LITTLE_ENDIAN_ENCAPSULATION);
    }

    public void align(int alignment) {
        outputStream.align(alignment);
    }

    public void writeInt32(int value) {
        outputStream.align(4);
        outputStream.writeInt32(ByteOrder.LITTLE, value);
    }

    public void writeInt16(int value) {
        outputStream.align(2);
        outputStream.writeInt16(ByteOrder.LITTLE, value);
    }

    public void writeInt64(long value) {
        outputStream.align(8);
        outputStream.writeInt64(ByteOrder.LITTLE, value);
    }

    public void writeUInt32(long value) {
        outputStream.align(4);
        outputStream.writeUInt32(ByteOrder.LITTLE, value);
    }

    public void writeUInt64(BigInteger value) {
        outputStream.align(8);
        outputStream.writeUInt64(ByteOrder.LITTLE, value);
    }

    public void writeUInt16(int value) {
        outputStream.align(2);
        outputStream.writeUInt16(ByteOrder.LITTLE, value);
    }

    public void writeUInt8(int value) {
        outputStream.writeUInt8(value);
    }

    public void writeInt8(int value) {
        outputStream.writeInt8(value);
    }

    public void writeBool(boolean value) {
        outputStream.writeUInt8(value ? 1 : 0);
    }

    public void writeDouble(double value) {
        outputStream.align(8);
        outputStream.writeDouble(ByteOrder.LITTLE, value);
    }

    public void writeFloat(float value) {
        outputStream.align(4);
        outputStream.writeFloat(ByteOrder.LITTLE, value);
    }

    public void writeBytes(byte[] bytes) {
        outputStream.writeBytes(bytes);
    }

    public void writeString(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        writeUInt32(bytes.length + 1L);
        outputStream.writeBytes(bytes);
        outputStream.writeByte((byte) 0);
    }

    public void writeByteSequence(byte[] values) {
        writeUInt32(values.length);
        outputStream.writeBytes(values);
    }

    public void writeDoubleSequence(double[] values) {
        writeUInt32(values.length);
        for (double value : values) {
            writeDouble(value);
        }
    }

    public void writeFloatSequence(float[] values) {
        writeUInt32(values.length);
        for (float value : values) {
            writeFloat(value);
        }
    }

    public void writeInt32Sequence(int[] values) {
        writeUInt32(values.length);
        for (int value : values) {
            writeInt32(value);
        }
    }

    public void writeInt16Sequence(int[] values) {
        writeUInt32(values.length);
        for (int value : values) {
            writeInt16(value);
        }
    }

    public void writeInt64Sequence(long[] values) {
        writeUInt32(values.length);
        for (long value : values) {
            writeInt64(value);
        }
    }

    public void writeUInt16Sequence(int[] values) {
        writeUInt32(values.length);
        for (int value : values) {
            writeUInt16(value);
        }
    }

    public void writeUInt32Sequence(long[] values) {
        writeUInt32(values.length);
        for (long value : values) {
            writeUInt32(value);
        }
    }

    public void writeUInt64Sequence(BigInteger[] values) {
        writeUInt32(values.length);
        for (BigInteger value : values) {
            writeUInt64(value);
        }
    }

    public void writeStringSequence(String[] values) {
        writeUInt32(values.length);
        for (String value : values) {
            writeString(value);
        }
    }

    public byte[] toByteArray() {
        return outputStream.toByteBuffer().toBytes();
    }
}
