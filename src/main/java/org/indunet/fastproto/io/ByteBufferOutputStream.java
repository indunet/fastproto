/**
 * Copyright 2019-2023 indunet.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.indunet.fastproto.io;

import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.*;

import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Output stream of ByteBuffer which supplies more convenient methods for writing.
 *
 * @author Deng Ran
 * @since 3.10.1
 */
public final class ByteBufferOutputStream {
    ByteBuffer byteBuffer;
    int byteIndex;
    int bitIndex;

    public ByteBufferOutputStream() {
        this(new ByteBuffer());
    }

    public ByteBufferOutputStream(byte[] bytes) {
        this(new ByteBuffer(bytes));
    }

    public ByteBufferOutputStream(ByteBuffer buffer) {
        this.byteBuffer = buffer;
        this.byteIndex = 0;
        this.bitIndex = 0;
    }

    public void writeBool(BitOrder order, boolean value) {
        this.writeBool(byteIndex, bitIndex, order, value);
    }

    public void writeBool(int byteOffset, int bitOffset, BitOrder order, boolean value) {
        if (bitOffset < BoolType.BIT_0 || bitOffset > BoolType.BIT_7) {
            throw new IllegalArgumentException("Out of byte range.");
        }

        int bo = bitOffset;     // default by LSB_0

        if (order == BitOrder.MSB_0) {
            bo = 7 - bitOffset;
        }

        if (bo == BoolType.BIT_7) {
            this.byteIndex ++;
            this.bitIndex = BoolType.BIT_0;
        }

        if (value) {
            byteBuffer.orEq(byteOffset, (byte) (0x01 << bo));
        } else {
            byteBuffer.andEq(byteOffset, (byte) ~(0x01 << bo));
        }
    }

    public void writeByte(byte value) {
        byteBuffer.set(byteIndex ++, value);
    }

    public void writeByte(int offset, byte value) {
        byteIndex = byteBuffer.reverse(offset) + Int8Type.SIZE;
        byteBuffer.set(offset, value);
    }

    public void writeShort(ByteOrder order, short value) {
        this.writeShort(byteIndex, order, value);
    }

    public void writeShort(int offset, ByteOrder order, short value) {
        int o = byteBuffer.reverse(offset);

        this.byteIndex = o + Int16Type.SIZE;

        if (order == ByteOrder.LITTLE) {
            byteBuffer.set(o, (byte) (value));
            byteBuffer.set(o + 1, (byte) (value >>> 8));
        } else if (order == ByteOrder.BIG) {
            byteBuffer.set(o + 1, (byte) (value));
            byteBuffer.set(o, (byte) (value >>> 8));
        }
    }

    public void writeInt8(int value) {
        if (value < Int8Type.MIN_VALUE || value > Int8Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of int8 range.");
        }

        byteBuffer.set(byteIndex ++, (byte) value);
    }

    public void writeInt8(int offset, int value) {
        if (value < Int8Type.MIN_VALUE || value > Int8Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of int8 range.");
        }

        byteIndex = byteBuffer.reverse(offset) + Int8Type.SIZE;
        byteBuffer.set(offset, (byte) value);
    }

    public void writeInt16(ByteOrder order, int value) {
        this.writeInt16(byteIndex, order, value);
    }

    public void writeInt16(int offset, ByteOrder order, int value) {
        if (value < Int16Type.MIN_VALUE || value > Int16Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of int16 range.");
        }

        int o = byteBuffer.reverse(offset);

        this.byteIndex = o + Int16Type.SIZE;

        if (order == ByteOrder.BIG) {
            byteBuffer.set(o + 1, (byte) value);
            byteBuffer.set(o, (byte) (value >>> 8));
        } else {
            byteBuffer.set(o, (byte) value);
            byteBuffer.set(o + 1, (byte) (value >>> 8));
        }
    }

    public void writeInt32(ByteOrder order, int value) {
        this.writeInt32(byteIndex, order, value);
    }

    public void writeInt32(int offset, ByteOrder order, int value) {
        int o = byteBuffer.reverse(offset);

        this.byteIndex = o + Int32Type.SIZE;

        if (order == ByteOrder.LITTLE) {
            byteBuffer.set(o, (byte) value);
            byteBuffer.set(o + 1, (byte) (value >>> 8));
            byteBuffer.set(o + 2, (byte) (value >>> 16));
            byteBuffer.set(o + 3, (byte) (value >>> 24));
        } else if (order == ByteOrder.BIG) {
            byteBuffer.set(o + 3, (byte) value);
            byteBuffer.set(o + 2, (byte) (value >>> 8));
            byteBuffer.set(o + 1, (byte) (value >>> 16));
            byteBuffer.set(o, (byte) (value >>> 24));
        }
    }

