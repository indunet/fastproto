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

package org.indunet.fastproto.util;

import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.exception.DecodingException;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Decode utils which supplies some methods for decoding.
 *
 * @author Deng Ran
 * @since 3.10.1
 */
public class DecodeUtils {
    /**
     * Decode bool value from binary data.
     *
     * @param bytes The binary data.
     * @param byteOffset The byte offset of the field in the binary data.
     * @param bitOffset The bit offset of the field in the binary data.
     * @return Decoded boolean value.
     */
    public static boolean readBool(byte[] bytes, int byteOffset, int bitOffset) {
        return readBool(bytes, byteOffset, bitOffset, BitOrder.LSB_0);
    }

    /**
     * Decode bool value from binary data.
     *
     * @param bytes The binary data.
     * @param byteOffset The byte offset of the field in the binary data.
     * @param bitOffset The bit offset of the field in the binary data.
     * @param order The bit order of the field in the binary data.
     * @return Decoded boolean value.
     */
    public static boolean readBool(byte[] bytes, int byteOffset, int bitOffset, BitOrder order) {
        if (bitOffset < BoolType.BIT_0 || bitOffset > BoolType.BIT_7) {
            throw new DecodingException("Out of byte range.");
        }

        int o = ReverseUtils.reverse(bytes, byteOffset);

        if (order == BitOrder.MSB_0) {
            bitOffset = 7 - bitOffset;
        }

        return (bytes[o] & (0x01 << bitOffset)) != 0;
    }

    /**
     * Decode byte value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @return Decoded byte value.
     */
    public static byte readByte(byte[] bytes, int offset) {
        int o = ReverseUtils.reverse(bytes, offset);

        return bytes[o];
    }

    /**
     * Decode short value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @return Decoded short value.
     */
    public static short readShort(byte[] bytes, int offset) {
        return readShort(bytes, offset, ByteOrder.LITTLE);
    }

    /**
     * Decode short value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @return Decoded short value.
     */
    public static short readShort(byte[] bytes, int offset, ByteOrder order) {
        int o = ReverseUtils.reverse(bytes, offset);
        short value = 0;

        if (order == ByteOrder.LITTLE) {
            value |= (bytes[o] & 0x00FF);
            value |= (bytes[o + 1] << 8);
        } else if (order == ByteOrder.BIG) {
            value |= (bytes[o + 1] & 0x00FF);
            value |= (bytes[o] << 8);
        }

        return value;
    }

    /**
     * Decode int8 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @return Decoded int8 value.
     */
    public static int readInt8(byte[] bytes, int offset) {
        int o = ReverseUtils.reverse(bytes, offset);

        return bytes[o];
    }

    /**
     * Decode int16 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @return Decoded int16 value.
     */
    public static int readInt16(byte[] bytes, int offset) {
        return readInt16(bytes, offset, ByteOrder.LITTLE);
    }

    /**
     * Decode int16 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @return Decoded int16 value.
     */
    public static int readInt16(byte[] bytes, int offset, ByteOrder order) {
        int o = ReverseUtils.reverse(bytes, offset);
        short value = 0;

        if (order == ByteOrder.BIG) {
            value |= (bytes[o + 1] & 0x00FF);
            value |= (bytes[o] << 8);
        } else {
            value |= (bytes[o] & 0x00FF);
            value |= (bytes[o + 1] << 8);
        }

        return value;
    }

    /**
     * Decode int32 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @return Decoded int32 value.
     */
    public static int readInt32(byte[] bytes, int offset) {
        return readInt32(bytes, offset, ByteOrder.LITTLE);
    }

    /**
     * Decode int32 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @return Decoded int32 value.
     */
    public static int readInt32(byte[] bytes, int offset, ByteOrder order) {
        int o = ReverseUtils.reverse(bytes, offset);
        int value = 0;

        if (order == ByteOrder.LITTLE) {
            value |= (bytes[o] & 0xFF);
            value |= ((bytes[o + 1] & 0xFF) << 8);
            value |= ((bytes[o + 2] & 0xFF) << 16);
            value |= ((bytes[o + 3] & 0xFF) << 24);
        } else if (order == ByteOrder.BIG) {
            value |= (bytes[o + 3] & 0xFF);
            value |= ((bytes[o + 2] & 0xFF) << 8);
            value |= ((bytes[o + 1] & 0xFF) << 16);
            value |= ((bytes[o] & 0xFF) << 24);
        }

        return value;
    }

