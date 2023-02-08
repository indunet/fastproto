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
import org.indunet.fastproto.exception.EncodingException;

import java.math.BigInteger;

/**
 * Encode utils which supplies some methods for encoding.
 *
 * @author Deng Ran
 * @since 3.10.1
 */
public class EncodeUtils {
    /**
     * Encode boolean values into binary data.
     *
     * @param bytes The target binary data.
     * @param byteOffset The byte offset of the byte values in binary data.
     * @param bitOffset The bit offset of the byte values in binary data.
     * @param value The boolean value to be encoded.
     */
    public static void writeBool(byte[] bytes, int byteOffset, int bitOffset, boolean value) {
        writeBool(bytes, byteOffset, bitOffset, BitOrder.LSB_0, value);
    }

    /**
     * Encode boolean values into binary data.
     *
     * @param bytes The target binary data.
     * @param byteOffset The byte offset of the field in the binary data.
     * @param bitOffset The bit offset of the field in the binary data.
     * @param order The bit order of the field in the binary data.
     * @param value The boolean value to be encoded.
     */
    public static void writeBool(byte[] bytes, int byteOffset, int bitOffset, BitOrder order, boolean value) {
        if (bitOffset < BoolType.BIT_0 || bitOffset > BoolType.BIT_7) {
            throw new EncodingException("Out of byte range.");
        }

        int o = ReverseUtils.reverse(bytes, byteOffset);

        if (order == BitOrder.MSB_0) {
            bitOffset = 7 - bitOffset;
        }

        if (value) {
            bytes[o] |= (0x01 << bitOffset);
        } else {
            bytes[o] &= ~(0x01 << bitOffset);
        }
    }

    /**
     * Encode byte values into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param value The byte value to be encoded.
     */
    public static void writeByte(byte[] bytes, int offset, byte value) {
        int o = ReverseUtils.reverse(bytes, offset);

        bytes[o] = value;
    }

    /**
     * Encode short value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param value The short value to be encoded.
     */
    public static void writeShort(byte[] bytes, int offset, short value) {
        writeShort(bytes, offset, ByteOrder.LITTLE, value);
    }

    /**
     * Encode short value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @param value The short value to be encoded.
     */
    public static void writeShort(byte[] bytes, int offset, ByteOrder order, short value) {
        int o = ReverseUtils.reverse(bytes, offset);

        if (order == ByteOrder.LITTLE) {
            bytes[o] = (byte) (value);
            bytes[o + 1] = (byte) (value >>> 8);
        } else if (order == ByteOrder.BIG) {
            bytes[o + 1] = (byte) (value);
            bytes[o] = (byte) (value >>> 8);
        }
    }

    /**
     * Encode int8 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param value The int8 value to be encoded.
     */
    public static void writeInt8(byte[] bytes, int offset, int value) {
        if (value < Int8Type.MIN_VALUE || value > Int8Type.MAX_VALUE) {
            throw new EncodingException("Out of int8 range.");
        }

        int o = ReverseUtils.reverse(bytes, offset);

        bytes[o] = (byte) value;
    }

    /**
     * Encode int16 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param value The int16 value to be encoded.
     */
    public static void writeInt16(byte[] bytes, int offset, int value) {
        writeInt16(bytes, offset, ByteOrder.LITTLE, value);
    }

    /**
     * Encode int16 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @param value The int16 value to be encoded.
     */
    public static void writeInt16(byte[] bytes, int offset, ByteOrder order, int value) {
        if (value < Int16Type.MIN_VALUE || value > Int16Type.MAX_VALUE) {
            throw new EncodingException("Out of int16 range.");
        }

        int o = ReverseUtils.reverse(bytes, offset);

        if (order == ByteOrder.BIG) {
            bytes[o + 1] = (byte) (value);
            bytes[o] = (byte) (value >>> 8);
        } else {
            bytes[o] = (byte) (value);
            bytes[o + 1] = (byte) (value >>> 8);
        }
    }

    /**
     * Encode int32 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param value The int32 value to be encoded.
     */
    public static void writeInt32(byte[] bytes, int offset, int value) {
        writeInt32(bytes, offset, ByteOrder.LITTLE, value);
    }

    /**
     * Encode int32 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @param value The int32 value to be encoded.
     */
    public static void writeInt32(byte[] bytes, int offset, ByteOrder order, int value) {
        int o = ReverseUtils.reverse(bytes, offset);

        if (order == ByteOrder.LITTLE) {
            bytes[o] = (byte) (value);
            bytes[o + 1] = (byte) (value >>> 8);
            bytes[o + 2] = (byte) (value >>> 16);
            bytes[o + 3] = (byte) (value >>> 24);
        } else if (order == ByteOrder.BIG) {
            bytes[o + 3] = (byte) (value);
            bytes[o + 2] = (byte) (value >>> 8);
            bytes[o + 1] = (byte) (value >>> 16);
            bytes[o] = (byte) (value >>> 24);
        }
    }

