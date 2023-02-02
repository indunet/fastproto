package org.indunet.fastproto.io;

import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.*;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class ByteBufferOutputStream {
    protected ByteBuffer byteBuffer;
    protected int index;

    public ByteBufferOutputStream() {
        this(new ByteBuffer());
    }

    public ByteBufferOutputStream(ByteBuffer buffer) {
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

    public void writeBytes(ByteBuffer buffer, int offset, int length, byte[] values) {
        int l = buffer.reverse(offset, length);

        IntStream.range(0, Math.min(l, values.length))
                .forEach(i -> buffer.set(offset + i, values[i]));
    }

    public void writeBool(int byteOffset, int bitOffset, BitOrder bitOrder, boolean value) {
        if (bitOffset < BoolType.BIT_0 || bitOffset > BoolType.BIT_7) {
            throw new IllegalArgumentException("Out of byte range.");
        }

        int bo = bitOffset;     // default by LSB_0

        if (bitOrder == BitOrder.MSB_0) {
            bo = 7 - bitOffset;
        }

        if (value) {
            byteBuffer.orEq(byteOffset, (byte) (0x01 << bo));
        } else {
            byteBuffer.andEq(byteOffset, (byte) ~(0x01 << bo));
        }
    }

    public void writeUInt8(int offset, int value) {
        if (value < UInt8Type.MIN_VALUE || value > UInt8Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uint8 range.");
        }

        int o = reverse(byteBuffer.getBytes(), offset);

        byteBuffer.set(o, (byte) value);
    }

    public void writeUInt16(int offset, ByteOrder byteOrder, int value) {
        if (value < UInt16Type.MIN_VALUE || value > UInt16Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uint16 range.");
        }

        int o = byteBuffer.reverse(offset);

        if (byteOrder == ByteOrder.BIG) {
            byteBuffer.set(o + 1, (byte) (value));
            byteBuffer.set(o, (byte) (value >>> 8));
        } else {
            byteBuffer.set(o, (byte) (value));
            byteBuffer.set(o + 1, (byte) (value >>> 8));
        }
    }

    public void writeInt16(int offset, ByteOrder byteOrder, int value) {
        if (value < Int16Type.MIN_VALUE || value > Int16Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of int16 range.");
        }

        int o = byteBuffer.reverse(offset);

        if (byteOrder == ByteOrder.BIG) {
            byteBuffer.set(o + 1, (byte) value);
            byteBuffer.set(o, (byte) (value >>> 8));
        } else {
            byteBuffer.set(o, (byte) value);
            byteBuffer.set(o + 1, (byte) (value >>> 8));
        }
    }

    public void writeByte(int offset, byte value) {
        byteBuffer.set(offset, value);
    }

    public void writeShort( int offset, ByteOrder byteOrder, short value) {
        int o = byteBuffer.reverse(offset);

        if (byteOrder == ByteOrder.LITTLE) {
            byteBuffer.set(o, (byte) (value));
            byteBuffer.set(o + 1, (byte) (value >>> 8));
        } else if (byteOrder == ByteOrder.BIG) {
            byteBuffer.set(o + 1, (byte) (value));
            byteBuffer.set(o, (byte) (value >>> 8));
        }
    }

    public void writeInt8(int offset, int value) {
        if (value < Int8Type.MIN_VALUE || value > Int8Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of int8 range.");
        }

        byteBuffer.set(offset, (byte) value);
    }

    public void writeInt32(int offset, ByteOrder byteOrder, int value) {
        int o = byteBuffer.reverse(offset);

        if (byteOrder == ByteOrder.LITTLE) {
            byteBuffer.set(o, (byte) value);
            byteBuffer.set(o + 1, (byte) (value >>> 8));
            byteBuffer.set(o + 2, (byte) (value >>> 16));
            byteBuffer.set(o + 3, (byte) (value >>> 24));
        } else if (byteOrder == ByteOrder.BIG) {
            byteBuffer.set(o + 3, (byte) value);
            byteBuffer.set(o + 2, (byte) (value >>> 8));
            byteBuffer.set(o + 1, (byte) (value >>> 16));
            byteBuffer.set(o, (byte) (value >>> 24));
        }
    }

    public void writeUInt32(int offset, ByteOrder byteOrder, long value) {
        if (value < UInt32Type.MIN_VALUE || value > UInt32Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uint32 range.");
        }

        int o = byteBuffer.reverse(offset);

        if (byteOrder == ByteOrder.BIG) {
            byteBuffer.set(o + 3, (byte) (value));
            byteBuffer.set(o + 2, (byte) (value >>> 8));
            byteBuffer.set(o + 1, (byte) (value >>> 16));
            byteBuffer.set(o, (byte) (value >>> 24));
        } else {
            byteBuffer.set(o, (byte) (value));
            byteBuffer.set(o + 1, (byte) (value >>> 8));
            byteBuffer.set(o + 2, (byte) (value >>> 16));
            byteBuffer.set(o + 3, (byte) (value >>> 24));
        }
    }

    public void writeUInt64(int offset, ByteOrder byteOrder, BigInteger value) {
        if (value.compareTo(UInt64Type.MAX_VALUE) > 0 || value.compareTo(UInt64Type.MIN_VALUE) < 0) {
            throw new IllegalArgumentException("Out of uinteger64 range.");
        }

        int o = byteBuffer.reverse(offset);
        long low = value
                .and(new BigInteger(String.valueOf(0xFFFF_FFFFL)))
                .longValueExact();
        long high = value
                .shiftRight(32)
                .longValueExact();

        if (byteOrder == ByteOrder.BIG) {
            byteBuffer.set(o + 7, (byte) low);
            byteBuffer.set(o + 6, (byte) (low >>> 8));
            byteBuffer.set(o + 5, (byte) (low >>> 16));
            byteBuffer.set(o + 4, (byte) (low >>> 24));

            byteBuffer.set(o + 3, (byte) high);
            byteBuffer.set(o + 2, (byte) (high >>> 8));
            byteBuffer.set(o + 1, (byte) (high >>> 16));
            byteBuffer.set(o, (byte) (high >>> 24));
        } else {
            byteBuffer.set(o, (byte) low);
            byteBuffer.set(o + 1, (byte) (low >>> 8));
            byteBuffer.set(o + 2, (byte) (low >>> 16));
            byteBuffer.set(o + 3, (byte) (low >>> 24));

            byteBuffer.set(o + 4, (byte) high);
            byteBuffer.set(o + 5, (byte) (high >>> 8));
            byteBuffer.set(o + 6, (byte) (high >>> 16));
            byteBuffer.set(o + 7, (byte) (high >>> 24));
        }
    }

    public void writeInt64(int offset, ByteOrder byteOrder, long value) {
        int o = byteBuffer.reverse(offset);

        if (byteOrder == ByteOrder.BIG) {
            byteBuffer.set(o + 7, (byte) value);
            byteBuffer.set(o + 6, (byte) (value >>> 8));
            byteBuffer.set(o + 5, (byte) (value >>> 16));
            byteBuffer.set(o + 4, (byte) (value >>> 24));

            byteBuffer.set(o + 3, (byte) (value >>> 32));
            byteBuffer.set(o + 2, (byte) (value >>> 40));
            byteBuffer.set(o + 1, (byte) (value >>> 48));
            byteBuffer.set(o, (byte) (value >>> 56));
        } else {
            byteBuffer.set(o, (byte) (value));
            byteBuffer.set(o + 1, (byte) (value >>> 8));
            byteBuffer.set(o + 2, (byte) (value >>> 16));
            byteBuffer.set(o + 3, (byte) (value >>> 24));

            byteBuffer.set(o + 4, (byte) (value >>> 32));
            byteBuffer.set(o + 5, (byte) (value >>> 40));
            byteBuffer.set(o + 6, (byte) (value >>> 48));
            byteBuffer.set(o + 7, (byte) (value >>> 56));
        }
    }

    public void writeFloat(int offset, ByteOrder byteOrder, float value) {
        int o = byteBuffer.reverse(offset);
        int bits = Float.floatToIntBits(value);

        if (byteOrder == ByteOrder.LITTLE) {
            byteBuffer.set(o, (byte) bits);
            byteBuffer.set(o + 1, (byte) (bits >>> 8));
            byteBuffer.set(o + 2, (byte) (bits >>> 16));
            byteBuffer.set(o + 3, (byte) (bits >>> 24));
        } else if (byteOrder == ByteOrder.BIG) {
            byteBuffer.set(o + 3, (byte) bits);
            byteBuffer.set(o + 2, (byte) (bits >>> 8));
            byteBuffer.set(o + 1, (byte) (bits >>> 16));
            byteBuffer.set(o, (byte) (bits >>> 24));
        }
    }

    public void writeDouble(int offset, ByteOrder byteOrder, double value) {
        int o = byteBuffer.reverse(offset);
        long bits = Double.doubleToRawLongBits(value);

        if (byteOrder == ByteOrder.BIG) {
            byteBuffer.set(o + 7, (byte) bits);
            byteBuffer.set(o + 6, (byte) (bits >>> 8));
            byteBuffer.set(o + 5, (byte) (bits >>> 16));
            byteBuffer.set(o + 4, (byte) (bits >>> 24));

            byteBuffer.set(o + 3, (byte) (bits >>> 32));
            byteBuffer.set(o + 2, (byte) (bits >>> 40));
            byteBuffer.set(o + 1, (byte) (bits >>> 48));
            byteBuffer.set(o, (byte) (bits >>> 56));
        } else {
            byteBuffer.set(o, (byte) bits);
            byteBuffer.set(o + 1, (byte) (bits >>> 8));
            byteBuffer.set(o + 2, (byte) (bits >>> 16));
            byteBuffer.set(o + 3, (byte) (bits >>> 24));

            byteBuffer.set(o + 4, (byte) (bits >>> 32));
            byteBuffer.set(o + 5, (byte) (bits >>> 40));
            byteBuffer.set(o + 6, (byte) (bits >>> 48));
            byteBuffer.set(o + 7, (byte) (bits >>> 56));
        }
    }

    public ByteBuffer toByteBuffer() {
        return this.byteBuffer;
    }
}
