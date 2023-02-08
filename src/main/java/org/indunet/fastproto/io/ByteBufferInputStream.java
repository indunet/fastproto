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
 * Input stream of ByteBuffer which supplies more convenient methods for reading.
 *
 * @author Deng Ran
 * @since 3.10.1
 */
public final class ByteBufferInputStream extends ByteBufferIOStream {
    public ByteBufferInputStream() {
        this(new ByteBuffer());
    }

    public ByteBufferInputStream(byte[] bytes) {
        this(new ByteBuffer(bytes));
    }

    public ByteBufferInputStream(ByteBuffer buffer) {
        super(buffer);
    }

    public boolean readBool(BitOrder order) {
        return this.readBool(byteIndex, bitIndex, order);
    }

    public boolean readBool(int byteOffset, int bitOffset, BitOrder order) {
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

        return (this.byteBuffer.get(byteOffset) & (1 << bo)) != 0;
    }

    public byte readByte() {
        return this.readByte(byteIndex);
    }

    public byte readByte(int offset) {
        this.byteIndex = byteBuffer.reverse(offset) + Int8Type.SIZE;

        return byteBuffer.get(offset);
    }

    public short readShort(ByteOrder order) {
        return this.readShort(byteIndex, order);
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

        this.byteIndex = o + Int16Type.SIZE;

        return value;
    }

    public int readInt8() {
        return this.readInt8(byteIndex);
    }

    public int readInt8(int offset) {
        this.byteIndex = byteBuffer.reverse(offset) + Int8Type.SIZE;

        return byteBuffer.get(offset);
    }

    public int readInt16(ByteOrder order) {
        return readInt16(byteIndex, order);
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

        this.byteIndex = o + Int16Type.SIZE;

        return value;
    }

    public int readInt32(ByteOrder order) {
        return this.readInt32(byteIndex, order);
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

        this.byteIndex = o + Int32Type.SIZE;

        return value;
    }

    public long readInt64(ByteOrder order) {
        return this.readInt64(byteIndex, order);
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

        this.byteIndex = o + Int64Type.SIZE;

        return value;
    }

    public int readUInt8() {
        return this.readUInt8(byteIndex ++);
    }

    public int readUInt8(int offset) {
        byteIndex = this.byteBuffer.reverse(offset) + UInt8Type.SIZE;

        return byteBuffer.get(offset) & 0xFF;
    }

    public int readUInt16(ByteOrder order) {
        return this.readUInt16(byteIndex, order);
    }

    public int readUInt16(int offset, ByteOrder order) {
        int o = byteBuffer.reverse(offset);

        this.byteIndex = o + UInt16Type.SIZE;

        if (order == ByteOrder.BIG) {
            return (byteBuffer.get(o) & 0xFF) * 256 + (byteBuffer.get(o + 1) & 0xFF);
        } else {
            return (byteBuffer.get(o) & 0xFF) + (byteBuffer.get(o + 1) & 0xFF) * 256;
        }
    }

    public long readUInt32(ByteOrder order) {
        return this.readUInt32(byteIndex, order);
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

        this.byteIndex = o + UInt32Type.SIZE;

        return value;
    }

    public BigInteger readUInt64(ByteOrder order) {
        return this.readUInt64(byteIndex, order);
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

        this.byteIndex = o + UInt64Type.SIZE;

        return new BigInteger(String.valueOf(high))
                .multiply(new BigInteger(String.valueOf(UInt32Type.MAX_VALUE + 1)))
                .add(new BigInteger(String.valueOf(low)));
    }

    public float readFloat(ByteOrder order) {
        return this.readFloat(byteIndex, order);
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

        this.byteIndex = o + FloatType.SIZE;

        return Float.intBitsToFloat(value);
    }

    public double readDouble(ByteOrder order) {
        return this.readDouble(byteIndex, order);
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

        this.byteIndex = o + DoubleType.SIZE;

        return Double.longBitsToDouble(value);
    }

    public byte[] readBytes(int length) {
        return this.readBytes(byteIndex, length);
    }

    public byte[] readBytes(int offset, int length) {
        int o = byteBuffer.reverse(offset);
        int l = byteBuffer.reverse(offset, length);
        byte[] bytes = new byte[l];

        IntStream.range(0, l)
                .forEach(i -> bytes[i] = byteBuffer.get(o + i));

        this.byteIndex = o + l;

        return bytes;
    }
}
