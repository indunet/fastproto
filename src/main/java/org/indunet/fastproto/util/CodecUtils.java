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
import org.indunet.fastproto.annotation.type.UInteger32Type;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Deng Ran
 * @since 2.5.0
 */
public class CodecUtils {
    public static byte[] binaryType(@NonNull final byte[] datagram, int offset, int length) {
        return Arrays.copyOfRange(datagram, offset, offset + length);
    }

    public static void binaryType(@NonNull byte[] datagram, int offset, int length, @NonNull byte[] values) {
        if (length >= values.length) {
            System.arraycopy(values, 0, datagram, offset, values.length);
        } else {
            System.arraycopy(values, 0, datagram, offset, length);
        }
    }

    public static void type(@NonNull byte[] datagram, int offset, int length, @NonNull byte[] values) {
        binaryType(datagram, offset, length, values);
    }

    public static void type(@NonNull byte[] datagram, int offset, @NonNull byte[] values) {
        binaryType(datagram, offset, values.length, values);
    }

    public static boolean booleanType(@NonNull final byte[] datagram, int byteOffset, int bitOffset) {
        return (datagram[byteOffset] & (0x01 << bitOffset)) != 0;
    }

    public static void booleanType(@NonNull final byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        if (value) {
            datagram[byteOffset] |= (0x01 << bitOffset);
        } else {
            datagram[byteOffset] &= ~(0x01 << bitOffset);
        }
    }

    public static void type(@NonNull final byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        booleanType(datagram, byteOffset, bitOffset, value);
    }

    public static byte byteType(@NonNull byte[] datagram, int offset) {
        return datagram[offset];
    }

    public static void byteType(@NonNull final byte[] datagram, int offset, byte value) {
        datagram[offset] = value;
    }

    public static void type(@NonNull final byte[] datagram, int offset, byte value) {
        byteType(datagram, offset, value);
    }

    public static char characterType(@NonNull byte[] datagram, int offset, EndianPolicy policy) {
        int value = 0;

        if (policy == EndianPolicy.BIG) {
            value = (datagram[offset] & 0xFF) * 256 + (datagram[offset + 1] & 0xFF);
        } else {
            value = (datagram[offset + 1] & 0xFF) * 256 + (datagram[offset] & 0xFF);
        }

        return (char) value;
    }

    public static void characterType(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull char value) {
        if (policy == EndianPolicy.BIG) {
            datagram[offset] = (byte) (value >>> 8);
            datagram[offset + 1] = (byte) value;
        } else {
            datagram[offset + 1] = (byte) (value >>> 8);
            datagram[offset] = (byte) value;
        }
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull char value) {
        characterType(datagram, offset, policy, value);
    }

    public static int uinteger8Type(@NonNull final byte[] datagram, int offset) {
        return datagram[offset] & 0xFF;
    }

    public static void uinteger8Type(@NonNull byte[] datagram, int offset, @NonNull int value) {
        datagram[offset] = (byte) value;
    }

    public static int integer8Type(@NonNull final byte[] datagram, int offset) {
        return datagram[offset];
    }

    public static void integer8Type(@NonNull byte[] datagram, int offset, @NonNull int value) {
        datagram[offset] = (byte) value;
    }

    public static int uinteger16Type(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        if (policy == EndianPolicy.BIG) {
            return (datagram[offset] & 0xFF) * 256 + (datagram[offset + 1] & 0xFF);
        } else {
            return (datagram[offset + 1] & 0xFF) * 256 + (datagram[offset] & 0xFF);
        }
    }

