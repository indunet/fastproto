package org.indunet.fastproto;

import lombok.val;
import org.indunet.fastproto.annotation.UInt16Type;
import org.indunet.fastproto.io.ByteBufferOutputStream;

import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Binary encoder which supplies method chain.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public final class Encoder {
    ByteBufferOutputStream outputStream;
    ByteOrder defaultByteOrder = ByteOrder.LITTLE;
    BitOrder defaultBitOrder = BitOrder.LSB_0;

    Encoder() {
        this.outputStream = new ByteBufferOutputStream();
    }

    Encoder(byte[] bytes) {
        this.outputStream = new ByteBufferOutputStream(bytes);
    }

    /**
     * Modify default byte order, FastProto uses little endian by default if not specified.
     *
     * @param order Byte order
     * @return this
     */
    public Encoder defaultByteOrder(ByteOrder order) {
        this.defaultByteOrder = order;

        return this;
    }

    /**
     * Modify default bit order, FastProto uses lsb_0 by default if not specified.
     *
     * @param order Bit order
     * @return this
     */
    public Encoder defaultBitOrder(BitOrder order) {
        this.defaultBitOrder = order;

        return this;
    }

    /**
     * Encode boolean values into binary data.
     *
     * @param values Boolean values.
     * @return this
     */
    public Encoder appendBool(boolean... values) {
        return this.appendBool(defaultBitOrder, values);
    }

    /**
     * Encode boolean values into binary data.
     *
     * @param order The bit order of the boolean values in binary data.
     * @param values Boolean values.
     * @return this
     */
    public Encoder appendBool(BitOrder order, boolean... values) {
        for (val value: values) {
            this.outputStream.writeBool(order, value);
        }

        return this;
    }

    /**
     * Encode boolean values into binary data.
     *
     * @param byteOffset The byte offset of the byte values in binary data.
     * @param bitOffset The bit offset of the byte values in binary data.
     * @param values Boolean values.
     * @return this
     */
    public Encoder writeBool(int byteOffset, int bitOffset, boolean... values) {
        return this.writeBool(byteOffset, bitOffset, defaultBitOrder, values);
    }

    /**
     * Encode byte values into binary data.
     *
     * @param byteOffset The byte offset of the byte values in binary data.
     * @param bitOffset The bit offset of the byte values in binary data.
     * @param order The bit order of the byte values in binary data.
     * @param values Boolean values.
     * @return this
     */
    public Encoder writeBool(int byteOffset, int bitOffset, BitOrder order, boolean... values) {
        for (val value: values) {
            this.outputStream.writeBool(byteOffset, bitOffset, order, value);
        }

        return this;
    }

    /**
     * Encode byte values into binary data.
     *
     * @param values Byte values.
     * @return this
     */
    public Encoder appendByte(byte... values) {
        for (val value: values) {
            this.outputStream.writeByte(value);
        }

        return this;
    }

    /**
     * Encode byte values into binary data.
     *
     * @param offset The byte offset of the byte values in binary data.
     * @param values Byte values.
     * @return this
     */
    public Encoder writeByte(int offset, byte... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeByte(o + i, values[i]);
        });

        return this;
    }

    /**
     * Encode short values into binary data.
     *
     * @param values Short values.
     * @return this
     */
    public Encoder appendShort(short... values) {
        return this.appendShort(defaultByteOrder, values);
    }

    /**
     * Encode short values into binary data.
     *
     * @param order The byte order of the short values in binary data.
     * @param values Short values.
     * @return this
     */
    public Encoder appendShort(ByteOrder order, short... values) {
        for (val value: values) {
            this.outputStream.writeShort(order, value);
        }

        return this;
    }

    /**
     * Encode short values into binary data.
     *
     * @param offset The byte offset of the short values in binary data.
     * @param values Short values.
     * @return this
     */
    public Encoder writeShort(int offset, short... values) {
        return this.writeShort(offset, defaultByteOrder, values);
    }

    /**
     * Encode short values into binary data.
     *
     * @param offset The byte offset of the short values in binary data.
     * @param order The byte order of the short values in binary data.
     * @param values Short values.
     * @return this
     */
    public Encoder writeShort(int offset, ByteOrder order, short... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeShort(o + i, order, values[i]);
        });

        return this;
    }

    /**
     * Encode int8 values into binary data.
     *
     * @param values Integer values.
     * @return this
     */
    public Encoder appendInt8(int... values) {
        for (val value: values) {
            this.outputStream.writeInt8(value);
        }

        return this;
    }

    /**
     * Encode int8 values into binary data.
     *
     * @param offset The byte offset of the int8 values in binary data.
     * @param values Integer values.
     * @return this
     */
    public Encoder writeInt8(int offset, int... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeInt8(o + i, values[i]);
        });

        return this;
    }

    /**
     * Encode int16 values into binary data.
     *
     * @param values Integer values.
     * @return this
     */
    public Encoder appendInt16(int... values) {
        return this.appendInt16(defaultByteOrder, values);
    }

    /**
     * Encode int16 values into binary data.
     *
     * @param order The byte order of the int16 values in binary data.
     * @param values Integer values.
     * @return this
     */
    public Encoder appendInt16(ByteOrder order, int... values) {
        for (val value: values) {
            this.outputStream.writeInt16(order, value);
        }

        return this;
    }

    /**
     * Encode int16 values into binary data.
     *
     * @param offset The byte offset of the int16 values in binary data.
     * @param values Integer values.
     * @return this
     */
    public Encoder writeInt16(int offset, int... values) {
        return this.writeInt16(offset, defaultByteOrder, values);
    }

    /**
     * Encode int16 values into binary data.
     *
     * @param offset The byte offset of the int16 values in binary data.
     * @param order The byte order of the int16 values in binary data.
     * @param values Integer values.
     * @return this
     */
    public Encoder writeInt16(int offset, ByteOrder order, int... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeInt16(o + i, order, values[i]);
        });

        return this;
    }

    /**
     * Encode int32 values into binary data.
     *
     * @param values Integer values.
     * @return this
     */
    public Encoder appendInt32(int... values) {
        return this.appendInt32(defaultByteOrder, values);
    }

    /**
     * Encode int32 values into binary data.
     *
     * @param order The byte order of the int32 values in binary data.
     * @param values Integer values.
     * @return this
     */
    public Encoder appendInt32(ByteOrder order, int... values) {
        for (val value: values) {
            this.outputStream.writeInt32(order, value);
        }

        return this;
    }

    /**
     * Encode int32 values into binary data.
     *
     * @param offset The byte offset of the int32 values in binary data.
     * @param values Integer values.
     * @return this
     */
    public Encoder writeInt32(int offset, int... values) {
        return this.writeInt32(offset, defaultByteOrder, values);
    }

    /**
     * Encode int32 values into binary data.
     *
     * @param offset The byte offset of the int32 values in binary data.
     * @param order The byte order of the int32 values in binary data.
     * @param values Integer values.
     * @return this
     */
    public Encoder writeInt32(int offset, ByteOrder order, int... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeInt32(o + i, order, values[i]);
        });

        return this;
    }

    /**
     * Encode int64 values into binary data.
     *
     * @param values Integer values.
     * @return this
     */
    public Encoder appendInt64(long... values) {
        return this.appendInt64(defaultByteOrder, values);
    }

    /**
     * Encode int64 values into binary data.
     *
     * @param order The byte order of the int64 values in binary data.
     * @param values Integer values.
     * @return this
     */
    public Encoder appendInt64(ByteOrder order, long... values) {
        for (val value: values) {
            this.outputStream.writeInt64(order, value);
        }

        return this;
    }

    /**
     * Encode int64 values into binary data.
     *
     * @param offset The byte offset of the int64 values in binary data.
     * @param values Integer values.
     * @return this
     */
    public Encoder writeInt64(int offset, long... values) {
        return this.writeInt64(offset, defaultByteOrder, values);
    }

    /**
     * Encode int64 values into binary data.
     *
     * @param offset The byte offset of the int64 values in binary data.
     * @param order The byte order of the int64 values in binary data.
     * @param values Integer values.
     * @return this
     */
    public Encoder writeInt64(int offset, ByteOrder order, long... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeInt64(o + i, order, values[i]);
        });

        return this;
    }

    /**
     * Encode uint8 values into binary data.
     *
     * @param values uint8 values.
     * @return this
     */
    public Encoder appendUInt8(int... values) {
        for (val value: values) {
            this.outputStream.writeUInt8(value);
        }

        return this;
    }

    /**
     * Encode uint8 values into binary data.
     *
     * @param offset The byte offset of the uint8 values in binary data.
     * @param values uint8 values.
     * @return this
     */
    public Encoder writeUInt8(int offset, int... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeUInt8(o + i, values[i]);
        });

        return this;
    }

    /**
     * Encode uint16 values into binary data.
     *
     * @param values uint16 values.
     * @return this
     */
    public Encoder appendUInt16(int... values) {
        return this.appendUInt16(defaultByteOrder, values);
    }

    /**
     * Encode uint16 values into binary data.
     *
     * @param order The byte order of the uint16 values in binary data.
     * @param values uint16 values.
     * @return this
     */
    public Encoder appendUInt16(ByteOrder order, int... values) {
        for (val value: values) {
            this.outputStream.writeUInt16(order, value);
        }

        return this;
    }

    /**
     * Encode uint16 values into binary data.
     *
     * @param offset The byte offset of the uint16 values in binary data.
     * @param values uint16 values.
     * @return this
     */
    public Encoder writeUInt16(int offset, int... values) {
        return this.writeUInt16(offset, defaultByteOrder, values);
    }

    /**
     * Encode uint16 values into binary data.
     *
     * @param offset The byte offset of the uint16 values in binary data.
     * @param order The byte order of the uint16 values in binary data.
     * @param values uint16 values.
     * @return this
     */
    public Encoder writeUInt16(int offset, ByteOrder order, int... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeUInt16(o + i * UInt16Type.SIZE, order, values[i]);
        });

        return this;
    }

    /**
     * Encode uint32 values into binary data.
     *
     * @param values uint32 values.
     * @return this
     */
    public Encoder appendUInt32(long... values) {
        return this.appendUInt32(defaultByteOrder, values);
    }

    /**
     * Encode uint32 values into binary data.
     *
     * @param order The byte order of the uint32 values in binary data.
     * @param values uint32 values.
     * @return this
     */
    public Encoder appendUInt32(ByteOrder order, long... values) {
        for (val value: values) {
            this.outputStream.writeUInt32(order, value);
        }

        return this;
    }

    /**
     * Encode uint32 values into binary data.
     *
     * @param offset The byte offset of the uint32 values in binary data.
     * @param values uint32 values.
     * @return this
     */
    public Encoder writeUInt32(int offset, long... values) {
        return this.writeUInt32(offset, defaultByteOrder, values);
    }

    /**
     * Encode uint32 values into binary data.
     *
     * @param offset The byte offset of the uint32 values in binary data.
     * @param order The byte order of the uint32 values in binary data.
     * @param values uint32 values.
     * @return this
     */
    public Encoder writeUInt32(int offset, ByteOrder order, long... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeUInt32(o + i, order, values[i]);
        });

        return this;
    }

    /**
     * Encode uint64 values into binary data.
     *
     * @param values uint64 values.
     * @return this
     */
    public Encoder appendUInt64(BigInteger... values) {
        return this.appendUInt64(defaultByteOrder, values);
    }

    /**
     * Encode uint64 values into binary data.
     *
     * @param order The byte order of the uint64 values in binary data.
     * @param values uint64 values.
     * @return this
     */
    public Encoder appendUInt64(ByteOrder order, BigInteger... values) {
        for (val value: values) {
            this.outputStream.writeUInt64(order, value);
        }

        return this;
    }

    /**
     * Encode uint64 values into binary data.
     *
     * @param offset The byte offset of the uint64 values in binary data.
     * @param values uint64 values.
     * @return this
     */
    public Encoder writeUInt64(int offset, BigInteger... values) {
        return this.writeUInt64(offset, defaultByteOrder, values);
    }

    /**
     * Encode uint64 values into binary data.
     *
     * @param offset The byte offset of the uint64 values in binary data.
     * @param order The byte order of the uint64 values in binary data.
     * @param values uint64 values.
     * @return this
     */
    public Encoder writeUInt64(int offset, ByteOrder order, BigInteger... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeUInt64(o + i, order, values[i]);
        });

        return this;
    }

    /**
     * Encode float values into binary data.
     *
     * @param values float values.
     * @return this
     */
    public Encoder appendFloat(float... values) {
        return this.appendFloat(defaultByteOrder, values);
    }

    /**
     * Encode float values into binary data.
     *
     * @param order The byte order of the float values in binary data.
     * @param values float values.
     * @return this
     */
    public Encoder appendFloat(ByteOrder order, float... values) {
        for (val value: values) {
            this.outputStream.writeFloat(order, value);
        }

        return this;
    }

    /**
     * Encode float values into binary data.
     *
     * @param offset The byte offset of the float values in binary data.
     * @param values float values.
     * @return this
     */
    public Encoder writeFloat(int offset, float... values) {
        return this.writeFloat(offset, defaultByteOrder, values);
    }

    /**
     * Encode float values into binary data.
     *
     * @param offset The byte offset of the float values in binary data.
     * @param order The byte order of the float values in binary data.
     * @param values float values.
     * @return this
     */
    public Encoder writeFloat(int offset, ByteOrder order, float... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeFloat(o + i, order, values[i]);
        });

        return this;
    }

    /**
     * Encode double values into binary data.
     *
     * @param values double values.
     * @return this
     */
    public Encoder appendDouble(double... values) {
        return this.appendDouble(defaultByteOrder, values);
    }

    /**
     * Encode double values into binary data.
     *
     * @param order The byte order of the double values in binary data.
     * @param values double values.
     * @return this
     */
    public Encoder appendDouble(ByteOrder order, double... values) {
        for (val value: values) {
            this.outputStream.writeDouble(order, value);
        }

        return this;
    }

    /**
     * Encode double values into binary data.
     *
     * @param offset The byte offset of the double values in binary data.
     * @param values double values.
     * @return this
     */
    public Encoder writeDouble(int offset, double... values) {
        return this.writeDouble(offset, defaultByteOrder, values);
    }

    /**
     * Encode double values into binary data.
     *
     * @param offset The byte offset of the double values in binary data.
     * @param order The byte order of the double values in binary data.
     * @param values double values.
     * @return this
     */
    public Encoder writeDouble(int offset, ByteOrder order, double... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeDouble(o + i, order, values[i]);
        });

        return this;
    }

    /**
     * Encode byte array values into binary data.
     *
     * @param bytes string values.
     * @return this
     */
    public Encoder appendBytes(byte[] bytes) {
        this.outputStream.writeBytes(bytes);

        return this;
    }

    /**
     * Encode byte array values into binary data.
     *
     * @param offset The byte offset of the byte array values in binary data.
     * @param bytes string values.
     * @return this
     */
    public Encoder writeBytes(int offset, byte[] bytes) {
        this.outputStream.writeBytes(offset, bytes);

        return this;
    }

    /**
     * Align the current position to the specified byte boundary.
     *
     * @param alignment The byte boundary to align to.
     * @return this
     */
    public Encoder align(int alignment) {
        this.outputStream.align(alignment);

        return this;
    }

    /**
     * Ignore current position and move to next.
     *
     * @return this
     */
    public Encoder skip() {
        this.outputStream.skip();

        return this;
    }

    /**
     * Ignore following num positions and move to next.
     *
     * @param num The length to skip.
     * @return this
     */
    public Encoder skip(int num) {
        this.outputStream.skip(num);

        return this;
    }

    /**
     * Get the created binary data.
     *
     * @return The created binary data.
     */
    public byte[] get() {
        return this.outputStream
                .toByteBuffer()
                .toBytes();
    }
}
