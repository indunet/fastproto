package org.indunet.fastproto.io;

import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.*;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class ByteBufferInputStream {
    protected ByteBuffer byteBuffer;
    protected int index;

    public ByteBufferInputStream() {
        this(new ByteBuffer());
    }

    public ByteBufferInputStream(ByteBuffer buffer) {
        this.byteBuffer = buffer;
        this.index = 0;
    }

    public static int reverse(byte[] datagram, int offset) {
        val o = offset >= 0 ? offset : datagram.length + offset;

        if (o >= 0) {
            return o;
        } else {
            throw new IllegalArgumentException(String.format("Illegal offset %d", o));
        }
    }

    public static int reverse(byte[] datagram, int offset, int length) {
        int o = reverse(datagram, offset);
        int l = length >= 0 ? length : datagram.length + length - o + 1;

        if (l > 0) {
            return l;
        } else {
            throw new IllegalArgumentException(String.format("Illegal length %d", l));
        }
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

    public short readShort(int offset, ByteOrder byteOrder) {
        int o = byteBuffer.reverse(offset);
        short value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (byteBuffer.get(o) & 0x00FF);
            value |= (byteBuffer.get(o + 1) << 8);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= (byteBuffer.get(o) << 8);
            value |= (byteBuffer.get(o + 1) & 0x00FF);
        }

        return value;
    }

    public int readInt8(int offset) {
        return byteBuffer.get(offset);
    }


    public int readUInt8(int offset) {
        return byteBuffer.get(offset) & 0xFF;
    }

    public int readUInt16(ByteBuffer buffer, int offset, ByteOrder byteOrder) {
        int o = buffer.reverse(offset);

        if (byteOrder == ByteOrder.BIG) {
            return (buffer.get(o) & 0xFF) * 256 + (buffer.get(o + 1) & 0xFF);
        } else {
            return (buffer.get(o) & 0xFF) + (buffer.get(o + 1) & 0xFF) * 256;
        }
    }

    public int readInt16(int offset, ByteOrder byteOrder) {
        int o = byteBuffer.reverse(offset);
        short value = 0;

        if (byteOrder == ByteOrder.BIG) {
            value |= (byteBuffer.get(o) << 8);
            value |= (byteBuffer.get(o + 1) & 0x00FF);
        } else {
            value |= (byteBuffer.get(o) & 0x00FF);
            value |= (byteBuffer.get(o + 1) << 8);
        }

        return value;
    }

    public int readInt32(int offset, ByteOrder byteOrder) {
        int o = byteBuffer.reverse(offset);
        int value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (byteBuffer.get(o) & 0xFF);
            value |= ((byteBuffer.get(o + 1) & 0xFF) << 8);
            value |= ((byteBuffer.get(o + 2) & 0xFF) << 16);
            value |= ((byteBuffer.get(o + 3) & 0xFF) << 24);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= ((byteBuffer.get(o) & 0xFF) << 24);
            value |= ((byteBuffer.get(o + 1) & 0xFF) << 16);
            value |= ((byteBuffer.get(o + 2) & 0xFF) << 8);
            value |= (byteBuffer.get(o + 3) & 0xFF);
        }

        return value;
    }

    public long readUInt32(int offset, ByteOrder byteOrder) {
        int o = byteBuffer.reverse(offset);
        long value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (byteBuffer.get(o) & 0xFF);
            value |= ((byteBuffer.get(o + 1) & 0xFFL) << 8);
            value |= ((byteBuffer.get(o + 2) & 0xFFL) << 16);
            value |= ((byteBuffer.get(o + 3) & 0xFFL) << 24);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= ((byteBuffer.get(o) & 0xFFL) << 24);
            value |= ((byteBuffer.get(o + 1) & 0xFFL) << 16);
            value |= ((byteBuffer.get(o + 2) & 0xFFL) << 8);
            value |= (byteBuffer.get(o + 3) & 0xFF);
        }

        return value;
    }

    public BigInteger readUInt64(int offset, ByteOrder byteOrder) {
        int o = byteBuffer.reverse(offset);
        long low = 0;
        long high = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            low |= (byteBuffer.get(o) & 0xFF);
            low |= ((byteBuffer.get(o + 1) & 0xFFL) << 8);
            low |= ((byteBuffer.get(o + 2) & 0xFFL) << 16);
            low |= ((byteBuffer.get(o + 3) & 0xFFL) << 24);

            high |= (byteBuffer.get(o + 4) & 0xFFL);
            high |= ((byteBuffer.get(o + 5) & 0xFFL) << 8);
            high |= ((byteBuffer.get(o + 6) & 0xFFL) << 16);
            high |= ((byteBuffer.get(o + 7) & 0xFFL) << 24);
        } else if (byteOrder == ByteOrder.BIG) {
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

    public long readInt64(int offset, ByteOrder byteOrder) {
        int o = byteBuffer.reverse(offset);
        long value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (byteBuffer.get(o) & 0xFF);
            value |= ((byteBuffer.get(o + 1) & 0xFFL) << 8);
            value |= ((byteBuffer.get(o + 2) & 0xFFL) << 16);
            value |= ((byteBuffer.get(o + 3) & 0xFFL) << 24);

            value |= ((byteBuffer.get(o + 4) & 0xFFL) << 32);
            value |= ((byteBuffer.get(o + 5) & 0xFFL) << 40);
            value |= ((byteBuffer.get(o + 6) & 0xFFL) << 48);
            value |= ((byteBuffer.get(o + 7) & 0xFFL) << 56);
        } else if (byteOrder == ByteOrder.BIG) {
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

    public float readFloat(int offset, ByteOrder byteOrder) {
        int o = byteBuffer.reverse(offset);
        int value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (byteBuffer.get(o) & 0xFF);
            value |= ((byteBuffer.get(o + 1) & 0xFF) << 8);
            value |= ((byteBuffer.get(o + 2) & 0xFF) << 16);
            value |= ((byteBuffer.get(o + 3) & 0xFF) << 24);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= ((byteBuffer.get(o) & 0xFF) << 24);
            value |= ((byteBuffer.get(o + 1) & 0xFF) << 16);
            value |= ((byteBuffer.get(o + 2) & 0xFF) << 8);
            value |= (byteBuffer.get(o + 3) & 0xFF);
        }

        return Float.intBitsToFloat(value);
    }

    public double readDouble(int offset, ByteOrder byteOrder) {
        int o = byteBuffer.reverse(offset);
        long value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (byteBuffer.get(o) & 0xFFL);
            value |= ((byteBuffer.get(o + 1) & 0xFFL) << 8);
            value |= ((byteBuffer.get(o + 2) & 0xFFL) << 16);
            value |= ((byteBuffer.get(o + 3) & 0xFFL) << 24);

            value |= ((byteBuffer.get(o + 4) & 0xFFL) << 32);
            value |= ((byteBuffer.get(o + 5) & 0xFFL) << 40);
            value |= ((byteBuffer.get(o + 6) & 0xFFL) << 48);
            value |= ((byteBuffer.get(o + 7) & 0xFFL) << 56);
        } else if (byteOrder == ByteOrder.BIG) {
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

    public ByteBuffer toByteBuffer() {
        return this.byteBuffer;
    }
}