    /**
     * Encode int64 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param value The int64 value to be encoded.
     */
    public static void writeInt64(byte[] bytes, int offset, long value) {
        writeInt64(bytes, offset, ByteOrder.LITTLE, value);
    }

    /**
     * Encode int64 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @param value The int64 value to be encoded.
     */
    public static void writeInt64(byte[] bytes, int offset, ByteOrder order, long value) {
        int o = ReverseUtils.reverse(bytes, offset);

        if (order == ByteOrder.BIG) {
            bytes[o + 7] = (byte) (value);
            bytes[o + 6] = (byte) (value >>> 8);
            bytes[o + 5] = (byte) (value >>> 16);
            bytes[o + 4] = (byte) (value >>> 24);

            bytes[o + 3] = (byte) (value >>> 32);
            bytes[o + 2] = (byte) (value >>> 40);
            bytes[o + 1] = (byte) (value >>> 48);
            bytes[o] = (byte) (value >>> 56);
        } else {
            bytes[o] = (byte) (value);
            bytes[o + 1] = (byte) (value >>> 8);
            bytes[o + 2] = (byte) (value >>> 16);
            bytes[o + 3] = (byte) (value >>> 24);

            bytes[o + 4] = (byte) (value >>> 32);
            bytes[o + 5] = (byte) (value >>> 40);
            bytes[o + 6] = (byte) (value >>> 48);
            bytes[o + 7] = (byte) (value >>> 56);
        }
    }

    /**
     * Encode uint8 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param value The uint8 value to be encoded.
     */
    public static void writeUInt8(byte[] bytes, int offset, int value) {
        if (value < UInt8Type.MIN_VALUE || value > UInt8Type.MAX_VALUE) {
            throw new EncodingException("Out of uint8 range.");
        }

        int o = ReverseUtils.reverse(bytes, offset);

        bytes[o] = (byte) value;
    }

    /**
     * Encode uint16 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param value The uint16 value to be encoded.
     */
    public static void writeUInt16(byte[] bytes, int offset, int value) {
        writeUInt16(bytes, offset, ByteOrder.LITTLE, value);
    }

    /**
     * Encode uint16 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @param value The uint16 value to be encoded.
     */
    public static void writeUInt16(byte[] bytes, int offset, ByteOrder order, int value) {
        if (value < UInt16Type.MIN_VALUE || value > UInt16Type.MAX_VALUE) {
            throw new EncodingException("Out of uint16 range.");
        }

        int o = ReverseUtils.reverse(bytes, offset);

        if (order == ByteOrder.BIG) {
            bytes[o + 1] = (byte) (value);
            bytes[o] = (byte) (value >>> 8);
        } else {
            bytes[o] = (byte) (value);
            bytes[o + 1] = (byte) (value >>> 8);
        }
    }

    /**
     * Encode uint32 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param value The uint32 value to be encoded.
     */
    public static void writeUInt32(byte[] bytes, int offset, long value) {
        writeUInt32(bytes, offset, ByteOrder.LITTLE, (int) value);
    }

    /**
     * Encode uint32 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @param value The uint32 value to be encoded.
     */
    public static void writeUInt32(byte[] bytes, int offset, ByteOrder order, long value) {
        if (value < UInt32Type.MIN_VALUE || value > UInt32Type.MAX_VALUE) {
            throw new EncodingException("Out of uint32 range.");
        }

        int o = ReverseUtils.reverse(bytes, offset);

        if (order == ByteOrder.BIG) {
            bytes[o + 3] = (byte) (value);
            bytes[o + 2] = (byte) (value >>> 8);
            bytes[o + 1] = (byte) (value >>> 16);
            bytes[o] = (byte) (value >>> 24);
        } else {
            bytes[o] = (byte) (value);
            bytes[o + 1] = (byte) (value >>> 8);
            bytes[o + 2] = (byte) (value >>> 16);
            bytes[o + 3] = (byte) (value >>> 24);
        }
    }

    /**
     * Encode uint64 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param value The uint64 value to be encoded.
     */
    public static void writeUInt64(byte[] bytes, int offset, BigInteger value) {
        writeUInt64(bytes, offset, ByteOrder.LITTLE, value);
    }

