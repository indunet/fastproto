package org.indunet.fastproto.checksum;

/**
 * CRC-64/ISO
 * poly=0x000000000000001B, init=0xFFFFFFFFFFFFFFFF, refin=true, refout=true, xorout=0xFFFFFFFFFFFFFFFF.
 */
public class CRC64ISO {
    public static final long POLYNOMIAL = 0x000000000000001BL;
    public static final long INITIAL_VALUE = 0xFFFFFFFFFFFFFFFFL;

    protected long polynomial = POLYNOMIAL;
    protected long initialValue = INITIAL_VALUE;

    protected static final long[] TABLE = new long[256];

    static {
        for (int i = 0; i < 256; i++) {
            long crc = i;
            for (int j = 0; j < 8; j++) {
                if ((crc & 1L) != 0) {
                    crc = (crc >>> 1) ^ POLYNOMIAL;
                } else {
                    crc >>>= 1;
                }
            }
            TABLE[i] = crc;
        }
    }

    private static long reflect8(int v) { return Long.reverse(v) >>> (64 - 8); }
    private static long reflect64(long v) { return Long.reverse(v); }

    public long calculate(byte[] data) {
        long crc = initialValue;
        for (byte b : data) {
            int idx = (int) ((crc ^ reflect8(b & 0xFF)) & 0xFF);
            crc = (crc >>> 8) ^ TABLE[idx];
        }
        crc = reflect64(crc) ^ 0xFFFFFFFFFFFFFFFFL;
        return crc;
    }

    public long calculate(byte[] data, int offset, int length) {
        long crc = initialValue;
        for (int i = 0; i < length; i++) {
            int idx = (int) ((crc ^ reflect8(data[offset + i] & 0xFF)) & 0xFF);
            crc = (crc >>> 8) ^ TABLE[idx];
        }
        crc = reflect64(crc) ^ 0xFFFFFFFFFFFFFFFFL;
        return crc;
    }
} 