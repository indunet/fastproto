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

    public Encoder length(int length) {
        this.byteBuffer = new ByteBuffer(length);

        return this;
    }

    public Encoder defaultEndian(ByteOrder endianPolicy) {
        this.byteOrder = endianPolicy;

        return this;
    }

    public Encoder boolType(int byteOffset, int bitOffset, boolean value) {
        CodecUtils.boolType(this.byteBuffer, byteOffset, bitOffset, value);

        return this;
    }

    public Encoder uint8Type(int offset, int... values) {
        IntStream.range(0, values.length)
                        .forEach(i -> CodecUtils.uint8Type(this.byteBuffer, offset + i, values[i]));

        return this;
    }

    public Encoder uint16Type(int offset, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint16Type(this.byteBuffer, offset + i * UInt16Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder uint16Type(int offset, ByteOrder endianPolicy, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint16Type(this.byteBuffer, offset + i * UInt16Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder uint32Type(int offset, long... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint32Type(this.byteBuffer, offset + i * UInt32Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder uint32Type(int offset, ByteOrder endianPolicy, long... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint32Type(this.byteBuffer, offset + i * UInt32Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder uint64Type(int offset, BigInteger... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint64Type(this.byteBuffer, offset + i * UInt64Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder uint64Type(int offset, ByteOrder endianPolicy, BigInteger... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint64Type(this.byteBuffer, offset + i * UInt64Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder int8Type(int offset, byte... values) {
        IntStream.range(0, values.length)
                .forEach(i -> CodecUtils.byteType(this.byteBuffer, offset + i, values[i]));

        return this;
    }

    public Encoder int8Type(int offset, int... values) {
        IntStream.range(0, values.length)
                .forEach(i -> CodecUtils.int8Type(this.byteBuffer, offset + i, values[i]));

        return this;
    }

    public Encoder int16Type(int offset, short... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(this.byteBuffer, offset + i * Int16Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder int16Type(int offset, ByteOrder endianPolicy, short... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(this.byteBuffer, offset + i * Int16Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder int16Type(int offset, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(this.byteBuffer, offset + i * Int16Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder int16Type(int offset, ByteOrder endianPolicy, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(this.byteBuffer, offset + i * Int16Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder int32Type(int offset, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int32Type(this.byteBuffer, offset + i * Int32Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder int32Type(int offset, ByteOrder endianPolicy, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int32Type(this.byteBuffer, offset + i * Int32Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder int64Type(int offset, long... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int64Type(this.byteBuffer, offset + i * Int64Type.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder int64Type(int offset, ByteOrder endianPolicy, long... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int64Type(this.byteBuffer, offset + i * Int64Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder floatType(int offset, float... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.floatType(this.byteBuffer, offset + i * FloatType.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder floatType(int offset, ByteOrder endianPolicy, float... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.floatType(this.byteBuffer, offset + i * FloatType.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder doubleType(int offset, double... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.doubleType(this.byteBuffer, offset + i * DoubleType.SIZE, this.byteOrder, values[i]));

        return this;
    }

    public Encoder doubleType(int offset, ByteOrder endianPolicy, double... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.doubleType(this.byteBuffer, offset + i * DoubleType.SIZE, endianPolicy, values[i]));

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