    /**
     * Decode int64 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @return Decoded int64 value.
     */
    public static long readInt64(byte[] bytes, int offset) {
        return readInt64(bytes, offset, ByteOrder.LITTLE);
    }

    /**
     * Decode int64 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @return Decoded int64 value.
     */
    public static long readInt64(byte[] bytes, int offset, ByteOrder order) {
        int o = ReverseUtils.reverse(bytes, offset);
        long value = 0;

        if (order == ByteOrder.LITTLE) {
            value |= (bytes[o] & 0xFF);
            value |= ((bytes[o + 1] & 0xFFL) << 8);
            value |= ((bytes[o + 2] & 0xFFL) << 16);
            value |= ((bytes[o + 3] & 0xFFL) << 24);

            value |= ((bytes[o + 4] & 0xFFL) << 32);
            value |= ((bytes[o + 5] & 0xFFL) << 40);
            value |= ((bytes[o + 6] & 0xFFL) << 48);
            value |= ((bytes[o + 7] & 0xFFL) << 56);
        } else if (order == ByteOrder.BIG) {
            value |= (bytes[o + 7] & 0xFF);
            value |= ((bytes[o + 6] & 0xFFL) << 8);
            value |= ((bytes[o + 5] & 0xFFL) << 16);
            value |= ((bytes[o + 4] & 0xFFL) << 24);

            value |= ((bytes[o + 3] & 0xFFL) << 32);
            value |= ((bytes[o + 2] & 0xFFL) << 40);
            value |= ((bytes[o + 1] & 0xFFL) << 48);
            value |= ((bytes[o] & 0xFFL) << 56);
        }

        return value;
    }

    /**
     * Decode uint8 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @return Decoded uint8 value.
     */
    public static int readUInt8(byte[] bytes, int offset) {
        int o = ReverseUtils.reverse(bytes, offset);

        return bytes[o] & 0xFF;
    }

    /**
     * Decode uint16 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @return Decoded uint16 value.
     */
    public static int readUInt16(byte[] bytes, int offset) {
        return readUInt16(bytes, offset, ByteOrder.LITTLE);
    }

    /**
     * Decode uint16 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @return Decoded uint16 value.
     */
    public static int readUInt16(byte[] bytes, int offset, ByteOrder order) {
        int o = ReverseUtils.reverse(bytes, offset);

        if (order == ByteOrder.BIG) {
            return (bytes[o] & 0xFF) * 256 + (bytes[o + 1] & 0xFF);
        } else {
            return (bytes[o + 1] & 0xFF) * 256 + (bytes[o] & 0xFF);
        }
    }

    /**
     * Decode uint32 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @return Decoded uint32 value.
     */
    public static long readUInt32(byte[] bytes, int offset) {
        return readInt32(bytes, offset, ByteOrder.LITTLE) & 0xFFFF_FFFFL;
    }

    /**
     * Decode uint32 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @return Decoded uint32 value.
     */
    public static long readUInt32(byte[] bytes, int offset, ByteOrder order) {
        int o = ReverseUtils.reverse(bytes, offset);
        long value = 0;

        if (order == ByteOrder.LITTLE) {
            value |= (bytes[o] & 0xFF);
            value |= ((bytes[o + 1] & 0xFFL) << 8);
            value |= ((bytes[o + 2] & 0xFFL) << 16);
            value |= ((bytes[o + 3] & 0xFFL) << 24);
        } else if (order == ByteOrder.BIG) {
            value |= (bytes[o + 3] & 0xFF);
            value |= ((bytes[o + 2] & 0xFFL) << 8);
            value |= ((bytes[o + 1] & 0xFFL) << 16);
            value |= ((bytes[o] & 0xFFL) << 24);
        }

        return value;
    }

    /**
     * Decode uint64 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @return Decoded uint64 value.
     */
    public static BigInteger readUInt64(byte[] bytes, int offset) {
        return readUInt64(bytes, offset, ByteOrder.LITTLE);
    }

