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
class Encoder {
    byte[] bytes;
    protected EndianPolicy endianPolicy = EndianPolicy.LITTLE;

    public Encoder length(int length) {
        this.bytes = new byte[length];

        return this;
    }

    public Encoder defaultEndian(EndianPolicy endianPolicy) {
        this.endianPolicy = endianPolicy;

        return this;
    }

    public Encoder boolType(int byteOffset, int bitOffset, boolean value) {
        CodecUtils.boolType(bytes, byteOffset, bitOffset, value);

        return this;
    }

    public Encoder uint8Type(int offset, int... values) {
        IntStream.range(0, values.length)
                        .forEach(i -> CodecUtils.uint8Type(bytes, offset + i, values[i]));

        return this;
    }

    public Encoder uint16Type(int offset, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint16Type(bytes, offset + i * UInt16Type.SIZE, this.endianPolicy, values[i]));

        return this;
    }

    public Encoder uint16Type(int offset, EndianPolicy endianPolicy, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint16Type(bytes, offset + i * UInt16Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder uint32Type(int offset, long... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint32Type(bytes, offset + i * UInt32Type.SIZE, this.endianPolicy, values[i]));

        return this;
    }

    public Encoder uint32Type(int offset, EndianPolicy endianPolicy, long... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint32Type(bytes, offset + i * UInt32Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder uint64Type(int offset, BigInteger... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint64Type(bytes, offset + i * UInt64Type.SIZE, this.endianPolicy, values[i]));

        return this;
    }

    public Encoder uint64Type(int offset, EndianPolicy endianPolicy, BigInteger... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.uint64Type(bytes, offset + i * UInt64Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder int8Type(int offset, byte... values) {
        IntStream.range(0, values.length)
                .forEach(i -> CodecUtils.byteType(bytes, offset + i, values[i]));

        return this;
    }

    public Encoder int8Type(int offset, int... values) {
        IntStream.range(0, values.length)
                .forEach(i -> CodecUtils.int8Type(bytes, offset + i, values[i]));

        return this;
    }

    public Encoder int16Type(int offset, short... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(bytes, offset + i * Int16Type.SIZE, this.endianPolicy, values[i]));

        return this;
    }

    public Encoder int16Type(int offset, EndianPolicy endianPolicy, short... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(bytes, offset + i * Int16Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder int16Type(int offset, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(bytes, offset + i * Int16Type.SIZE, this.endianPolicy, values[i]));

        return this;
    }

    public Encoder int16Type(int offset, EndianPolicy endianPolicy, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int16Type(bytes, offset + i * Int16Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder int32Type(int offset, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int32Type(bytes, offset + i * Int32Type.SIZE, this.endianPolicy, values[i]));

        return this;
    }

    public Encoder int32Type(int offset, EndianPolicy endianPolicy, int... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int32Type(bytes, offset + i * Int32Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder int64Type(int offset, long... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int64Type(bytes, offset + i * Int64Type.SIZE, this.endianPolicy, values[i]));

        return this;
    }

    public Encoder int64Type(int offset, EndianPolicy endianPolicy, long... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.int64Type(bytes, offset + i * Int64Type.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder floatType(int offset, float... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.floatType(bytes, offset + i * FloatType.SIZE, this.endianPolicy, values[i]));

        return this;
    }

    public Encoder floatType(int offset, EndianPolicy endianPolicy, float... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.floatType(bytes, offset + i * FloatType.SIZE, endianPolicy, values[i]));

        return this;
    }

    public Encoder doubleType(int offset, double... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.doubleType(bytes, offset + i * DoubleType.SIZE, this.endianPolicy, values[i]));

        return this;
    }

    public Encoder doubleType(int offset, EndianPolicy endianPolicy, double... values) {
        IntStream.range(0, values.length)
                .forEach(i ->
                        CodecUtils.doubleType(bytes, offset + i * DoubleType.SIZE, endianPolicy, values[i]));

        return this;
    }

    public byte[] get() {
        return this.bytes;
    }
}
