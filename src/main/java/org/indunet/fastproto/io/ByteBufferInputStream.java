package org.indunet.fastproto.io;

import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.*;

import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Input stream of ByteBuffer which supplies more convenient methods for reading.
 *
 * @author Deng Ran
 * @since 3.10.1
 */
public final class ByteBufferInputStream {
    ByteBuffer byteBuffer;
    int index;

    public ByteBufferInputStream() {
        this(new ByteBuffer());
    }

    public ByteBufferInputStream(byte[] bytes) {
        this(new ByteBuffer(bytes));
    }

    public ByteBufferInputStream(ByteBuffer buffer) {
        this.byteBuffer = buffer;
        this.index = 0;
    }

    public boolean readBool(int byteOffset, int bitOffset, BitOrder bitOrder) {
        if (bitOffset < BoolType.BIT_0 || bitOffset > BoolType.BIT_7) {
            throw new IllegalArgumentException("Out of byte range.");
        }

        int bo = bitOffset;     // default by LSB_0

        if (bitOrder == BitOrder.MSB_0) {
            bo = 7 - bitOffset;
        }

        return (this.byteBuffer.get(byteOffset) & (1 << bo)) != 0;
    }

    public byte readByte(int offset) {
        return byteBuffer.get(offset);
    }

    public short readShort(int offset, ByteOrder order) {
        int o = byteBuffer.reverse(offset);
        short value = 0;

        if (order == ByteOrder.LITTLE) {
            value |= (byteBuffer.get(o) & 0x00FF);
            value |= (byteBuffer.get(o + 1) << 8);
        } else if (order == ByteOrder.BIG) {
            value |= (byteBuffer.get(o) << 8);
            value |= (byteBuffer.get(o + 1) & 0x00FF);
        }

        return value;
    }

    public int readInt8(int offset) {
        return byteBuffer.get(offset);
    }

    public int readInt16(int offset, ByteOrder order) {
        int o = byteBuffer.reverse(offset);
        short value = 0;

        if (order == ByteOrder.BIG) {
            value |= (byteBuffer.get(o) << 8);
            value |= (byteBuffer.get(o + 1) & 0x00FF);
        } else {
            value |= (byteBuffer.get(o) & 0x00FF);
            value |= (byteBuffer.get(o + 1) << 8);
        }

        return value;
    }

    public int readInt32(int offset, ByteOrder order) {
        int o = byteBuffer.reverse(offset);
        int value = 0;

        if (order == ByteOrder.LITTLE) {
            value |= (byteBuffer.get(o) & 0xFF);
            value |= ((byteBuffer.get(o + 1) & 0xFF) << 8);
            value |= ((byteBuffer.get(o + 2) & 0xFF) << 16);
            value |= ((byteBuffer.get(o + 3) & 0xFF) << 24);
        } else if (order == ByteOrder.BIG) {
            value |= ((byteBuffer.get(o) & 0xFF) << 24);
            value |= ((byteBuffer.get(o + 1) & 0xFF) << 16);
            value |= ((byteBuffer.get(o + 2) & 0xFF) << 8);
            value |= (byteBuffer.get(o + 3) & 0xFF);
        }

        return value;
    }

    public long readInt64(int offset, ByteOrder order) {
        int o = byteBuffer.reverse(offset);
        long value = 0;

        if (order == ByteOrder.LITTLE) {
            value |= (byteBuffer.get(o) & 0xFF);
            value |= ((byteBuffer.get(o + 1) & 0xFFL) << 8);
            value |= ((byteBuffer.get(o + 2) & 0xFFL) << 16);
            value |= ((byteBuffer.get(o + 3) & 0xFFL) << 24);

            value |= ((byteBuffer.get(o + 4) & 0xFFL) << 32);
            value |= ((byteBuffer.get(o + 5) & 0xFFL) << 40);
            value |= ((byteBuffer.get(o + 6) & 0xFFL) << 48);
            value |= ((byteBuffer.get(o + 7) & 0xFFL) << 56);
        } else if (order == ByteOrder.BIG) {
            value |= ((byteBuffer.get(o) & 0xFFL) << 56);
            value |= ((byteBuffer.get(o + 1) & 0xFFL) << 48);
            value |= ((byteBuffer.get(o + 2) & 0xFFL) << 40);
            value |= ((byteBuffer.get(o + 3) & 0xFFL) << 32);

            value |= ((byteBuffer.get(o + 4) & 0xFFL) << 24);
            value |= ((byteBuffer.get(o + 5) & 0xFFL) << 16);
            value |= ((byteBuffer.get(o + 6) & 0xFFL) << 8);
            value |= (byteBuffer.get(o + 7) & 0xFF);
        }

        return value;
    }

    public int readUInt8(int offset) {
        return byteBuffer.get(offset) & 0xFF;
    }

    public int readUInt16(int offset, ByteOrder order) {
        int o = byteBuffer.reverse(offset);

        if (order == ByteOrder.BIG) {
            return (byteBuffer.get(o) & 0xFF) * 256 + (byteBuffer.get(o + 1) & 0xFF);
        } else {
            return (byteBuffer.get(o) & 0xFF) + (byteBuffer.get(o + 1) & 0xFF) * 256;
        }
    }