    public static void uinteger16Type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull int value) {
        if (policy == EndianPolicy.BIG) {
            datagram[offset + 1] = (byte) (value);
            datagram[offset] = (byte) (value >>> 8);
        } else {
            datagram[offset] = (byte) (value);
            datagram[offset + 1] = (byte) (value >>> 8);
        }
    }

    public static int integer16Type(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        short value = 0;

        if (policy == EndianPolicy.BIG) {
            value |= (datagram[offset + 1] & 0x00FF);
            value |= (datagram[offset] << 8);
        } else {
            value |= (datagram[offset] & 0x00FF);
            value |= (datagram[offset + 1] << 8);
        }

        return value;
    }

    public static void integer16Type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull int value) {
        if (policy == EndianPolicy.BIG) {
            datagram[offset + 1] = (byte) (value);
            datagram[offset] = (byte) (value >>> 8);
        } else {
            datagram[offset] = (byte) (value);
            datagram[offset + 1] = (byte) (value >>> 8);
        }
    }

    public static short shortType(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        short value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[offset] & 0x00FF);
            value |= (datagram[offset + 1] << 8);
        } else if (policy == EndianPolicy.BIG) {
            value |= (datagram[offset + 1] & 0x00FF);
            value |= (datagram[offset] << 8);
        }

        return value;
    }

    public static void shortType(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull short value) {
        if (policy == EndianPolicy.LITTLE) {
            datagram[offset] = (byte) (value);
            datagram[offset + 1] = (byte) (value >>> 8);
        } else if (policy == EndianPolicy.BIG) {
            datagram[offset + 1] = (byte) (value);
            datagram[offset] = (byte) (value >>> 8);
        }
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull short value) {
        shortType(datagram, offset, policy, value);
    }

    public static int integerType(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        int value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[offset] & 0xFF);
            value |= ((datagram[offset + 1] & 0xFF) << 8);
            value |= ((datagram[offset + 2] & 0xFF) << 16);
            value |= ((datagram[offset + 3] & 0xFF) << 24);
        } else if (policy == EndianPolicy.BIG) {
            value |= (datagram[offset + 3] & 0xFF);
            value |= ((datagram[offset + 2] & 0xFF) << 8);
            value |= ((datagram[offset + 1] & 0xFF) << 16);
            value |= ((datagram[offset] & 0xFF) << 24);
        }

        return value;
    }

    public static void integerType(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull int value) {
        if (policy == EndianPolicy.LITTLE) {
            datagram[offset] = (byte) (value);
            datagram[offset + 1] = (byte) (value >>> 8);
            datagram[offset + 2] = (byte) (value >>> 16);
            datagram[offset + 3] = (byte) (value >>> 24);
        } else if (policy == EndianPolicy.BIG) {
            datagram[offset + 3] = (byte) (value);
            datagram[offset + 2] = (byte) (value >>> 8);
            datagram[offset + 1] = (byte) (value >>> 16);
            datagram[offset] = (byte) (value >>> 24);
        }
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull int value) {
        integerType(datagram, offset, policy, value);
    }

    public static long uinteger32Type(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        long value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[offset] & 0xFF);
            value |= ((datagram[offset + 1] & 0xFFL) << 8);
            value |= ((datagram[offset + 2] & 0xFFL) << 16);
            value |= ((datagram[offset + 3] & 0xFFL) << 24);
        } else if (policy == EndianPolicy.BIG) {
            value |= (datagram[offset + 3] & 0xFF);
            value |= ((datagram[offset + 2] & 0xFFL) << 8);
            value |= ((datagram[offset + 1] & 0xFFL) << 16);
            value |= ((datagram[offset] & 0xFFL) << 24);
        }

        return value;
    }

    public static void uinteger32Type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull long value) {
        if (policy == EndianPolicy.BIG) {
            datagram[offset + 3] = (byte) (value);
            datagram[offset + 2] = (byte) (value >>> 8);
            datagram[offset + 1] = (byte) (value >>> 16);
            datagram[offset] = (byte) (value >>> 24);
        } else {
            datagram[offset] = (byte) (value);
            datagram[offset + 1] = (byte) (value >>> 8);
            datagram[offset + 2] = (byte) (value >>> 16);
            datagram[offset + 3] = (byte) (value >>> 24);
        }
    }

    public static BigInteger uinteger64Type(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        long low = 0;
        long high = 0;

        if (policy == EndianPolicy.LITTLE) {
            low |= (datagram[offset] & 0xFF);
            low |= ((datagram[offset + 1] & 0xFFL) << 8);
            low |= ((datagram[offset + 2] & 0xFFL) << 16);
            low |= ((datagram[offset + 3] & 0xFFL) << 24);

            high |= (datagram[offset + 4] & 0xFFL);
            high |= ((datagram[offset + 5] & 0xFFL) << 8);
            high |= ((datagram[offset + 6] & 0xFFL) << 16);
            high |= ((datagram[offset + 7] & 0xFFL) << 24);
        } else if (policy == EndianPolicy.BIG) {
            low |= (datagram[offset + 7] & 0xFF);
            low |= ((datagram[offset + 6] & 0xFFL) << 8);
            low |= ((datagram[offset + 5] & 0xFFL) << 16);
            low |= ((datagram[offset + 4] & 0xFFL) << 24);

            high |= (datagram[offset + 3] & 0xFFL);
            high |= ((datagram[offset + 2] & 0xFFL) << 8);
            high |= ((datagram[offset + 1] & 0xFFL) << 16);
            high |= ((datagram[offset] & 0xFFL) << 24);
        }

        return new BigInteger(String.valueOf(high))
                .multiply(new BigInteger(String.valueOf(UInteger32Type.MAX_VALUE + 1)))
                .add(new BigInteger(String.valueOf(low)));
    }

    public static void uinteger64Type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull BigInteger value) {
        long low = value
                .and(new BigInteger(String.valueOf(0xFFFF_FFFFL)))
                .longValueExact();
        long high = value
                .shiftRight(32)
                .longValueExact();

        if (policy == EndianPolicy.BIG) {
            datagram[offset + 7] = (byte) (low);
            datagram[offset + 6] = (byte) (low >>> 8);
            datagram[offset + 5] = (byte) (low >>> 16);
            datagram[offset + 4] = (byte) (low >>> 24);

            datagram[offset + 3] = (byte) (high);
            datagram[offset + 2] = (byte) (high >>> 8);
            datagram[offset + 1] = (byte) (high >>> 16);
            datagram[offset] = (byte) (high >>> 24);
        } else {
            datagram[offset] = (byte) (low);
            datagram[offset + 1] = (byte) (low >>> 8);
            datagram[offset + 2] = (byte) (low >>> 16);
            datagram[offset + 3] = (byte) (low >>> 24);

            datagram[offset + 4] = (byte) (high);
            datagram[offset + 5] = (byte) (high >>> 8);
            datagram[offset + 6] = (byte) (high >>> 16);
            datagram[offset + 7] = (byte) (high >>> 24);
        }
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull BigInteger value) {
        uinteger64Type(datagram, offset, policy, value);
    }

    public static long longType(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        long value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[offset] & 0xFF);
            value |= ((datagram[offset + 1] & 0xFFL) << 8);
            value |= ((datagram[offset + 2] & 0xFFL) << 16);
            value |= ((datagram[offset + 3] & 0xFFL) << 24);

            value |= ((datagram[offset + 4] & 0xFFL) << 32);
            value |= ((datagram[offset + 5] & 0xFFL) << 40);
            value |= ((datagram[offset + 6] & 0xFFL) << 48);
            value |= ((datagram[offset + 7] & 0xFFL) << 56);
        } else if (policy == EndianPolicy.BIG) {
            value |= (datagram[offset + 7] & 0xFF);
            value |= ((datagram[offset + 6] & 0xFFL) << 8);
            value |= ((datagram[offset + 5] & 0xFFL) << 16);
            value |= ((datagram[offset + 4] & 0xFFL) << 24);

            value |= ((datagram[offset + 3] & 0xFFL) << 32);
            value |= ((datagram[offset + 2] & 0xFFL) << 40);
            value |= ((datagram[offset + 1] & 0xFFL) << 48);
            value |= ((datagram[offset] & 0xFFL) << 56);
        }

        return value;
    }

    public static void longType(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull long value) {
        if (policy == EndianPolicy.BIG) {
            datagram[offset + 7] = (byte) (value);
            datagram[offset + 6] = (byte) (value >>> 8);
            datagram[offset + 5] = (byte) (value >>> 16);
            datagram[offset + 4] = (byte) (value >>> 24);

            datagram[offset + 3] = (byte) (value >>> 32);
            datagram[offset + 2] = (byte) (value >>> 40);
            datagram[offset + 1] = (byte) (value >>> 48);
            datagram[offset] = (byte) (value >>> 56);
        } else {
            datagram[offset] = (byte) (value);
            datagram[offset + 1] = (byte) (value >>> 8);
            datagram[offset + 2] = (byte) (value >>> 16);
            datagram[offset + 3] = (byte) (value >>> 24);

            datagram[offset + 4] = (byte) (value >>> 32);
            datagram[offset + 5] = (byte) (value >>> 40);
            datagram[offset + 6] = (byte) (value >>> 48);
            datagram[offset + 7] = (byte) (value >>> 56);
        }
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull long value) {
        longType(datagram, offset, policy, value);
    }

    public static float floatType(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        int value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[offset] & 0xFF);
            value |= ((datagram[offset + 1] & 0xFF) << 8);
            value |= ((datagram[offset + 2] & 0xFF) << 16);
            value |= ((datagram[offset + 3] & 0xFF) << 24);
        } else if (policy == EndianPolicy.BIG) {
            value |= (datagram[offset + 3] & 0xFF);
            value |= ((datagram[offset + 2] & 0xFF) << 8);
            value |= ((datagram[offset + 1] & 0xFF) << 16);
            value |= ((datagram[offset] & 0xFF) << 24);
        }

        return Float.intBitsToFloat(value);
    }

    public static void floatType(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull float value) {
        int bits = Float.floatToIntBits(value);

        if (policy == EndianPolicy.LITTLE) {
            datagram[offset] = (byte) bits;
            datagram[offset + 1] = (byte) (bits >>> 8);
            datagram[offset + 2] = (byte) (bits >>> 16);
            datagram[offset + 3] = (byte) (bits >>> 24);
        } else if (policy == EndianPolicy.BIG) {
            datagram[offset + 3] = (byte) (bits);
            datagram[offset + 2] = (byte) (bits >>> 8);
            datagram[offset + 1] = (byte) (bits >>> 16);
            datagram[offset] = (byte) (bits >>> 24);
        }
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull float value) {
        floatType(datagram, offset, policy, value);
    }

    public static double doubleType(@NonNull final byte[] datagram, int offset, EndianPolicy policy) {
        long value = 0;

        if (policy == EndianPolicy.LITTLE) {
            value |= (datagram[offset] & 0xFFL);
            value |= ((datagram[offset + 1] & 0xFFL) << 8);
            value |= ((datagram[offset + 2] & 0xFFL) << 16);
            value |= ((datagram[offset + 3] & 0xFFL) << 24);

            value |= ((datagram[offset + 4] & 0xFFL) << 32);
            value |= ((datagram[offset + 5] & 0xFFL) << 40);
            value |= ((datagram[offset + 6] & 0xFFL) << 48);
            value |= ((datagram[offset + 7] & 0xFFL) << 56);
        } else if (policy == EndianPolicy.BIG) {
            value |= (datagram[offset + 7] & 0xFFL);
            value |= ((datagram[offset + 6] & 0xFFL) << 8);
            value |= ((datagram[offset + 5] & 0xFFL) << 16);
            value |= ((datagram[offset + 4] & 0xFFL) << 24);

            value |= ((datagram[offset + 3] & 0xFFL) << 32);
            value |= ((datagram[offset + 2] & 0xFFL) << 40);
            value |= ((datagram[offset + 1] & 0xFFL) << 48);
            value |= ((datagram[offset] & 0xFFL) << 56);
        }

        return Double.longBitsToDouble(value);
    }

    public static void doubleType(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull double value) {
        long bits = Double.doubleToLongBits(value);

        if (policy == EndianPolicy.BIG) {
            datagram[offset + 7] = (byte) (bits);
            datagram[offset + 6] = (byte) (bits >>> 8);
            datagram[offset + 5] = (byte) (bits >>> 16);
            datagram[offset + 4] = (byte) (bits >>> 24);

            datagram[offset + 3] = (byte) (bits >>> 32);
            datagram[offset + 2] = (byte) (bits >>> 40);
            datagram[offset + 1] = (byte) (bits >>> 48);
            datagram[offset] = (byte) (bits >>> 56);
        } else {
            datagram[offset] = (byte) (bits);
            datagram[offset + 1] = (byte) (bits >>> 8);
            datagram[offset + 2] = (byte) (bits >>> 16);
            datagram[offset + 3] = (byte) (bits >>> 24);

            datagram[offset + 4] = (byte) (bits >>> 32);
            datagram[offset + 5] = (byte) (bits >>> 40);
            datagram[offset + 6] = (byte) (bits >>> 48);
            datagram[offset + 7] = (byte) (bits >>> 56);
        }
    }

    public static void type(@NonNull byte[] datagram, int offset, EndianPolicy policy, @NonNull double value) {
        doubleType(datagram, offset, policy, value);
    }
}
