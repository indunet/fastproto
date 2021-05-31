package org.indunet.fastproto.util;

import org.indunet.fastproto.EndianPolicy;

public class NumberUtils {
    public static byte[] floatToBinary(float value) {
        return intToBinary(Float.floatToIntBits(value));
    }

    public static byte[] doubleToBinary(double value) {
        return longToBinary(Double.doubleToLongBits(value));
    }

    public static byte[] intToBinary(int value) {
        return intToBinary(value, EndianPolicy.LITTLE);
    }

    public static byte[] intToBinary(int value, EndianPolicy endian) {
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

    public static byte[] longToBinary(long value) {
        return longToBinary(value, EndianPolicy.LITTLE);
    }

    public static byte[] longToBinary(long value, EndianPolicy endian) {
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