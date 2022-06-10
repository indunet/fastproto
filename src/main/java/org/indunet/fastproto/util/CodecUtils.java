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
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.*;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Deng Ran
 * @since 2.5.0
 */
public class CodecUtils {
    public static int reverse(@NonNull byte[] datagram, int offset) {
        int o = offset >= 0 ? offset : datagram.length + offset;

        if (o >= 0) {
            return o;
        } else {
            throw new IllegalArgumentException("Out of the datagram range.");
        }
    }

    public static int reverse(@NonNull byte[] datagram, int offset, int length) {
        int o = reverse(datagram, offset);
        int l = length >= 0 ? length : datagram.length + length - o + 1;

        if (l > 0) {
            return l;
        } else {
            throw new IllegalArgumentException("Out of the datagram range.");
        }
    }

    public static byte[] binaryType(@NonNull final byte[] datagram, int offset, int length) {
        int o = reverse(datagram, offset);
        int l = reverse(datagram, offset, length);

        if (o + l > datagram.length) {
            throw new IllegalArgumentException("Out of the datagram range.");
        }

        return Arrays.copyOfRange(datagram, o, o + l);
    }

    public static void binaryType(@NonNull byte[] datagram, int offset, int length, @NonNull byte[] values) {
        int o =reverse(datagram, offset);
        int l = reverse(datagram, offset, length);

        if (o + l > datagram.length) {
            throw new IllegalArgumentException("Out of the datagram range.");
        } else if (l >= values.length) {
            System.arraycopy(values, 0, datagram, o, values.length);
        } else {
            System.arraycopy(values, 0, datagram, o, l);
        }
    }

    public static void type(@NonNull byte[] datagram, int offset, int length, @NonNull byte[] values) {
        binaryType(datagram, offset, length, values);
    }

    public static void type(@NonNull byte[] datagram, int offset, @NonNull byte[] values) {
        binaryType(datagram, offset, values.length, values);
    }

    public static boolean booleanType(@NonNull final byte[] datagram, int byteOffset, int bitOffset) {
        if (bitOffset < BoolType.MIN_BIT_OFFSET || bitOffset > BoolType.MAX_BIT_OFFSET) {
            throw new IllegalArgumentException("Out of byte range.");
        }

        int o = reverse(datagram, byteOffset);

        return (datagram[o] & (0x01 << bitOffset)) != 0;
    }

    public static void booleanType(@NonNull final byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        if (bitOffset < BoolType.MIN_BIT_OFFSET || bitOffset > BoolType.MAX_BIT_OFFSET) {
            throw new IllegalArgumentException("Out of byte range.");
        }

        int o = reverse(datagram, byteOffset);

        if (value) {
            datagram[o] |= (0x01 << bitOffset);
        } else {
            datagram[o] &= ~(0x01 << bitOffset);
        }
    }

    public static void type(@NonNull final byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        booleanType(datagram, byteOffset, bitOffset, value);
    }

    public static byte byteType(@NonNull byte[] datagram, int offset) {
        int o = reverse(datagram, offset);

        return datagram[o];
    }

    public static void byteType(@NonNull final byte[] datagram, int offset, byte value) {
        int o = reverse(datagram, offset);

        datagram[o] = value;
    }

    public static void type(@NonNull final byte[] datagram, int offset, byte value) {
        byteType(datagram, offset, value);
    }

    public static char characterType(@NonNull byte[] datagram, int offset, EndianPolicy policy) {
        int o = reverse(datagram, offset);
        int value = 0;

        if (policy == EndianPolicy.BIG) {
            value = (datagram[o] & 0xFF) * 256 + (datagram[o + 1] & 0xFF);
        } else {
            value = (datagram[o + 1] & 0xFF) * 256 + (datagram[o] & 0xFF);
        }

        return (char) value;
    }

    public static char characterType(@NonNull byte[] datagram, int offset) {
        return characterType(datagram, offset, EndianPolicy.LITTLE);
    }

    public static void characterType(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull char value) {
        int o = reverse(datagram, offset);

        if (policy == EndianPolicy.BIG) {
            datagram[o] = (byte) (value >>> 8);
            datagram[o + 1] = (byte) value;
        } else {
            datagram[o + 1] = (byte) (value >>> 8);
            datagram[o] = (byte) value;
        }
    }

