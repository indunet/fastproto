package org.indunet.fastproto;

import lombok.val;
import org.indunet.fastproto.annotation.UInt16Type;
import org.indunet.fastproto.io.ByteBuffer;
import org.indunet.fastproto.io.ByteBufferOutputStream;

import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Binary encoder.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public final class Encoder {
    ByteBufferOutputStream outputStream;
    ByteOrder defaultByteOrder = ByteOrder.LITTLE;
    BitOrder defaultBitOrder = BitOrder.LSB_0;

    public Encoder() {
        this.outputStream = new ByteBufferOutputStream();
    }

    public Encoder(byte[] bytes) {
        this.outputStream = new ByteBufferOutputStream(bytes);
    }

    public Encoder defaultByteOrder(ByteOrder byteOrder) {
        this.defaultByteOrder = byteOrder;

        return this;
    }

    public Encoder defaultBitOrder(BitOrder bitOrder) {
        this.defaultBitOrder = bitOrder;

        return this;
    }

    public Encoder appendBool(boolean... values) {
        return this.appendBool(defaultBitOrder, values);
    }

    public Encoder appendBool(BitOrder order, boolean... values) {
        for (val value: values) {
            this.outputStream.writeBool(order, value);
        }

        return this;
    }

    public Encoder writeBool(int byteOffset, int bitOffset, boolean... values) {
        return this.writeBool(byteOffset, bitOffset, defaultBitOrder, values);
    }

    public Encoder writeBool(int byteOffset, int bitOffset, BitOrder order, boolean... values) {
        for (val value: values) {
            this.outputStream.writeBool(byteOffset, bitOffset, order, value);
        }

        return this;
    }

    public Encoder appendByte(byte... values) {
        for (val value: values) {
            this.outputStream.writeByte(value);
        }

        return this;
    }

    public Encoder writeByte(int offset, byte... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeByte(o + i, values[i]);
        });

        return this;
    }

    public Encoder appendShort(short... values) {
        return this.appendShort(defaultByteOrder, values);
    }

    public Encoder appendShort(ByteOrder order, short... values) {
        for (val value: values) {
            this.outputStream.writeShort(order, value);
        }

        return this;
    }

    public Encoder writeShort(int offset, short... values) {
        return this.writeShort(offset, defaultByteOrder, values);
    }

    public Encoder writeShort(int offset, ByteOrder order, short... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeShort(o + i, order, values[i]);
        });

        return this;
    }

    public Encoder appendInt8(int... values) {
        for (val value: values) {
            this.outputStream.writeInt8(value);
        }

        return this;
    }

    public Encoder writeInt8(int offset, int... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeInt8(o + i, values[i]);
        });

        return this;
    }

    public Encoder appendInt16(int... values) {
        return this.appendInt16(defaultByteOrder, values);
    }

    public Encoder appendInt16(ByteOrder order, int... values) {
        for (val value: values) {
            this.outputStream.writeInt16(order, value);
        }

        return this;
    }

    public Encoder writeInt16(int offset, int... values) {
        return this.writeInt16(offset, defaultByteOrder, values);
    }

    public Encoder writeInt16(int offset, ByteOrder order, int... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeInt16(o + i, order, values[i]);
        });

        return this;
    }

    public Encoder appendInt32(int... values) {
        return this.appendInt32(defaultByteOrder, values);
    }

    public Encoder appendInt32(ByteOrder order, int... values) {
        for (val value: values) {
            this.outputStream.writeInt32(order, value);
        }

        return this;
    }

    public Encoder writeInt32(int offset, int... values) {
        return this.writeInt32(offset, defaultByteOrder, values);
    }

    public Encoder writeInt32(int offset, ByteOrder order, int... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeInt32(o + i, order, values[i]);
        });

        return this;
    }

    public Encoder appendInt64(long... values) {
        return this.appendInt64(defaultByteOrder, values);
    }

    public Encoder appendInt64(ByteOrder order, long... values) {
        for (val value: values) {
            this.outputStream.writeInt64(order, value);
        }

        return this;
    }

    public Encoder writeInt64(int offset, long... values) {
        return this.writeInt64(offset, defaultByteOrder, values);
    }

    public Encoder writeInt64(int offset, ByteOrder order, long... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeInt64(o + i, order, values[i]);
        });

        return this;
    }

    public Encoder appendUInt8(int... values) {
        for (val value: values) {
            this.outputStream.writeUInt8(value);
        }

        return this;
    }

    public Encoder writeUInt8(int offset, int... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeUInt8(o + i, values[i]);
        });

        return this;
    }

    public Encoder appendUInt16(int... values) {
        return this.appendUInt16(defaultByteOrder, values);
    }

    public Encoder appendUInt16(ByteOrder order, int... values) {
        for (val value: values) {
            this.outputStream.writeUInt16(order, value);
        }

        return this;
    }

    public Encoder writeUInt16(int offset, int... values) {
        return this.writeUInt16(offset, defaultByteOrder, values);
    }

    public Encoder writeUInt16(int offset, ByteOrder order, int... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeUInt16(o + i * UInt16Type.SIZE, order, values[i]);
        });

        return this;
    }

    public Encoder appendUInt32(long... values) {
        return this.appendUInt32(defaultByteOrder, values);
    }

    public Encoder appendUInt32(ByteOrder order, long... values) {
        for (val value: values) {
            this.outputStream.writeUInt32(order, value);
        }

        return this;
    }

    public Encoder writeUInt32(int offset, long... values) {
        return this.writeUInt32(offset, defaultByteOrder, values);
    }

    public Encoder writeUInt32(int offset, ByteOrder order, long... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeUInt32(o + i, order, values[i]);
        });

        return this;
    }

    public Encoder appendUInt64(BigInteger... values) {
        return this.appendUInt64(defaultByteOrder, values);
    }

    public Encoder appendUInt64(ByteOrder order, BigInteger... values) {
        for (val value: values) {
            this.outputStream.writeUInt64(order, value);
        }

        return this;
    }

    public Encoder writeUInt64(int offset, BigInteger... values) {
        return this.writeUInt64(offset, defaultByteOrder, values);
    }

    public Encoder writeUInt64(int offset, ByteOrder order, BigInteger... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeUInt64(o + i, order, values[i]);
        });

        return this;
    }

    public Encoder appendFloat(float... values) {
        return this.appendFloat(defaultByteOrder, values);
    }

    public Encoder appendFloat(ByteOrder order, float... values) {
        for (val value: values) {
            this.outputStream.writeFloat(order, value);
        }

        return this;
    }

    public Encoder writeFloat(int offset, float... values) {
        return this.writeFloat(offset, defaultByteOrder, values);
    }

    public Encoder writeFloat(int offset, ByteOrder order, float... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeFloat(o + i, order, values[i]);
        });

        return this;
    }

    public Encoder appendDouble(double... values) {
        return this.appendDouble(defaultByteOrder, values);
    }

    public Encoder appendDouble(ByteOrder order, double... values) {
        for (val value: values) {
            this.outputStream.writeDouble(order, value);
        }

        return this;
    }

    public Encoder writeDouble(int offset, double... values) {
        return this.writeDouble(offset, defaultByteOrder, values);
    }

    public Encoder writeDouble(int offset, ByteOrder order, double... values) {
        int o = this.outputStream.toByteBuffer().reverse(offset);

        IntStream.range(0, values.length).forEach(i -> {
            this.outputStream.writeDouble(o + i, order, values[i]);
        });

        return this;
    }

    public Encoder appendBytes(byte[] bytes) {
        this.outputStream.writeBytes(bytes);

        return this;
    }

    public Encoder writeBytes(int offset, byte[] bytes) {
        this.outputStream.writeBytes(offset, bytes);

        return this;
    }

    public Encoder align(int alignment) {
        this.outputStream.align(alignment);

        return this;
    }

    public Encoder skip() {
        this.outputStream.skip();

        return this;
    }

    public Encoder skip(int num) {
        this.outputStream.skip(num);

        return this;
    }

    public byte[] get() {
        return this.outputStream
                .toByteBuffer()
                .toBytes();
    }
}
