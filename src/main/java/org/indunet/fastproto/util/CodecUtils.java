/*
 * Copyright 2019-2021 indunet.org
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

package org.indunet.fastproto.util;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.io.ByteBuffer;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Codec utils.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class CodecUtils {
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

    public static byte[] binaryType(byte[] datagram, int offset, int length) {
        int o = reverse(datagram, offset);
        int l = reverse(datagram, offset, length);

        if (o + l > datagram.length) {
            throw new IllegalArgumentException("Out of the datagram range.");
        }

        return Arrays.copyOfRange(datagram, o, o + l);
    }

    public static void binaryType(byte[] datagram, int offset, int length, byte[] values) {
        int o = reverse(datagram, offset);
        int l = reverse(datagram, offset, length);

        if (o + l > datagram.length) {
            throw new IllegalArgumentException("Out of the datagram range.");
        } else {
            System.arraycopy(values, 0, datagram, o, Math.min(l, values.length));
        }
    }

    public static void binaryType(ByteBuffer buffer, int offset, int length, byte[] values) {
        int l = buffer.reverse(offset, length);

        IntStream.range(0, Math.min(l, values.length))
                .forEach(i -> buffer.set(offset + i, values[i]));
    }

    public static boolean boolType(byte[] datagram, int byteOffset, int bitOffset, BitOrder order) {
        if (bitOffset < BoolType.BIT_0 || bitOffset > BoolType.BIT_7) {
            throw new IllegalArgumentException("Out of byte range.");
        }

        int o = reverse(datagram, byteOffset);

        if (order == BitOrder.MSB_0) {
            bitOffset = 7 - bitOffset;
        }

        return (datagram[o] & (0x01 << bitOffset)) != 0;
    }

    public static void boolType(byte[] datagram, int byteOffset, int bitOffset, BitOrder order, boolean value) {
        if (bitOffset < BoolType.BIT_0 || bitOffset > BoolType.BIT_7) {
            throw new IllegalArgumentException("Out of byte range.");
        }

        int o = reverse(datagram, byteOffset);

        if (order == BitOrder.MSB_0) {
            bitOffset = 7 - bitOffset;
        }

        if (value) {
            datagram[o] |= (0x01 << bitOffset);
        } else {
            datagram[o] &= ~(0x01 << bitOffset);
        }
    }

    public static void boolType(ByteBuffer byteBuffer, int byteOffset, int bitOffset, BitOrder bitOrder, boolean value) {
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

    public static byte byteType(ByteBuffer byteBuffer, int offset) {
        return byteBuffer.get(offset);
    }

    public static byte byteType(@NonNull byte[] datagram, int offset) {
        int o = reverse(datagram, offset);

        return datagram[o];
    }

    public static void byteType(@NonNull final byte[] datagram, int offset, byte value) {
        int o = reverse(datagram, offset);

        datagram[o] = value;
    }

    public static void byteType(ByteBuffer byteBuffer, int offset, byte value) {
        byteBuffer.set(offset, value);
    }

    public static int uint8Type(ByteBuffer byteBuffer, int offset) {
        return byteBuffer.get(offset) & 0xFF;
    }

    public static int uint8Type(@NonNull final byte[] datagram, int offset) {
        int o = reverse(datagram, offset);

        return datagram[o] & 0xFF;
    }

    public static void uint8Type(@NonNull byte[] datagram, int offset, int value) {
        if (value < UInt8Type.MIN_VALUE || value > UInt8Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uint8 range.");
        }

        int o = reverse(datagram, offset);

        datagram[o] = (byte) value;
    }

    public static void uint8Type(ByteBuffer byteArray, int offset, int value) {
        if (value < UInt8Type.MIN_VALUE || value > UInt8Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uint8 range.");
        }

        int o = reverse(byteArray.toBytes(), offset);

        byteArray.set(o, (byte) value);
    }

    public static int int8Type(ByteBuffer byteBuffer, int offset) {
        return byteBuffer.get(offset);
    }

    public static int int8Type(@NonNull final byte[] datagram, int offset) {
        int o = reverse(datagram, offset);

        return datagram[o];
    }

    public static void int8Type(@NonNull byte[] datagram, int offset, int value) {
        if (value < Int8Type.MIN_VALUE || value > Int8Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of int8 range.");
        }

        int o = reverse(datagram, offset);

        datagram[o] = (byte) value;
    }

    public static void int8Type(ByteBuffer byteBuffer, int offset, int value) {
        if (value < Int8Type.MIN_VALUE || value > Int8Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of int8 range.");
        }

        byteBuffer.set(offset, (byte) value);
    }

    public static int uint16Type(byte[] datagram, int offset, ByteOrder byteOrder) {
        int o = reverse(datagram, offset);

        if (byteOrder == ByteOrder.BIG) {
            return (datagram[o] & 0xFF) * 256 + (datagram[o + 1] & 0xFF);
        } else {
            return (datagram[o + 1] & 0xFF) * 256 + (datagram[o] & 0xFF);
        }
    }

    public static int uint16Type(ByteBuffer buffer, int offset, ByteOrder byteOrder) {
        int o = buffer.reverse(offset);

        if (byteOrder == ByteOrder.BIG) {
            return (buffer.get(o) & 0xFF) * 256 + (buffer.get(o + 1) & 0xFF);
        } else {
            return (buffer.get(o) & 0xFF) + (buffer.get(o + 1) & 0xFF) * 256;
        }
    }

    public static void uint16Type(byte[] datagram, int offset, ByteOrder byteOrder, int value) {
        if (value < UInt16Type.MIN_VALUE || value > UInt16Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uint16 range.");
        }

        int o = reverse(datagram, offset);

        if (byteOrder == ByteOrder.BIG) {
            datagram[o + 1] = (byte) (value);
            datagram[o] = (byte) (value >>> 8);
        } else {
            datagram[o] = (byte) (value);
            datagram[o + 1] = (byte) (value >>> 8);
        }
    }

    public static void uint16Type(ByteBuffer buffer, int offset, ByteOrder byteOrder, int value) {
        if (value < UInt16Type.MIN_VALUE || value > UInt16Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uint16 range.");
        }

        int o = buffer.reverse(offset);

        if (byteOrder == ByteOrder.BIG) {
            buffer.set(o + 1, (byte) (value));
            buffer.set(o, (byte) (value >>> 8));
        } else {
            buffer.set(o, (byte) (value));
            buffer.set(o + 1, (byte) (value >>> 8));
        }
    }

    public static int int16Type(@NonNull final byte[] datagram, int offset, ByteOrder byteOrder) {
        int o = reverse(datagram, offset);
        short value = 0;

        if (byteOrder == ByteOrder.BIG) {
            value |= (datagram[o + 1] & 0x00FF);
            value |= (datagram[o] << 8);
        } else {
            value |= (datagram[o] & 0x00FF);
            value |= (datagram[o + 1] << 8);
        }

        return value;
    }

    public static int int16Type(ByteBuffer buffer, int offset, ByteOrder byteOrder) {
        int o = buffer.reverse(offset);
        short value = 0;

        if (byteOrder == ByteOrder.BIG) {
            value |= (buffer.get(o) << 8);
            value |= (buffer.get(o + 1) & 0x00FF);
        } else {
            value |= (buffer.get(o) & 0x00FF);
            value |= (buffer.get(o + 1) << 8);
        }

        return value;
    }

    public static void int16Type(byte[] datagram, int offset, ByteOrder byteOrder, int value) {
        if (value < Int16Type.MIN_VALUE || value > Int16Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of int16 range.");
        }

        int o = reverse(datagram, offset);

        if (byteOrder == ByteOrder.BIG) {
            datagram[o + 1] = (byte) (value);
            datagram[o] = (byte) (value >>> 8);
        } else {
            datagram[o] = (byte) (value);
            datagram[o + 1] = (byte) (value >>> 8);
        }
    }

    public static void int16Type(ByteBuffer buffer, int offset, ByteOrder byteOrder, int value) {
        if (value < Int16Type.MIN_VALUE || value > Int16Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of int16 range.");
        }

        int o = buffer.reverse(offset);

        if (byteOrder == ByteOrder.BIG) {
            buffer.set(o + 1, (byte) value);
            buffer.set(o, (byte) (value >>> 8));
        } else {
            buffer.set(o, (byte) value);
            buffer.set(o + 1, (byte) (value >>> 8));
        }
    }

    public static short shortType(@NonNull final byte[] datagram, int offset, ByteOrder byteOrder) {
        int o = reverse(datagram, offset);
        short value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (datagram[o] & 0x00FF);
            value |= (datagram[o + 1] << 8);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= (datagram[o + 1] & 0x00FF);
            value |= (datagram[o] << 8);
        }

        return value;
    }

    public static short shortType(ByteBuffer buffer, int offset, ByteOrder byteOrder) {
        int o = buffer.reverse(offset);
        short value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (buffer.get(o) & 0x00FF);
            value |= (buffer.get(o + 1) << 8);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= (buffer.get(o) << 8);
            value |= (buffer.get(o + 1) & 0x00FF);
        }

        return value;
    }

    public static void shortType(@NonNull byte[] datagram, int offset, ByteOrder byteOrder, short value) {
        int o = reverse(datagram, offset);

        if (byteOrder == ByteOrder.LITTLE) {
            datagram[o] = (byte) (value);
            datagram[o + 1] = (byte) (value >>> 8);
        } else if (byteOrder == ByteOrder.BIG) {
            datagram[o + 1] = (byte) (value);
            datagram[o] = (byte) (value >>> 8);
        }
    }

    public static void shortType(ByteBuffer buffer, int offset, ByteOrder byteOrder, short value) {
        int o = buffer.reverse(offset);

        if (byteOrder == ByteOrder.LITTLE) {
            buffer.set(o, (byte) (value));
            buffer.set(o + 1, (byte) (value >>> 8));
        } else if (byteOrder == ByteOrder.BIG) {
            buffer.set(o + 1, (byte) (value));
            buffer.set(o, (byte) (value >>> 8));
        }
    }

    public static int int32Type(@NonNull final byte[] datagram, int offset, ByteOrder byteOrder) {
        int o = reverse(datagram, offset);
        int value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (datagram[o] & 0xFF);
            value |= ((datagram[o + 1] & 0xFF) << 8);
            value |= ((datagram[o + 2] & 0xFF) << 16);
            value |= ((datagram[o + 3] & 0xFF) << 24);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= (datagram[o + 3] & 0xFF);
            value |= ((datagram[o + 2] & 0xFF) << 8);
            value |= ((datagram[o + 1] & 0xFF) << 16);
            value |= ((datagram[o] & 0xFF) << 24);
        }

        return value;
    }

    public static int int32Type(ByteBuffer buffer, int offset, ByteOrder byteOrder) {
        int o = buffer.reverse(offset);
        int value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (buffer.get(o) & 0xFF);
            value |= ((buffer.get(o + 1) & 0xFF) << 8);
            value |= ((buffer.get(o + 2) & 0xFF) << 16);
            value |= ((buffer.get(o + 3) & 0xFF) << 24);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= ((buffer.get(o) & 0xFF) << 24);
            value |= ((buffer.get(o + 1) & 0xFF) << 16);
            value |= ((buffer.get(o + 2) & 0xFF) << 8);
            value |= (buffer.get(o + 3) & 0xFF);
        }

        return value;
    }

    public static void int32Type(byte[] datagram, int offset, ByteOrder byteOrder, int value) {
        int o = reverse(datagram, offset);

        if (byteOrder == ByteOrder.LITTLE) {
            datagram[o] = (byte) (value);
            datagram[o + 1] = (byte) (value >>> 8);
            datagram[o + 2] = (byte) (value >>> 16);
            datagram[o + 3] = (byte) (value >>> 24);
        } else if (byteOrder == ByteOrder.BIG) {
            datagram[o + 3] = (byte) (value);
            datagram[o + 2] = (byte) (value >>> 8);
            datagram[o + 1] = (byte) (value >>> 16);
            datagram[o] = (byte) (value >>> 24);
        }
    }

    public static void int32Type(ByteBuffer buffer, int offset, ByteOrder byteOrder, int value) {
        int o = buffer.reverse(offset);

        if (byteOrder == ByteOrder.LITTLE) {
            buffer.set(o, (byte) value);
            buffer.set(o + 1, (byte) (value >>> 8));
            buffer.set(o + 2, (byte) (value >>> 16));
            buffer.set(o + 3, (byte) (value >>> 24));
        } else if (byteOrder == ByteOrder.BIG) {
            buffer.set(o + 3, (byte) value);
            buffer.set(o + 2, (byte) (value >>> 8));
            buffer.set(o + 1, (byte) (value >>> 16));
            buffer.set(o, (byte) (value >>> 24));
        }
    }

    public static long uint32Type(@NonNull final byte[] datagram, int offset, ByteOrder byteOrder) {
        int o = reverse(datagram, offset);
        long value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (datagram[o] & 0xFF);
            value |= ((datagram[o + 1] & 0xFFL) << 8);
            value |= ((datagram[o + 2] & 0xFFL) << 16);
            value |= ((datagram[o + 3] & 0xFFL) << 24);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= (datagram[o + 3] & 0xFF);
            value |= ((datagram[o + 2] & 0xFFL) << 8);
            value |= ((datagram[o + 1] & 0xFFL) << 16);
            value |= ((datagram[o] & 0xFFL) << 24);
        }

        return value;
    }

    public static long uint32Type(ByteBuffer buffer, int offset, ByteOrder byteOrder) {
        int o = buffer.reverse(offset);
        long value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (buffer.get(o) & 0xFF);
            value |= ((buffer.get(o + 1) & 0xFFL) << 8);
            value |= ((buffer.get(o + 2) & 0xFFL) << 16);
            value |= ((buffer.get(o + 3) & 0xFFL) << 24);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= ((buffer.get(o) & 0xFFL) << 24);
            value |= ((buffer.get(o + 1) & 0xFFL) << 16);
            value |= ((buffer.get(o + 2) & 0xFFL) << 8);
            value |= (buffer.get(o + 3) & 0xFF);
        }

        return value;
    }

    public static void uint32Type(@NonNull byte[] datagram, int offset, ByteOrder byteOrder, long value) {
        if (value < UInt32Type.MIN_VALUE || value > UInt32Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uint32 range.");
        }

        int o = reverse(datagram, offset);

        if (byteOrder == ByteOrder.BIG) {
            datagram[o + 3] = (byte) (value);
            datagram[o + 2] = (byte) (value >>> 8);
            datagram[o + 1] = (byte) (value >>> 16);
            datagram[o] = (byte) (value >>> 24);
        } else {
            datagram[o] = (byte) (value);
            datagram[o + 1] = (byte) (value >>> 8);
            datagram[o + 2] = (byte) (value >>> 16);
            datagram[o + 3] = (byte) (value >>> 24);
        }
    }

    public static void uint32Type(ByteBuffer buffer, int offset, ByteOrder byteOrder, long value) {
        if (value < UInt32Type.MIN_VALUE || value > UInt32Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uint32 range.");
        }

        int o = buffer.reverse(offset);

        if (byteOrder == ByteOrder.BIG) {
            buffer.set(o + 3, (byte) (value));
            buffer.set(o + 2, (byte) (value >>> 8));
            buffer.set(o + 1, (byte) (value >>> 16));
            buffer.set(o, (byte) (value >>> 24));
        } else {
            buffer.set(o, (byte) (value));
            buffer.set(o + 1, (byte) (value >>> 8));
            buffer.set(o + 2, (byte) (value >>> 16));
            buffer.set(o + 3, (byte) (value >>> 24));
        }
    }

    public static BigInteger uint64Type(@NonNull final byte[] datagram, int offset, ByteOrder byteOrder) {
        int o = reverse(datagram, offset);
        long low = 0;
        long high = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            low |= (datagram[o] & 0xFF);
            low |= ((datagram[o + 1] & 0xFFL) << 8);
            low |= ((datagram[o + 2] & 0xFFL) << 16);
            low |= ((datagram[o + 3] & 0xFFL) << 24);

            high |= (datagram[o + 4] & 0xFFL);
            high |= ((datagram[o + 5] & 0xFFL) << 8);
            high |= ((datagram[o + 6] & 0xFFL) << 16);
            high |= ((datagram[o + 7] & 0xFFL) << 24);
        } else if (byteOrder == ByteOrder.BIG) {
            low |= (datagram[o + 7] & 0xFF);
            low |= ((datagram[o + 6] & 0xFFL) << 8);
            low |= ((datagram[o + 5] & 0xFFL) << 16);
            low |= ((datagram[o + 4] & 0xFFL) << 24);

            high |= (datagram[o + 3] & 0xFFL);
            high |= ((datagram[o + 2] & 0xFFL) << 8);
            high |= ((datagram[o + 1] & 0xFFL) << 16);
            high |= ((datagram[o] & 0xFFL) << 24);
        }

        return new BigInteger(String.valueOf(high))
                .multiply(new BigInteger(String.valueOf(UInt32Type.MAX_VALUE + 1)))
                .add(new BigInteger(String.valueOf(low)));
    }

    public static BigInteger uint64Type(ByteBuffer buffer, int offset, ByteOrder byteOrder) {
        int o = buffer.reverse(offset);
        long low = 0;
        long high = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            low |= (buffer.get(o) & 0xFF);
            low |= ((buffer.get(o + 1) & 0xFFL) << 8);
            low |= ((buffer.get(o + 2) & 0xFFL) << 16);
            low |= ((buffer.get(o + 3) & 0xFFL) << 24);

            high |= (buffer.get(o + 4) & 0xFFL);
            high |= ((buffer.get(o + 5) & 0xFFL) << 8);
            high |= ((buffer.get(o + 6) & 0xFFL) << 16);
            high |= ((buffer.get(o + 7) & 0xFFL) << 24);
        } else if (byteOrder == ByteOrder.BIG) {
            high |= ((buffer.get(o) & 0xFFL) << 24);
            high |= ((buffer.get(o + 1) & 0xFFL) << 16);
            high |= ((buffer.get(o + 2) & 0xFFL) << 8);
            high |= (buffer.get(o + 3) & 0xFFL);

            low |= ((buffer.get(o + 4) & 0xFFL) << 24);
            low |= ((buffer.get(o + 5) & 0xFFL) << 16);
            low |= ((buffer.get(o + 6) & 0xFFL) << 8);
            low |= (buffer.get(o + 7) & 0xFF);
        }

        return new BigInteger(String.valueOf(high))
                .multiply(new BigInteger(String.valueOf(UInt32Type.MAX_VALUE + 1)))
                .add(new BigInteger(String.valueOf(low)));
    }

    public static void uint64Type(@NonNull byte[] datagram, int offset, ByteOrder byteOrder, BigInteger value) {
        if (value.compareTo(UInt64Type.MAX_VALUE) > 0 || value.compareTo(UInt64Type.MIN_VALUE) < 0) {
            throw new IllegalArgumentException("Out of uinteger64 range.");
        }

        int o = reverse(datagram, offset);
        long low = value
                .and(new BigInteger(String.valueOf(0xFFFF_FFFFL)))
                .longValueExact();
        long high = value
                .shiftRight(32)
                .longValueExact();

        if (byteOrder == ByteOrder.BIG) {
            datagram[o + 7] = (byte) (low);
            datagram[o + 6] = (byte) (low >>> 8);
            datagram[o + 5] = (byte) (low >>> 16);
            datagram[o + 4] = (byte) (low >>> 24);

            datagram[o + 3] = (byte) (high);
            datagram[o + 2] = (byte) (high >>> 8);
            datagram[o + 1] = (byte) (high >>> 16);
            datagram[o] = (byte) (high >>> 24);
        } else {
            datagram[o] = (byte) (low);
            datagram[o + 1] = (byte) (low >>> 8);
            datagram[o + 2] = (byte) (low >>> 16);
            datagram[o + 3] = (byte) (low >>> 24);

            datagram[o + 4] = (byte) (high);
            datagram[o + 5] = (byte) (high >>> 8);
            datagram[o + 6] = (byte) (high >>> 16);
            datagram[o + 7] = (byte) (high >>> 24);
        }
    }

    public static void uint64Type(ByteBuffer buffer, int offset, ByteOrder byteOrder, BigInteger value) {
        if (value.compareTo(UInt64Type.MAX_VALUE) > 0 || value.compareTo(UInt64Type.MIN_VALUE) < 0) {
            throw new IllegalArgumentException("Out of uinteger64 range.");
        }

        int o = buffer.reverse(offset);
        long low = value
                .and(new BigInteger(String.valueOf(0xFFFF_FFFFL)))
                .longValueExact();
        long high = value
                .shiftRight(32)
                .longValueExact();

        if (byteOrder == ByteOrder.BIG) {
            buffer.set(o + 7, (byte) low);
            buffer.set(o + 6, (byte) (low >>> 8));
            buffer.set(o + 5, (byte) (low >>> 16));
            buffer.set(o + 4, (byte) (low >>> 24));

            buffer.set(o + 3, (byte) high);
            buffer.set(o + 2, (byte) (high >>> 8));
            buffer.set(o + 1, (byte) (high >>> 16));
            buffer.set(o, (byte) (high >>> 24));
        } else {
            buffer.set(o, (byte) low);
            buffer.set(o + 1, (byte) (low >>> 8));
            buffer.set(o + 2, (byte) (low >>> 16));
            buffer.set(o + 3, (byte) (low >>> 24));

            buffer.set(o + 4, (byte) high);
            buffer.set(o + 5, (byte) (high >>> 8));
            buffer.set(o + 6, (byte) (high >>> 16));
            buffer.set(o + 7, (byte) (high >>> 24));
        }
    }

    public static long int64Type(@NonNull final byte[] datagram, int offset, ByteOrder byteOrder) {
        int o = reverse(datagram, offset);
        long value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (datagram[o] & 0xFF);
            value |= ((datagram[o + 1] & 0xFFL) << 8);
            value |= ((datagram[o + 2] & 0xFFL) << 16);
            value |= ((datagram[o + 3] & 0xFFL) << 24);

            value |= ((datagram[o + 4] & 0xFFL) << 32);
            value |= ((datagram[o + 5] & 0xFFL) << 40);
            value |= ((datagram[o + 6] & 0xFFL) << 48);
            value |= ((datagram[o + 7] & 0xFFL) << 56);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= (datagram[o + 7] & 0xFF);
            value |= ((datagram[o + 6] & 0xFFL) << 8);
            value |= ((datagram[o + 5] & 0xFFL) << 16);
            value |= ((datagram[o + 4] & 0xFFL) << 24);

            value |= ((datagram[o + 3] & 0xFFL) << 32);
            value |= ((datagram[o + 2] & 0xFFL) << 40);
            value |= ((datagram[o + 1] & 0xFFL) << 48);
            value |= ((datagram[o] & 0xFFL) << 56);
        }

        return value;
    }

    public static long int64Type(ByteBuffer buffer, int offset, ByteOrder byteOrder) {
        int o = buffer.reverse(offset);
        long value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (buffer.get(o) & 0xFF);
            value |= ((buffer.get(o + 1) & 0xFFL) << 8);
            value |= ((buffer.get(o + 2) & 0xFFL) << 16);
            value |= ((buffer.get(o + 3) & 0xFFL) << 24);

            value |= ((buffer.get(o + 4) & 0xFFL) << 32);
            value |= ((buffer.get(o + 5) & 0xFFL) << 40);
            value |= ((buffer.get(o + 6) & 0xFFL) << 48);
            value |= ((buffer.get(o + 7) & 0xFFL) << 56);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= ((buffer.get(o) & 0xFFL) << 56);
            value |= ((buffer.get(o + 1) & 0xFFL) << 48);
            value |= ((buffer.get(o + 2) & 0xFFL) << 40);
            value |= ((buffer.get(o + 3) & 0xFFL) << 32);

            value |= ((buffer.get(o + 4) & 0xFFL) << 24);
            value |= ((buffer.get(o + 5) & 0xFFL) << 16);
            value |= ((buffer.get(o + 6) & 0xFFL) << 8);
            value |= (buffer.get(o + 7) & 0xFF);
        }

        return value;
    }

    public static void int64Type(@NonNull byte[] datagram, int offset, ByteOrder byteOrder, long value) {
        int o = reverse(datagram, offset);

        if (byteOrder == ByteOrder.BIG) {
            datagram[o + 7] = (byte) (value);
            datagram[o + 6] = (byte) (value >>> 8);
            datagram[o + 5] = (byte) (value >>> 16);
            datagram[o + 4] = (byte) (value >>> 24);

            datagram[o + 3] = (byte) (value >>> 32);
            datagram[o + 2] = (byte) (value >>> 40);
            datagram[o + 1] = (byte) (value >>> 48);
            datagram[o] = (byte) (value >>> 56);
        } else {
            datagram[o] = (byte) (value);
            datagram[o + 1] = (byte) (value >>> 8);
            datagram[o + 2] = (byte) (value >>> 16);
            datagram[o + 3] = (byte) (value >>> 24);

            datagram[o + 4] = (byte) (value >>> 32);
            datagram[o + 5] = (byte) (value >>> 40);
            datagram[o + 6] = (byte) (value >>> 48);
            datagram[o + 7] = (byte) (value >>> 56);
        }
    }

    public static void int64Type(ByteBuffer buffer, int offset, ByteOrder byteOrder, long value) {
        int o = buffer.reverse(offset);

        if (byteOrder == ByteOrder.BIG) {
            buffer.set(o + 7, (byte) value);
            buffer.set(o + 6, (byte) (value >>> 8));
            buffer.set(o + 5, (byte) (value >>> 16));
            buffer.set(o + 4, (byte) (value >>> 24));

            buffer.set(o + 3, (byte) (value >>> 32));
            buffer.set(o + 2, (byte) (value >>> 40));
            buffer.set(o + 1, (byte) (value >>> 48));
            buffer.set(o, (byte) (value >>> 56));
        } else {
            buffer.set(o, (byte) (value));
            buffer.set(o + 1, (byte) (value >>> 8));
            buffer.set(o + 2, (byte) (value >>> 16));
            buffer.set(o + 3, (byte) (value >>> 24));

            buffer.set(o + 4, (byte) (value >>> 32));
            buffer.set(o + 5, (byte) (value >>> 40));
            buffer.set(o + 6, (byte) (value >>> 48));
            buffer.set(o + 7, (byte) (value >>> 56));
        }
    }

    public static float floatType(@NonNull final byte[] datagram, int offset, ByteOrder byteOrder) {
        int o = reverse(datagram, offset);
        int value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (datagram[o] & 0xFF);
            value |= ((datagram[o + 1] & 0xFF) << 8);
            value |= ((datagram[o + 2] & 0xFF) << 16);
            value |= ((datagram[o + 3] & 0xFF) << 24);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= (datagram[o + 3] & 0xFF);
            value |= ((datagram[o + 2] & 0xFF) << 8);
            value |= ((datagram[o + 1] & 0xFF) << 16);
            value |= ((datagram[o] & 0xFF) << 24);
        }

        return Float.intBitsToFloat(value);
    }

    public static float floatType(ByteBuffer buffer, int offset, ByteOrder byteOrder) {
        int o = buffer.reverse(offset);
        int value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (buffer.get(o) & 0xFF);
            value |= ((buffer.get(o + 1) & 0xFF) << 8);
            value |= ((buffer.get(o + 2) & 0xFF) << 16);
            value |= ((buffer.get(o + 3) & 0xFF) << 24);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= ((buffer.get(o) & 0xFF) << 24);
            value |= ((buffer.get(o + 1) & 0xFF) << 16);
            value |= ((buffer.get(o + 2) & 0xFF) << 8);
            value |= (buffer.get(o + 3) & 0xFF);
        }

        return Float.intBitsToFloat(value);
    }

    public static void floatType(@NonNull byte[] datagram, int offset, ByteOrder byteOrder, float value) {
        int o = reverse(datagram, offset);
        int bits = Float.floatToIntBits(value);

        if (byteOrder == ByteOrder.LITTLE) {
            datagram[o] = (byte) bits;
            datagram[o + 1] = (byte) (bits >>> 8);
            datagram[o + 2] = (byte) (bits >>> 16);
            datagram[o + 3] = (byte) (bits >>> 24);
        } else if (byteOrder == ByteOrder.BIG) {
            datagram[o + 3] = (byte) (bits);
            datagram[o + 2] = (byte) (bits >>> 8);
            datagram[o + 1] = (byte) (bits >>> 16);
            datagram[o] = (byte) (bits >>> 24);
        }
    }

    public static void floatType(ByteBuffer buffer, int offset, ByteOrder byteOrder, float value) {
        int o = buffer.reverse(offset);
        int bits = Float.floatToIntBits(value);

        if (byteOrder == ByteOrder.LITTLE) {
            buffer.set(o, (byte) bits);
            buffer.set(o + 1, (byte) (bits >>> 8));
            buffer.set(o + 2, (byte) (bits >>> 16));
            buffer.set(o + 3, (byte) (bits >>> 24));
        } else if (byteOrder == ByteOrder.BIG) {
            buffer.set(o + 3, (byte) bits);
            buffer.set(o + 2, (byte) (bits >>> 8));
            buffer.set(o + 1, (byte) (bits >>> 16));
            buffer.set(o, (byte) (bits >>> 24));
        }
    }

    public static double doubleType(@NonNull final byte[] datagram, int offset, ByteOrder byteOrder) {
        int o = reverse(datagram, offset);
        long value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (datagram[o] & 0xFFL);
            value |= ((datagram[o + 1] & 0xFFL) << 8);
            value |= ((datagram[o + 2] & 0xFFL) << 16);
            value |= ((datagram[o + 3] & 0xFFL) << 24);

            value |= ((datagram[o + 4] & 0xFFL) << 32);
            value |= ((datagram[o + 5] & 0xFFL) << 40);
            value |= ((datagram[o + 6] & 0xFFL) << 48);
            value |= ((datagram[o + 7] & 0xFFL) << 56);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= (datagram[o + 7] & 0xFFL);
            value |= ((datagram[o + 6] & 0xFFL) << 8);
            value |= ((datagram[o + 5] & 0xFFL) << 16);
            value |= ((datagram[o + 4] & 0xFFL) << 24);

            value |= ((datagram[o + 3] & 0xFFL) << 32);
            value |= ((datagram[o + 2] & 0xFFL) << 40);
            value |= ((datagram[o + 1] & 0xFFL) << 48);
            value |= ((datagram[o] & 0xFFL) << 56);
        }

        return Double.longBitsToDouble(value);
    }

    public static double doubleType(ByteBuffer buffer, int offset, ByteOrder byteOrder) {
        int o = buffer.reverse(offset);
        long value = 0;

        if (byteOrder == ByteOrder.LITTLE) {
            value |= (buffer.get(o) & 0xFFL);
            value |= ((buffer.get(o + 1) & 0xFFL) << 8);
            value |= ((buffer.get(o + 2) & 0xFFL) << 16);
            value |= ((buffer.get(o + 3) & 0xFFL) << 24);

            value |= ((buffer.get(o + 4) & 0xFFL) << 32);
            value |= ((buffer.get(o + 5) & 0xFFL) << 40);
            value |= ((buffer.get(o + 6) & 0xFFL) << 48);
            value |= ((buffer.get(o + 7) & 0xFFL) << 56);
        } else if (byteOrder == ByteOrder.BIG) {
            value |= ((buffer.get(o) & 0xFFL) << 56);
            value |= ((buffer.get(o + 1) & 0xFFL) << 48);
            value |= ((buffer.get(o + 2) & 0xFFL) << 40);
            value |= ((buffer.get(o + 3) & 0xFFL) << 32);

            value |= ((buffer.get(o + 5) & 0xFFL) << 16);
            value |= ((buffer.get(o + 4) & 0xFFL) << 24);
            value |= ((buffer.get(o + 6) & 0xFFL) << 8);
            value |= (buffer.get(o + 7) & 0xFFL);
        }

        return Double.longBitsToDouble(value);
    }

    public static void doubleType(@NonNull byte[] datagram, int offset, ByteOrder byteOrder, double value) {
        int o = reverse(datagram, offset);
        long bits = Double.doubleToRawLongBits(value);

        if (byteOrder == ByteOrder.BIG) {
            datagram[o + 7] = (byte) (bits);
            datagram[o + 6] = (byte) (bits >>> 8);
            datagram[o + 5] = (byte) (bits >>> 16);
            datagram[o + 4] = (byte) (bits >>> 24);

            datagram[o + 3] = (byte) (bits >>> 32);
            datagram[o + 2] = (byte) (bits >>> 40);
            datagram[o + 1] = (byte) (bits >>> 48);
            datagram[o] = (byte) (bits >>> 56);
        } else {
            datagram[o] = (byte) (bits);
            datagram[o + 1] = (byte) (bits >>> 8);
            datagram[o + 2] = (byte) (bits >>> 16);
            datagram[o + 3] = (byte) (bits >>> 24);

            datagram[o + 4] = (byte) (bits >>> 32);
            datagram[o + 5] = (byte) (bits >>> 40);
            datagram[o + 6] = (byte) (bits >>> 48);
            datagram[o + 7] = (byte) (bits >>> 56);
        }
    }

    public static void doubleType(ByteBuffer buffer, int offset, ByteOrder byteOrder, double value) {
        int o = buffer.reverse(offset);
        long bits = Double.doubleToRawLongBits(value);

        if (byteOrder == ByteOrder.BIG) {
            buffer.set(o + 7, (byte) bits);
            buffer.set(o + 6, (byte) (bits >>> 8));
            buffer.set(o + 5, (byte) (bits >>> 16));
            buffer.set(o + 4, (byte) (bits >>> 24));

            buffer.set(o + 3, (byte) (bits >>> 32));
            buffer.set(o + 2, (byte) (bits >>> 40));
            buffer.set(o + 1, (byte) (bits >>> 48));
            buffer.set(o, (byte) (bits >>> 56));
        } else {
            buffer.set(o, (byte) bits);
            buffer.set(o + 1, (byte) (bits >>> 8));
            buffer.set(o + 2, (byte) (bits >>> 16));
            buffer.set(o + 3, (byte) (bits >>> 24));

            buffer.set(o + 4, (byte) (bits >>> 32));
            buffer.set(o + 5, (byte) (bits >>> 40));
            buffer.set(o + 6, (byte) (bits >>> 48));
            buffer.set(o + 7, (byte) (bits >>> 56));
        }
    }
}