    public static void characterType(@NonNull byte[] datagram, int offset, @NonNull char value) {
        characterType(datagram, offset, EndianPolicy.LITTLE, value);
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull char value) {
        characterType(datagram, offset, policy, value);
    }

    public static int uinteger8Type(@NonNull final byte[] datagram, int offset) {
        int o = reverse(datagram, offset);

        return datagram[o] & 0xFF;
    }

    public static void uinteger8Type(@NonNull byte[] datagram, int offset, @NonNull int value) {
        if (value < UInt8Type.MIN_VALUE || value > UInt8Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uinteger8 range.");
        }

        int o = reverse(datagram, offset);

        datagram[o] = (byte) value;
    }

    public static int integer8Type(@NonNull final byte[] datagram, int offset) {
        int o = reverse(datagram, offset);

        return datagram[o];
    }

    public static void integer8Type(@NonNull byte[] datagram, int offset, @NonNull int value) {
        if (value < Int8Type.MIN_VALUE || value > Int8Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of integer8 range.");
        }

        int o = reverse(datagram, offset);

        datagram[o] = (byte) value;
    }

    public static int uinteger16Type(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        int o = reverse(datagram, offset);

        if (policy == EndianPolicy.BIG) {
            return (datagram[o] & 0xFF) * 256 + (datagram[o + 1] & 0xFF);
        } else {
            return (datagram[o + 1] & 0xFF) * 256 + (datagram[o] & 0xFF);
        }
    }

    public static int uinteger16Type(@NonNull final byte[] datagram, int offset) {
        return uinteger16Type(datagram, offset, EndianPolicy.LITTLE);
    }