    public long readUInt32(int offset, ByteOrder order) {
        int o = byteBuffer.reverse(offset);
        long value = 0;

        if (order == ByteOrder.LITTLE) {
            value |= (byteBuffer.get(o) & 0xFF);
            value |= ((byteBuffer.get(o + 1) & 0xFFL) << 8);
            value |= ((byteBuffer.get(o + 2) & 0xFFL) << 16);
            value |= ((byteBuffer.get(o + 3) & 0xFFL) << 24);
        } else if (order == ByteOrder.BIG) {
            value |= ((byteBuffer.get(o) & 0xFFL) << 24);
            value |= ((byteBuffer.get(o + 1) & 0xFFL) << 16);
            value |= ((byteBuffer.get(o + 2) & 0xFFL) << 8);
            value |= (byteBuffer.get(o + 3) & 0xFF);
        }

        return value;
    }

    public BigInteger readUInt64(int offset, ByteOrder order) {
        int o = byteBuffer.reverse(offset);
        long low = 0;
        long high = 0;

        if (order == ByteOrder.LITTLE) {
            low |= (byteBuffer.get(o) & 0xFF);
            low |= ((byteBuffer.get(o + 1) & 0xFFL) << 8);
            low |= ((byteBuffer.get(o + 2) & 0xFFL) << 16);
            low |= ((byteBuffer.get(o + 3) & 0xFFL) << 24);

            high |= (byteBuffer.get(o + 4) & 0xFFL);
            high |= ((byteBuffer.get(o + 5) & 0xFFL) << 8);
            high |= ((byteBuffer.get(o + 6) & 0xFFL) << 16);
            high |= ((byteBuffer.get(o + 7) & 0xFFL) << 24);
        } else if (order == ByteOrder.BIG) {
            high |= ((byteBuffer.get(o) & 0xFFL) << 24);
            high |= ((byteBuffer.get(o + 1) & 0xFFL) << 16);
            high |= ((byteBuffer.get(o + 2) & 0xFFL) << 8);
            high |= (byteBuffer.get(o + 3) & 0xFFL);

            low |= ((byteBuffer.get(o + 4) & 0xFFL) << 24);
            low |= ((byteBuffer.get(o + 5) & 0xFFL) << 16);
            low |= ((byteBuffer.get(o + 6) & 0xFFL) << 8);
            low |= (byteBuffer.get(o + 7) & 0xFF);
        }

        return new BigInteger(String.valueOf(high))
                .multiply(new BigInteger(String.valueOf(UInt32Type.MAX_VALUE + 1)))
                .add(new BigInteger(String.valueOf(low)));
    }

    public float readFloat(int offset, ByteOrder order) {
        int o = byteBuffer.reverse(offset);
        int value = 0;

        if (order == ByteOrder.LITTLE) {
            value |= (byteBuffer.get(o) & 0xFF);
            value |= ((byteBuffer.get(o + 1) & 0xFF) << 8);
            value |= ((byteBuffer.get(o + 2) & 0xFF) << 16);
            value |= ((byteBuffer.get(o + 3) & 0xFF) << 24);
        } else if (order == ByteOrder.BIG) {
            value |= ((byteBuffer.get(o) & 0xFF) << 24);
            value |= ((byteBuffer.get(o + 1) & 0xFF) << 16);
            value |= ((byteBuffer.get(o + 2) & 0xFF) << 8);
            value |= (byteBuffer.get(o + 3) & 0xFF);
        }

        return Float.intBitsToFloat(value);
    }

    public double readDouble(int offset, ByteOrder order) {
        int o = byteBuffer.reverse(offset);
        long value = 0;

        if (order == ByteOrder.LITTLE) {
            value |= (byteBuffer.get(o) & 0xFFL);
            value |= ((byteBuffer.get(o + 1) & 0xFFL) << 8);
            value |= ((byteBuffer.get(o + 2) & 0xFFL) << 16);
            value |= ((byteBuffer.get(o + 3) & 0xFFL) << 24);

            value |= ((byteBuffer.get(o + 4) & 0xFFL) << 32);
            value |= ((byteBuffer.get(o + 5) & 0xFFL) << 40);
            value |= ((byteBuffer.get(o + 6) & 0xFFL) << 48);
            value |= ((byteBuffer.get(o + 7) & 0xFFL) << 56);
        } else if (order == ByteOrder.BIG) {
            value |= ((byteBuffer.get(o) & 0xFFL) << 56);
            value |= ((byteBuffer.get(o + 1) & 0xFFL) << 48);
            value |= ((byteBuffer.get(o + 2) & 0xFFL) << 40);
            value |= ((byteBuffer.get(o + 3) & 0xFFL) << 32);

            value |= ((byteBuffer.get(o + 5) & 0xFFL) << 16);
            value |= ((byteBuffer.get(o + 4) & 0xFFL) << 24);
            value |= ((byteBuffer.get(o + 6) & 0xFFL) << 8);
            value |= (byteBuffer.get(o + 7) & 0xFFL);
        }

        return Double.longBitsToDouble(value);
    }

    public byte[] readBytes(int offset, int length) {
        int o = byteBuffer.reverse(offset);
        int l = byteBuffer.reverse(offset, length);
        byte[] bytes = new byte[l];

        IntStream.range(0, l)
                .forEach(i -> bytes[i] = byteBuffer.get(o + i));

        return bytes;
    }

    public ByteBuffer toByteBuffer() {
        return this.byteBuffer;
    }
}
