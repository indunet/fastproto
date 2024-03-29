/*
 * Copyright 2019-2021 indunet
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

import lombok.val;
import org.indunet.fastproto.ByteOrder;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class BinaryUtils {
    public static byte[] valueOf(float value) {
        return valueOf(Float.floatToIntBits(value));
    }

    public static byte[] valueOf(float value, ByteOrder policy) {
        return valueOf(Float.floatToIntBits(value), policy);
    }

    public static byte[] valueOf(double value) {
        return valueOf(Double.doubleToRawLongBits(value));
    }

    public static byte[] valueOf(double value, ByteOrder policy) {
        return valueOf(Double.doubleToRawLongBits(value), policy);
    }

    public static byte[] valueOf(int value) {
        return valueOf(value, ByteOrder.LITTLE);
    }

    public static byte[] valueOf(int value, ByteOrder endian) {
        byte[] bytes = new byte[4];

        if (endian == ByteOrder.LITTLE) {
            bytes[0] = (byte) (value & 0xFF);
            bytes[1] = (byte) (value >> 8 & 0xFF);
            bytes[2] = (byte) (value >> 16 & 0xFF);
            bytes[3] = (byte) (value >> 24 & 0xFF);
        } else if (endian == ByteOrder.BIG) {
            bytes[3] = (byte) (value & 0xFF);
            bytes[2] = (byte) (value >> 8 & 0xFF);
            bytes[1] = (byte) (value >> 16 & 0xFF);
            bytes[0] = (byte) (value >> 24 & 0xFF);
        }

        return bytes;
    }

    public static byte[] valueOf(double[] values, ByteOrder policy) {
        val bytes = new byte[values.length * 8];

        IntStream.range(0, values.length)
                .forEach(i -> System.arraycopy(valueOf(values[i], policy), 0, bytes, i * 8, 8));

        return bytes;
    }

    public static byte[] valueOf(float[] values, ByteOrder policy) {
        val bytes = new byte[values.length * 4];

        IntStream.range(0, values.length)
                .forEach(i -> System.arraycopy(valueOf(values[i], policy), 0, bytes, i * 4, 4));

        return bytes;
    }

    public static byte[] int8Of(int[] values) {
        val bytes = new byte[values.length];

        IntStream.range(0, values.length)
                .forEach(i -> bytes[i] = (byte) values[i]);

        return bytes;
    }

    public static byte[] int16Of(int value, ByteOrder policy) {
        val bytes = new byte[2];

        if (policy == ByteOrder.LITTLE) {
            bytes[0] = (byte) (value % 256);
            bytes[1] = (byte) (value / 256);
        } else {
            bytes[0] = (byte) (value / 256);
            bytes[1] = (byte) (value % 256);
        }

        return bytes;
    }

    public static byte[] int16Of(int[] values, ByteOrder policy) {
        val bytes = new byte[values.length * 2];

        IntStream.range(0, values.length)
                .forEach(i -> {
                    if (policy == ByteOrder.LITTLE) {
                        bytes[i * 2] = (byte) (values[i] & 0xFF);
                        bytes[i * 2 + 1] = (byte) (values[i] >> 8 & 0xFF);
                    } else if (policy == ByteOrder.BIG) {
                        bytes[i * 2 + 1] = (byte) (values[i] & 0xFF);
                        bytes[i * 2] = (byte) (values[i] >> 8 & 0xFF);
                    }
                });

        return bytes;
    }

    public static byte[] valueOf(Byte[] bytes) {
        val bs = new byte[bytes.length];

        IntStream.range(0, bytes.length)
                .forEach(i -> bs[i] = bytes[i]);

        return bs;
    }

    public static byte[] valueOf(Short[] shorts, ByteOrder policy) {
        val ss = new short[shorts.length];

        IntStream.range(0, shorts.length)
                .forEach(i -> ss[i] = shorts[i]);

        return valueOf(ss, policy);
    }

    public static byte[] valueOf(Float[] floats, ByteOrder policy) {
        val fs = new float[floats.length];

        IntStream.range(0, floats.length)
                .forEach(i -> fs[i] = floats[i]);

        return valueOf(fs, policy);
    }

    public static byte[] valueOf(short[] values, ByteOrder policy) {
        val bytes = new byte[values.length * 2];

        IntStream.range(0, values.length)
                .forEach(i -> {
                    if (policy == ByteOrder.LITTLE) {
                        bytes[i * 2] = (byte) (values[i] & 0xFF);
                        bytes[i * 2 + 1] = (byte) (values[i] >> 8 & 0xFF);
                    } else if (policy == ByteOrder.BIG) {
                        bytes[i * 2 + 1] = (byte) (values[i] & 0xFF);
                        bytes[i * 2] = (byte) (values[i] >> 8 & 0xFF);
                    }
                });

        return bytes;
    }

    public static byte[] int32Of(int[] values, ByteOrder policy) {
        val bytes = new byte[values.length * 4];

        IntStream.range(0, values.length)
                .forEach(i -> System.arraycopy(valueOf(values[i], policy), 0, bytes, i * 4, 4));

        return bytes;
    }

    public static byte[] uint16Of(int[] values, ByteOrder policy) {
        val bytes = new byte[values.length * 2];

        IntStream.range(0, values.length)
                .forEach(i -> {
                    if (policy == ByteOrder.BIG) {
                        bytes[i * 2 + 1] = (byte) values[i];
                        bytes[i * 2] = (byte) (values[i] >>> 8);
                    } else {
                        bytes[i * 2] = (byte) values[i];
                        bytes[i * 2 + 1] = (byte) (values[i] >>> 8);
                    }
                });

        return bytes;
    }

    public static byte[] valueOf(long[] values, ByteOrder policy) {
        val bytes = new byte[values.length * 8];

        IntStream.range(0, values.length)
                .forEach(i -> System.arraycopy(valueOf(values[i], policy), 0, bytes, i * 8, 8));

        return bytes;
    }

    public static byte[] uint32Of(long[] values, ByteOrder policy) {
        val bytes = new byte[values.length * 4];

        IntStream.range(0, values.length)
                .forEach(i -> System.arraycopy(uint32of(values[i], policy), 0, bytes, i * 4, 4));

        return bytes;
    }

    public static byte[] uint64Of(BigInteger[] values, ByteOrder policy) {
        val bytes = new byte[values.length * 8];

        IntStream.range(0, values.length)
                .forEach(i -> {
                    val buf = new byte[8];

                    EncodeUtils.writeUInt64(buf, 0, policy, values[i]);
                    System.arraycopy(buf, 0, bytes, i * 8, 8);
                });

        return bytes;
    }

    public static byte[] uint8Of(int[] values) {
        val bytes = new byte[values.length];

        IntStream.range(0, values.length)
                .forEach(i -> bytes[i] = (byte) (values[i] & 0xFF));

        return bytes;
    }

    public static byte[] uint32of(long value) {
        return uint32of(value, ByteOrder.LITTLE);
    }

    public static byte[] uint32of(long value, ByteOrder policy) {
        byte[] bytes = new byte[4];

        if (policy == ByteOrder.LITTLE) {
            bytes[0] = (byte) (value & 0xFF);
            bytes[1] = (byte) (value >> 8 & 0xFF);
            bytes[2] = (byte) (value >> 16 & 0xFF);
            bytes[3] = (byte) (value >> 24 & 0xFF);
        } else if (policy == ByteOrder.BIG) {
            bytes[3] = (byte) (value & 0xFF);
            bytes[2] = (byte) (value >> 8 & 0xFF);
            bytes[1] = (byte) (value >> 16 & 0xFF);
            bytes[0] = (byte) (value >> 24 & 0xFF);
        }

        return bytes;
    }

    public static byte[] valueOf(long value) {
        return valueOf(value, ByteOrder.LITTLE);
    }

    public static byte[] valueOf(long value, ByteOrder endian) {
        byte[] bytes = new byte[8];

        if (endian == ByteOrder.LITTLE) {
            bytes[0] |= (value & 0xFFL);
            bytes[1] = (byte) (value >> 8 & 0xFFL);
            bytes[2] = (byte) (value >> 16 & 0xFFL);
            bytes[3] = (byte) (value >> 24 & 0xFFL);
            bytes[4] = (byte) (value >> 32 & 0xFFL);
            bytes[5] = (byte) (value >> 40 & 0xFFL);
            bytes[6] = (byte) (value >> 48 & 0xFFL);
            bytes[7] = (byte) (value >> 56 & 0xFFL);
        } else if (endian == ByteOrder.BIG) {
            bytes[7] = (byte) (value & 0xFF);
            bytes[6] = (byte) (value >> 8 & 0xFFL);
            bytes[5] = (byte) (value >> 16 & 0xFFL);
            bytes[4] = (byte) (value >> 24 & 0xFFL);
            bytes[3] = (byte) (value >> 32 & 0xFFL);
            bytes[2] = (byte) (value >> 40 & 0xFFL);
            bytes[1] = (byte) (value >> 48 & 0xFFL);
            bytes[0] = (byte) (value >> 56 & 0xFFL);
        }

        return bytes;
    }
}