    public static void uinteger16Type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull int value) {
        if (value < UInt16Type.MIN_VALUE || value > UInt16Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uinteger16 range.");
        }

        int o = reverse(datagram, offset);

        if (policy == EndianPolicy.BIG) {
            datagram[o + 1] = (byte) (value);
            datagram[o] = (byte) (value >>> 8);
        } else {
            datagram[o] = (byte) (value);
            datagram[o + 1] = (byte) (value >>> 8);
        }
    }

    public static void uinteger16Type(@NonNull byte[] datagram, int offset, @NonNull int value) {
        uinteger16Type(datagram, offset, EndianPolicy.LITTLE, value);
    }

    public static int integer16Type(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        int o = reverse(datagram, offset);
        short value = 0;

        if (policy == EndianPolicy.BIG) {
            value |= (datagram[o + 1] & 0x00FF);
            value |= (datagram[o] << 8);
        } else {
            value |= (datagram[o] & 0x00FF);
            value |= (datagram[o + 1] << 8);
        }

        return value;
    }

    public static int integer16Type(@NonNull final byte[] datagram, int offset) {
        return integer16Type(datagram, offset, EndianPolicy.LITTLE);
    }

    public static void integer16Type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull int value) {
        if (value < Int16Type.MIN_VALUE || value > Int16Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of integer16 range.");
        }

        int o = reverse(datagram, offset);

        if (policy == EndianPolicy.BIG) {
            datagram[o + 1] = (byte) (value);
            datagram[o] = (byte) (value >>> 8);
        } else {
            datagram[o] = (byte) (value);
            datagram[o + 1] = (byte) (value >>> 8);
        }
    }

    public static void integer16Type(@NonNull byte[] datagram, int offset, @NonNull int value) {
        integer16Type(datagram, offset, EndianPolicy.LITTLE, value);
    }

    public static short shortType(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        int o = reverse(datagram, offset);
        short value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[o] & 0x00FF);
            value |= (datagram[o + 1] << 8);
        } else if (policy == EndianPolicy.BIG) {
            value |= (datagram[o + 1] & 0x00FF);
            value |= (datagram[o] << 8);
        }

        return value;
    }

    public static short shortType(@NonNull final byte[] datagram, int offset) {
        return shortType(datagram, offset, EndianPolicy.LITTLE);
    }

    public static void shortType(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull short value) {
        int o = reverse(datagram, offset);

        if (policy == EndianPolicy.LITTLE) {
            datagram[o] = (byte) (value);
            datagram[o + 1] = (byte) (value >>> 8);
        } else if (policy == EndianPolicy.BIG) {
            datagram[o + 1] = (byte) (value);
            datagram[o] = (byte) (value >>> 8);
        }
    }

    public static void shortType(@NonNull byte[] datagram, int offset, @NonNull short value) {
        shortType(datagram, offset, EndianPolicy.LITTLE, value);
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull short value) {
        shortType(datagram, offset, policy, value);
    }

    public static int integerType(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        int o = reverse(datagram, offset);
        int value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[o] & 0xFF);
            value |= ((datagram[o + 1] & 0xFF) << 8);
            value |= ((datagram[o + 2] & 0xFF) << 16);
            value |= ((datagram[o + 3] & 0xFF) << 24);
        } else if (policy == EndianPolicy.BIG) {
            value |= (datagram[o + 3] & 0xFF);
            value |= ((datagram[o + 2] & 0xFF) << 8);
            value |= ((datagram[o + 1] & 0xFF) << 16);
            value |= ((datagram[o] & 0xFF) << 24);
        }

        return value;
    }

    public static int integerType(@NonNull final byte[] datagram, int offset) {
        return integerType(datagram, offset, EndianPolicy.LITTLE);
    }

    public static void integerType(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull int value) {
        int o = reverse(datagram, offset);

        if (policy == EndianPolicy.LITTLE) {
            datagram[o] = (byte) (value);
            datagram[o + 1] = (byte) (value >>> 8);
            datagram[o + 2] = (byte) (value >>> 16);
            datagram[o + 3] = (byte) (value >>> 24);
        } else if (policy == EndianPolicy.BIG) {
            datagram[o + 3] = (byte) (value);
            datagram[o + 2] = (byte) (value >>> 8);
            datagram[o + 1] = (byte) (value >>> 16);
            datagram[o] = (byte) (value >>> 24);
        }
    }

    public static void integerType(@NonNull byte[] datagram, int offset, @NonNull int value) {
        integerType(datagram, offset, EndianPolicy.LITTLE, value);
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull int value) {
        integerType(datagram, offset, policy, value);
    }

    public static long uinteger32Type(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        int o = reverse(datagram, offset);
        long value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[o] & 0xFF);
            value |= ((datagram[o + 1] & 0xFFL) << 8);
            value |= ((datagram[o + 2] & 0xFFL) << 16);
            value |= ((datagram[o + 3] & 0xFFL) << 24);
        } else if (policy == EndianPolicy.BIG) {
            value |= (datagram[o + 3] & 0xFF);
            value |= ((datagram[o + 2] & 0xFFL) << 8);
            value |= ((datagram[o + 1] & 0xFFL) << 16);
            value |= ((datagram[o] & 0xFFL) << 24);
        }

        return value;
    }

    public static long uinteger32Type(@NonNull final byte[] datagram, int offset) {
        return uinteger32Type(datagram, offset, EndianPolicy.LITTLE);
    }

    public static void uinteger32Type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull long value) {
        if (value < UInt32Type.MIN_VALUE || value > UInt32Type.MAX_VALUE) {
            throw new IllegalArgumentException("Out of uinteger32 range.");
        }

        int o = reverse(datagram, offset);

        if (policy == EndianPolicy.BIG) {
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

    public static void uinteger32Type(@NonNull byte[] datagram, int offset, @NonNull long value) {
        uinteger32Type(datagram, offset, EndianPolicy.LITTLE, value);
    }

    public static BigInteger uinteger64Type(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        int o = reverse(datagram, offset);
        long low = 0;
        long high = 0;

        if (policy == EndianPolicy.LITTLE) {
            low |= (datagram[o] & 0xFF);
            low |= ((datagram[o + 1] & 0xFFL) << 8);
            low |= ((datagram[o + 2] & 0xFFL) << 16);
            low |= ((datagram[o + 3] & 0xFFL) << 24);

            high |= (datagram[o + 4] & 0xFFL);
            high |= ((datagram[o + 5] & 0xFFL) << 8);
            high |= ((datagram[o + 6] & 0xFFL) << 16);
            high |= ((datagram[o + 7] & 0xFFL) << 24);
        } else if (policy == EndianPolicy.BIG) {
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

    public static BigInteger uinteger64Type(@NonNull final byte[] datagram, int offset) {
        return uinteger64Type(datagram, offset, EndianPolicy.LITTLE);
    }

    public static void uinteger64Type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull BigInteger value) {
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

        if (policy == EndianPolicy.BIG) {
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

    public static void uinteger64Type(@NonNull byte[] datagram, int offset, @NonNull BigInteger value) {
        uinteger64Type(datagram, offset, EndianPolicy.LITTLE, value);
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull BigInteger value) {
        uinteger64Type(datagram, offset, policy, value);
    }

    public static long longType(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        int o = reverse(datagram, offset);
        long value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[o] & 0xFF);
            value |= ((datagram[o + 1] & 0xFFL) << 8);
            value |= ((datagram[o + 2] & 0xFFL) << 16);
            value |= ((datagram[o + 3] & 0xFFL) << 24);

            value |= ((datagram[o + 4] & 0xFFL) << 32);
            value |= ((datagram[o + 5] & 0xFFL) << 40);
            value |= ((datagram[o + 6] & 0xFFL) << 48);
            value |= ((datagram[o + 7] & 0xFFL) << 56);
        } else if (policy == EndianPolicy.BIG) {
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

    public static long longType(@NonNull final byte[] datagram, int offset) {
        return longType(datagram, offset, EndianPolicy.LITTLE);
    }

    public static void longType(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull long value) {
        int o = reverse(datagram, offset);

        if (policy == EndianPolicy.BIG) {
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

    public static void longType(@NonNull byte[] datagram, int offset, @NonNull long value) {
        longType(datagram, offset, EndianPolicy.LITTLE, value);
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull long value) {
        longType(datagram, offset, policy, value);
    }

    public static float floatType(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        int o = reverse(datagram, offset);
        int value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[o] & 0xFF);
            value |= ((datagram[o + 1] & 0xFF) << 8);
            value |= ((datagram[o + 2] & 0xFF) << 16);
            value |= ((datagram[o + 3] & 0xFF) << 24);
        } else if (policy == EndianPolicy.BIG) {
            value |= (datagram[o + 3] & 0xFF);
            value |= ((datagram[o + 2] & 0xFF) << 8);
            value |= ((datagram[o + 1] & 0xFF) << 16);
            value |= ((datagram[o] & 0xFF) << 24);
        }

        return Float.intBitsToFloat(value);
    }

    public static float floatType(@NonNull final byte[] datagram, int offset) {
        return floatType(datagram, offset, EndianPolicy.LITTLE);
    }

    public static void floatType(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull float value) {
        int o = reverse(datagram, offset);
        int bits = Float.floatToIntBits(value);

        if (policy == EndianPolicy.LITTLE) {
            datagram[o] = (byte) bits;
            datagram[o + 1] = (byte) (bits >>> 8);
            datagram[o + 2] = (byte) (bits >>> 16);
            datagram[o + 3] = (byte) (bits >>> 24);
        } else if (policy == EndianPolicy.BIG) {
            datagram[o + 3] = (byte) (bits);
            datagram[o + 2] = (byte) (bits >>> 8);
            datagram[o + 1] = (byte) (bits >>> 16);
            datagram[o] = (byte) (bits >>> 24);
        }
    }

    public static void floatType(@NonNull byte[] datagram, int offset, @NonNull float value) {
        floatType(datagram, offset, EndianPolicy.LITTLE, value);
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull float value) {
        floatType(datagram, offset, policy, value);
    }

    public static double doubleType(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        int o = reverse(datagram, offset);
        long value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[o] & 0xFFL);
            value |= ((datagram[o + 1] & 0xFFL) << 8);
            value |= ((datagram[o + 2] & 0xFFL) << 16);
            value |= ((datagram[o + 3] & 0xFFL) << 24);

            value |= ((datagram[o + 4] & 0xFFL) << 32);
            value |= ((datagram[o + 5] & 0xFFL) << 40);
            value |= ((datagram[o + 6] & 0xFFL) << 48);
            value |= ((datagram[o + 7] & 0xFFL) << 56);
        } else if (policy == EndianPolicy.BIG) {
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

    public static double doubleType(@NonNull final byte[] datagram, int offset) {
        return doubleType(datagram, offset, EndianPolicy.LITTLE);
    }

    public static void doubleType(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull double value) {
        int o = reverse(datagram, offset);
        long bits = Double.doubleToLongBits(value);

        if (policy == EndianPolicy.BIG) {
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

    public static void doubleType(@NonNull byte[] datagram, int offset, @NonNull double value) {
        doubleType(datagram, offset, EndianPolicy.LITTLE, value);
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull double value) {
        doubleType(datagram, offset, policy, value);
    }
}