    /**
     * Decode uint64 value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @return Decoded uint64 value.
     */
    public static BigInteger readUInt64(byte[] bytes, int offset, ByteOrder order) {
        int o = ReverseUtils.reverse(bytes, offset);
        long low = 0;
        long high = 0;

        if (order == ByteOrder.LITTLE) {
            low |= (bytes[o] & 0xFF);
            low |= ((bytes[o + 1] & 0xFFL) << 8);
            low |= ((bytes[o + 2] & 0xFFL) << 16);
            low |= ((bytes[o + 3] & 0xFFL) << 24);

            high |= (bytes[o + 4] & 0xFFL);
            high |= ((bytes[o + 5] & 0xFFL) << 8);
            high |= ((bytes[o + 6] & 0xFFL) << 16);
            high |= ((bytes[o + 7] & 0xFFL) << 24);
        } else if (order == ByteOrder.BIG) {
            low |= (bytes[o + 7] & 0xFF);
            low |= ((bytes[o + 6] & 0xFFL) << 8);
            low |= ((bytes[o + 5] & 0xFFL) << 16);
            low |= ((bytes[o + 4] & 0xFFL) << 24);

            high |= (bytes[o + 3] & 0xFFL);
            high |= ((bytes[o + 2] & 0xFFL) << 8);
            high |= ((bytes[o + 1] & 0xFFL) << 16);
            high |= ((bytes[o] & 0xFFL) << 24);
        }

        return new BigInteger(String.valueOf(high))
                .multiply(new BigInteger(String.valueOf(UInt32Type.MAX_VALUE + 1)))
                .add(new BigInteger(String.valueOf(low)));
    }

    /**
     * Decode float value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @return Decoded float value.
     */
    public static float readFloat(byte[] bytes, int offset) {
        return readFloat(bytes, offset, ByteOrder.LITTLE);
    }

    /**
     * Decode float value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @return Decoded float value.
     */
    public static float readFloat(byte[] bytes, int offset, ByteOrder order) {
        int o = ReverseUtils.reverse(bytes, offset);
        int value = 0;

        if (order == ByteOrder.LITTLE) {
            value |= (bytes[o] & 0xFF);
            value |= ((bytes[o + 1] & 0xFF) << 8);
            value |= ((bytes[o + 2] & 0xFF) << 16);
            value |= ((bytes[o + 3] & 0xFF) << 24);
        } else if (order == ByteOrder.BIG) {
            value |= (bytes[o + 3] & 0xFF);
            value |= ((bytes[o + 2] & 0xFF) << 8);
            value |= ((bytes[o + 1] & 0xFF) << 16);
            value |= ((bytes[o] & 0xFF) << 24);
        }

        return Float.intBitsToFloat(value);
    }

    /**
     * Decode double value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @return Decoded double value.
     */
    public static double readDouble(byte[] bytes, int offset) {
        return readDouble(bytes, offset, ByteOrder.LITTLE);
    }

    /**
     * Decode double value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @return Decoded double value.
     */
    public static double readDouble(byte[] bytes, int offset, ByteOrder order) {
        int o = ReverseUtils.reverse(bytes, offset);
        long value = 0;

        if (order == ByteOrder.LITTLE) {
            value |= (bytes[o] & 0xFFL);
            value |= ((bytes[o + 1] & 0xFFL) << 8);
            value |= ((bytes[o + 2] & 0xFFL) << 16);
            value |= ((bytes[o + 3] & 0xFFL) << 24);

            value |= ((bytes[o + 4] & 0xFFL) << 32);
            value |= ((bytes[o + 5] & 0xFFL) << 40);
            value |= ((bytes[o + 6] & 0xFFL) << 48);
            value |= ((bytes[o + 7] & 0xFFL) << 56);
        } else if (order == ByteOrder.BIG) {
            value |= (bytes[o + 7] & 0xFFL);
            value |= ((bytes[o + 6] & 0xFFL) << 8);
            value |= ((bytes[o + 5] & 0xFFL) << 16);
            value |= ((bytes[o + 4] & 0xFFL) << 24);

            value |= ((bytes[o + 3] & 0xFFL) << 32);
            value |= ((bytes[o + 2] & 0xFFL) << 40);
            value |= ((bytes[o + 1] & 0xFFL) << 48);
            value |= ((bytes[o] & 0xFFL) << 56);
        }

        return Double.longBitsToDouble(value);
    }

    /**
     * Decode byte array value from binary data.
     *
     * @param bytes The binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param length The length of the field in the binary data.
     * @return Decoded byte array value.
     */
    public static byte[] readBytes(byte[] bytes, int offset, int length) {
        int o = ReverseUtils.reverse(bytes, offset);
        int l = ReverseUtils.reverse(bytes, offset, length);

        if (o + l > bytes.length) {
            throw new IllegalArgumentException("Out of the bytes range.");
        }

        return Arrays.copyOfRange(bytes, o, o + l);
    }
}
