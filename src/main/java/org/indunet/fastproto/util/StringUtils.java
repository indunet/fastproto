package org.indunet.fastproto.util;

import java.util.Locale;

public class StringUtils {
    public static String byteToHexString(byte[] bytes) {
        return byteToHexString(bytes, 0, bytes.length);
    }

    public static String byteToHexString(byte[] bytes, int start, int end) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes == null");
        } else {
            StringBuilder s = new StringBuilder();

            // Convert from hex to string one by one, there is no better method.
            for(int i = start; i < end; ++i) {
                s.append(format("%02x", bytes[i]));
            }

            return s.toString();
        }
    }

    public static String format(String format, Object... objects) {
        // It seems like printf method which input format string and object.
        return String.format(Locale.ENGLISH, format, objects);
    }

    public static byte[] hexStringToByte(String hex) {
        // This method would not work properly if there is string with odd length.
        byte[] bts = new byte[hex.length() / 2];

        // Convert from string to hex one by one, there is no better method.
        for(int i = 0; i < bts.length; ++i) {
            bts[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }

        return bts;
    }
}