    /**
     * Encode uint64 value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @param value The uint64 value to be encoded.
     */
    public static void writeUInt64(byte[] bytes, int offset, ByteOrder order, BigInteger value) {
        if (value.compareTo(UInt64Type.MAX_VALUE) > 0 || value.compareTo(UInt64Type.MIN_VALUE) < 0) {
            throw new EncodingException("Out of uinteger64 range.");
        }

        int o = ReverseUtils.reverse(bytes, offset);
        long low = value
                .and(new BigInteger(String.valueOf(0xFFFF_FFFFL)))
                .longValueExact();
        long high = value
                .shiftRight(32)
                .longValueExact();

        if (order == ByteOrder.BIG) {
            bytes[o + 7] = (byte) (low);
            bytes[o + 6] = (byte) (low >>> 8);
            bytes[o + 5] = (byte) (low >>> 16);
            bytes[o + 4] = (byte) (low >>> 24);

            bytes[o + 3] = (byte) (high);
            bytes[o + 2] = (byte) (high >>> 8);
            bytes[o + 1] = (byte) (high >>> 16);
            bytes[o] = (byte) (high >>> 24);
        } else {
            bytes[o] = (byte) (low);
            bytes[o + 1] = (byte) (low >>> 8);
            bytes[o + 2] = (byte) (low >>> 16);
            bytes[o + 3] = (byte) (low >>> 24);

            bytes[o + 4] = (byte) (high);
            bytes[o + 5] = (byte) (high >>> 8);
            bytes[o + 6] = (byte) (high >>> 16);
            bytes[o + 7] = (byte) (high >>> 24);
        }
    }

    /**
     * Encode float value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param value The float value to be encoded.
     */
    public static void writeFloat(byte[] bytes, int offset, float value) {
        writeFloat(bytes, offset, ByteOrder.LITTLE, value);
    }

    /**
     * Encode float value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @param value The float value to be encoded.
     */
    public static void writeFloat(byte[] bytes, int offset, ByteOrder order, float value) {
        int o = ReverseUtils.reverse(bytes, offset);
        int bits = Float.floatToIntBits(value);

        if (order == ByteOrder.LITTLE) {
            bytes[o] = (byte) bits;
            bytes[o + 1] = (byte) (bits >>> 8);
            bytes[o + 2] = (byte) (bits >>> 16);
            bytes[o + 3] = (byte) (bits >>> 24);
        } else if (order == ByteOrder.BIG) {
            bytes[o + 3] = (byte) (bits);
            bytes[o + 2] = (byte) (bits >>> 8);
            bytes[o + 1] = (byte) (bits >>> 16);
            bytes[o] = (byte) (bits >>> 24);
        }
    }

    /**
     * Encode double value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param value The double value to be encoded.
     */
    public static void writeDouble(byte[] bytes, int offset, double value) {
        writeDouble(bytes, offset, ByteOrder.LITTLE, value);
    }

    /**
     * Encode double value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param order The byte order of the field in the binary data.
     * @param value The double value to be encoded.
     */
    public static void writeDouble(byte[] bytes, int offset, ByteOrder order, double value) {
        int o = ReverseUtils.reverse(bytes, offset);
        long bits = Double.doubleToRawLongBits(value);

        if (order == ByteOrder.BIG) {
            bytes[o + 7] = (byte) (bits);
            bytes[o + 6] = (byte) (bits >>> 8);
            bytes[o + 5] = (byte) (bits >>> 16);
            bytes[o + 4] = (byte) (bits >>> 24);

            bytes[o + 3] = (byte) (bits >>> 32);
            bytes[o + 2] = (byte) (bits >>> 40);
            bytes[o + 1] = (byte) (bits >>> 48);
            bytes[o] = (byte) (bits >>> 56);
        } else {
            bytes[o] = (byte) (bits);
            bytes[o + 1] = (byte) (bits >>> 8);
            bytes[o + 2] = (byte) (bits >>> 16);
            bytes[o + 3] = (byte) (bits >>> 24);

            bytes[o + 4] = (byte) (bits >>> 32);
            bytes[o + 5] = (byte) (bits >>> 40);
            bytes[o + 6] = (byte) (bits >>> 48);
            bytes[o + 7] = (byte) (bits >>> 56);
        }
    }

    /**
     * Encode byte array value into binary data.
     *
     * @param bytes The target binary data.
     * @param offset The byte offset of the field in the binary data.
     * @param values The byte array value to be encoded.
     */
    public static void writeBytes(byte[] bytes, int offset, byte[] values) {
        int o = ReverseUtils.reverse(bytes, offset);

        if (o + values.length > bytes.length) {
            throw new EncodingException("Out of the bytes range.");
        } else {
            System.arraycopy(values, 0, bytes, o, values.length);
        }
    }
}