    public void writeInt64(ByteOrder order, long value) {
        this.writeInt64(byteIndex, order, value);
    }

    public void writeInt64(int offset, ByteOrder order, long value) {
        int o = byteBuffer.reverse(offset);

        this.byteIndex = o + Int64Type.SIZE;

        if (order == ByteOrder.BIG) {
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

    public void writeUInt8(int value) {
        this.writeUInt8(byteIndex, value);
    }

    public void writeUInt8(int offset, int value) {
        if (value < UInt8Type.MIN_VALUE || value > UInt8Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uint8 range.");
        }

        byteIndex = byteBuffer.reverse(offset) + UInt8Type.SIZE;
        byteBuffer.set(offset, (byte) value);
    }

    public void writeUInt16(ByteOrder order, int value) {
        this.writeUInt16(byteIndex, order, value);
    }

    public void writeUInt16(int offset, ByteOrder order, int value) {
        if (value < UInt16Type.MIN_VALUE || value > UInt16Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uint16 range.");
        }

        int o = byteBuffer.reverse(offset);

        this.byteIndex = o + UInt16Type.SIZE;

        if (order == ByteOrder.BIG) {
            byteBuffer.set(o + 1, (byte) (value));
            byteBuffer.set(o, (byte) (value >>> 8));
        } else {
            byteBuffer.set(o, (byte) (value));
            byteBuffer.set(o + 1, (byte) (value >>> 8));
        }
    }

    public void writeUInt32(ByteOrder order, long value) {
        this.writeUInt32(byteIndex, order, value);
    }

    public void writeUInt32(int offset, ByteOrder order, long value) {
        if (value < UInt32Type.MIN_VALUE || value > UInt32Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uint32 range.");
        }

        int o = byteBuffer.reverse(offset);

        this.byteIndex = o + UInt32Type.SIZE;

        if (order == ByteOrder.BIG) {
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

    public void writeUInt64(ByteOrder order, BigInteger value) {
        this.writeUInt64(byteIndex, order, value);
    }

    public void writeUInt64(int offset, ByteOrder order, BigInteger value) {
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

        this.byteIndex = o + UInt64Type.SIZE;

        if (order == ByteOrder.BIG) {
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

    public void writeFloat(ByteOrder order, float value) {
        this.writeFloat(byteIndex, order, value);
    }

    public void writeFloat(int offset, ByteOrder order, float value) {
        int o = byteBuffer.reverse(offset);
        int bits = Float.floatToIntBits(value);

        this.byteIndex = o + FloatType.SIZE;

        if (order == ByteOrder.LITTLE) {
            byteBuffer.set(o, (byte) bits);
            byteBuffer.set(o + 1, (byte) (bits >>> 8));
            byteBuffer.set(o + 2, (byte) (bits >>> 16));
            byteBuffer.set(o + 3, (byte) (bits >>> 24));
        } else if (order == ByteOrder.BIG) {
            byteBuffer.set(o + 3, (byte) bits);
            byteBuffer.set(o + 2, (byte) (bits >>> 8));
            byteBuffer.set(o + 1, (byte) (bits >>> 16));
            byteBuffer.set(o, (byte) (bits >>> 24));
        }
    }

    public void writeDouble(ByteOrder order, double value) {
        this.writeDouble(byteIndex, order, value);
    }

    public void writeDouble(int offset, ByteOrder order, double value) {
        int o = byteBuffer.reverse(offset);
        long bits = Double.doubleToRawLongBits(value);

        this.byteIndex = o + DoubleType.SIZE;

        if (order == ByteOrder.BIG) {
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

    public void writeBytes(byte[] values) {
        this.writeBytes(byteIndex, values);
    }

    public void writeBytes(int offset, byte[] values) {
        int o = byteBuffer.reverse(offset);

        this.byteIndex = o + values.length;

        IntStream.range(0, values.length)
                .forEach(i -> byteBuffer.set(o + i, values[i]));
    }

    public void align(int alignment) {
        if (alignment <= 0 || (alignment & 0x01) != 0) {
            throw new IllegalArgumentException("alignment must be a positive even number");
        }

        int index = this.byteIndex;
        int after = ((index + (alignment - 1)) & ~(alignment - 1));

        this.byteIndex = Math.max(after, 0);
    }

    public void skip() {
        this.byteIndex ++;
    }

    public void skip(int num) {
        if (num >= 0) {
            this.byteIndex += num;
        } else {
            throw new IllegalArgumentException("num must be a positive number.");
        }
    }

    public ByteBuffer toByteBuffer() {
        return this.byteBuffer;
    }
}
