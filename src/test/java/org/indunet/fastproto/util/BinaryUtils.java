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

import org.indunet.fastproto.EndianPolicy;

public class BinaryUtils {
    public static byte[] valueOf(float value) {
        return valueOf(Float.floatToIntBits(value));
    }

    public static byte[] valueOf(float value, EndianPolicy policy) {
        return valueOf(Float.floatToIntBits(value), policy);
    }

    public static byte[] valueOf(double value) {
        return valueOf(Double.doubleToLongBits(value));
    }

    public static byte[] valueOf(double value, EndianPolicy policy) {
        return valueOf(Double.doubleToLongBits(value), policy);
    }

    public static byte[] valueOf(int value) {
        return valueOf(value, EndianPolicy.LITTLE);
    }

    public static byte[] valueOf(int value, EndianPolicy endian) {
        byte[] bytes = new byte[4];

        if (endian == EndianPolicy.LITTLE) {
            bytes[0] = (byte)(value & 0xFF);
            bytes[1] = (byte)(value >> 8 & 0xFF);
            bytes[2] = (byte)(value >> 16 & 0xFF);
            bytes[3] = (byte)(value >> 24 & 0xFF);
        } else if (endian == EndianPolicy.BIG) {
            bytes[3] = (byte)(value & 0xFF);
            bytes[2] = (byte)(value >> 8 & 0xFF);
            bytes[1] = (byte)(value >> 16 & 0xFF);
            bytes[0] = (byte)(value >> 24 & 0xFF);
        }

        return bytes;
    }

    public static byte[] uint32of(long value) {
        return uint32of(value, EndianPolicy.LITTLE);
    }

    public static byte[] uint32of(long value, EndianPolicy policy) {
        byte[] bytes = new byte[4];

        if (policy == EndianPolicy.LITTLE) {
            bytes[0] = (byte)(value & 0xFF);
            bytes[1] = (byte)(value >> 8 & 0xFF);
            bytes[2] = (byte)(value >> 16 & 0xFF);
            bytes[3] = (byte)(value >> 24 & 0xFF);
        } else if (policy == EndianPolicy.BIG) {
            bytes[7] = (byte)(value & 0xFF);
            bytes[6] = (byte)(value >> 8 & 0xFF);
            bytes[5] = (byte)(value >> 16 & 0xFF);
            bytes[4] = (byte)(value >> 24 & 0xFF);
        }

        return bytes;
    }

    public static byte[] valueOf(long value) {
        return valueOf(value, EndianPolicy.LITTLE);
    }

    public static byte[] valueOf(long value, EndianPolicy endian) {
        byte[] bytes = new byte[8];

        if (endian == EndianPolicy.LITTLE) {
            bytes[0] |= (value & 0xFFL);
            bytes[1] = (byte)(value >> 8 & 0xFFL);
            bytes[2] = (byte)(value >> 16 & 0xFFL);
            bytes[3] = (byte)(value >> 24 & 0xFFL);
            bytes[4] = (byte)(value >> 32 & 0xFFL);
            bytes[5] = (byte)(value >> 40 & 0xFFL);
            bytes[6] = (byte)(value >> 48 & 0xFFL);
            bytes[7] = (byte)(value >> 56 & 0xFFL);
        } else if (endian == EndianPolicy.BIG) {
            bytes[7] = (byte)(value & 0xFF);
            bytes[6] = (byte)(value >> 8 & 0xFFL);
            bytes[5] = (byte)(value >> 16 & 0xFFL);
            bytes[4] = (byte)(value >> 24 & 0xFFL);
            bytes[3] = (byte)(value >> 32 & 0xFFL);
            bytes[2] = (byte)(value >> 40 & 0xFFL);
            bytes[1] = (byte)(value >> 48 & 0xFFL);
            bytes[0] = (byte)(value >> 56 & 0xFFL);
        }

        return bytes;
    }
}
