package org.indunet.fastproto;

import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.util.CodecUtils;

import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Binary encoder.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
final class Encoder {
    ByteBuffer byteBuffer = new ByteBuffer();
    ByteOrder byteOrder = ByteOrder.LITTLE;
    BitOrder bitOrder = BitOrder.LSB_0;

    public Encoder length(int length) {
        this.byteBuffer = new ByteBuffer(length);

        return this;
    }

    public Encoder defaultByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;

        return this;
    }

    public Encoder defaultBitOrder(BitOrder bitOrder) {
        this.bitOrder = bitOrder;

        return this;
    }

    public Encoder boolType(int byteOffset, int bitOffset, boolean value) {
        this.boolType(byteOffset, bitOffset, this.bitOrder, value);

        return this;
    }

    public Encoder boolType(int byteOffset, int bitOffset, BitOrder bitOrder, boolean value) {
        CodecUtils.boolType(this.byteBuffer, byteOffset, bitOffset, bitOrder, value);

        return this;
    }

    public Encoder writeUInt8(int offset, int... values) {
        IntStream.range(0, values.length)
                        .forEach(i -> CodecUtils.uint8Type(this.byteBuffer, offset + i, values[i]));

        return this;
    }

    public Encoder appendUInt8(int... values) {
        for (int value: values) {
            CodecUtils.uint8Type(this.byteBuffer, this.byteBuffer.getWriteIndex(), value);
        }

        return this;
    }

    public Encoder writeUInt16(int offset, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint16Type(this.byteBuffer, offset + i * UInt16Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder writeUInt16(int offset, ByteOrder byteOrder, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint16Type(this.byteBuffer, offset + i * UInt16Type.SIZE, byteOrder, values[i]));

        return this;
    }

    public Encoder appendUInt16(int... values) {
        this.appendUInt16(this.byteOrder, values);

        return this;
    }

    public Encoder appendUInt16(ByteOrder byteOrder, int... values) {
        for (int value: values) {
            CodecUtils.uint16Type(this.byteBuffer, this.byteBuffer.getWriteIndex(), byteOrder, value);
        }

        return this;
    }

    public Encoder writeUInt32(int offset, long... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint32Type(this.byteBuffer, offset + i * UInt32Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder writeUInt32(int offset, ByteOrder byteOrder, long... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint32Type(this.byteBuffer, offset + i * UInt32Type.SIZE, byteOrder, values[i]));

        return this;
    }

    public Encoder appendUInt32(long... values) {
        this.appendUInt32(this.byteOrder, values);

        return this;
    }

    public Encoder appendUInt32(ByteOrder byteOrder, long... values) {
        for (long value: values) {
            CodecUtils.uint32Type(this.byteBuffer, this.byteBuffer.getWriteIndex(), byteOrder, value);
        }

        return this;
    }

    public Encoder writeUInt64(int offset, BigInteger... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint64Type(this.byteBuffer, offset + i * UInt64Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder writeUInt64(int offset, ByteOrder byteOrder, BigInteger... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint64Type(this.byteBuffer, offset + i * UInt64Type.SIZE, byteOrder, values[i]));

        return this;
    }

    public Encoder appendUInt64(BigInteger... values) {
        this.appendUInt64(this.byteOrder, values);

        return this;
    }

    public Encoder appendUInt64(ByteOrder byteOrder, BigInteger... values) {
        for (BigInteger value: values) {
            CodecUtils.uint64Type(this.byteBuffer, this.byteBuffer.getWriteIndex(), byteOrder, value);
        }

        return this;
    }

    public Encoder writeByte(int offset, byte... values) {
        IntStream.range(0, values.length)
                .forEach(i -> CodecUtils.byteType(this.byteBuffer, offset + i, values[i]));

        return this;
    }

    public Encoder appendByte(byte... values) {
        for (byte value: values) {
            CodecUtils.byteType(this.byteBuffer, this.byteBuffer.getWriteIndex(), value);
        }

        return this;
    }

    public Encoder writeInt8(int offset, byte... values) {
        IntStream.range(0, values.length)
                .forEach(i -> CodecUtils.byteType(this.byteBuffer, offset + i, values[i]));

        return this;
    }

    public Encoder writeInt8(int offset, int... values) {
        IntStream.range(0, values.length)
                .forEach(i -> CodecUtils.int8Type(this.byteBuffer, offset + i, values[i]));

        return this;
    }

    public Encoder appendInt8(byte... values) {
        for (byte value: values) {
            CodecUtils.byteType(this.byteBuffer, this.byteBuffer.getWriteIndex(), value);
        }

        return this;
    }

    public Encoder appendInt8(int... values) {
        for (int value: values) {
            CodecUtils.int8Type(this.byteBuffer, this.byteBuffer.getWriteIndex(), value);
        }

        return this;
    }

    public Encoder writeShort(int offset, short... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(this.byteBuffer, offset + i * Int16Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder writeShort(int offset, ByteOrder byteOrder, short... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(this.byteBuffer, offset + i * Int16Type.SIZE, byteOrder, values[i]));

        return this;
    }

    public Encoder appendShort(short... values) {
        this.appendShort(this.byteOrder, values);

        return this;
    }

    public Encoder appendShort(ByteOrder byteOrder, short... values) {
        for (short value: values) {
            CodecUtils.int16Type(this.byteBuffer, this.byteBuffer.getWriteIndex(), byteOrder, value);
        }

        return this;
    }

    public Encoder writeInt16(int offset, short... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(this.byteBuffer, offset + i * Int16Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder writeInt16(int offset, ByteOrder byteOrder, short... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(this.byteBuffer, offset + i * Int16Type.SIZE, byteOrder, values[i]));

        return this;
    }

    public Encoder writeInt16(int offset, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(this.byteBuffer, offset + i * Int16Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder writeInt16(int offset, ByteOrder byteOrder, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(this.byteBuffer, offset + i * Int16Type.SIZE, byteOrder, values[i]));

        return this;
    }

    public Encoder appendInt16(short... values) {
        this.appendInt16(this.byteOrder, values);

        return this;
    }

    public Encoder appendInt16(ByteOrder byteOrder, short... values) {
        for (short value: values) {
            CodecUtils.int16Type(this.byteBuffer, this.byteBuffer.getWriteIndex(), byteOrder, value);
        }

        return this;
    }

    public Encoder appendInt16(int... values) {
        this.appendInt16(this.byteOrder, values);

        return this;
    }

    public Encoder appendInt16(ByteOrder byteOrder, int... values) {
        for (int value: values) {
            CodecUtils.int16Type(this.byteBuffer, this.byteBuffer.getWriteIndex(), byteOrder, value);
        }

        return this;
    }

    public Encoder writeInt32(int offset, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int32Type(this.byteBuffer, offset + i * Int32Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder writeInt32(int offset, ByteOrder byteOrder, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int32Type(this.byteBuffer, offset + i * Int32Type.SIZE, byteOrder, values[i]));

        return this;
    }

    public Encoder appendInt32(int... values) {
        this.appendInt32(this.byteOrder, values);

        return this;
    }

    public Encoder appendInt32(ByteOrder byteOrder, int... values) {
        for (int value: values) {
            CodecUtils.int32Type(this.byteBuffer, this.byteBuffer.getWriteIndex(), byteOrder, value);
        }

        return this;
    }

    public Encoder writeInt64(int offset, long... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int64Type(this.byteBuffer, offset + i * Int64Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder writeInt64(int offset, ByteOrder byteOrder, long... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int64Type(this.byteBuffer, offset + i * Int64Type.SIZE, byteOrder, values[i]));

        return this;
    }

    public Encoder appendInt64(long... values) {
        this.appendInt64(this.byteOrder, values);

        return this;
    }

    public Encoder appendInt64(ByteOrder byteOrder, long... values) {
        for (long value: values) {
            CodecUtils.int64Type(this.byteBuffer, this.byteBuffer.getWriteIndex(), byteOrder, value);
        }

        return this;
    }

    public Encoder writeFloat(int offset, float... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.floatType(this.byteBuffer, offset + i * FloatType.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder writeFloat(int offset, ByteOrder byteOrder, float... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.floatType(this.byteBuffer, offset + i * FloatType.SIZE, byteOrder, values[i]));

        return this;
    }

    public Encoder appendFloat(float... values) {
        this.appendFloat(this.byteOrder, values);

        return this;
    }

    public Encoder appendFloat(ByteOrder byteOrder, float... values) {
        for (float value: values) {
            CodecUtils.floatType(this.byteBuffer, this.byteBuffer.getWriteIndex(), byteOrder, value);
        }

        return this;
    }

    public Encoder writeDouble(int offset, double... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.doubleType(this.byteBuffer, offset + i * DoubleType.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder writeDouble(int offset, ByteOrder byteOrder, double... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.doubleType(this.byteBuffer, offset + i * DoubleType.SIZE, byteOrder, values[i]));

        return this;
    }

    public Encoder appendDouble(double... values) {
        this.appendDouble(this.byteOrder, values);

        return this;
    }

    public Encoder appendDouble(ByteOrder byteOrder, double... values) {
        for (double value: values) {
            CodecUtils.doubleType(this.byteBuffer, this.byteBuffer.getWriteIndex(), byteOrder, value);
        }

        return this;
    }

    public Encoder align(int alignment) {
        if (alignment <= 0 || (alignment & 0x01) != 0) {
            throw new IllegalArgumentException("alignment must be a positive even number");
        }

        int index = this.byteBuffer.getWriteIndex();
        int after = ((index + (alignment - 1)) & ~(alignment - 1));

        if (after > 0) {
            this.byteBuffer.set(after - 1, (byte) 0);
        } else {
            this.byteBuffer.resetWriteIndex();
        }

        return this;
    }

    public byte[] get() {
        return this.byteBuffer.getBytes();
    }
}
