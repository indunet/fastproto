package org.indunet.fastproto.checksum;

/**
 * CRC-64/ECMA-182
 * poly=0x42F0E1EBA9EA3693, init=0x0000000000000000, refin=false, refout=false, xorout=0x0000000000000000.
 */
public class CRC64ECMA182 {
    public static final long POLYNOMIAL = 0x42F0E1EBA9EA3693L;
    public static final long INITIAL_VALUE = 0x0000000000000000L;

    protected long polynomial = POLYNOMIAL;
    protected long initialValue = INITIAL_VALUE;

    protected static final long[] TABLE = new long[256];

    static {
        for (int i = 0; i < 256; i++) {
            long crc = ((long) i) << 56;
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x8000000000000000L) != 0) {
                    crc = (crc << 1) ^ POLYNOMIAL;
                } else {
                    crc <<= 1;
                }
            }
            TABLE[i] = crc;
        }
    }

    public long calculate(byte[] data) {
        long crc = initialValue;
        for (byte b : data) {
            int idx = (int) (((crc >>> 56) ^ (b & 0xFF)) & 0xFF);
            crc = (crc << 8) ^ TABLE[idx];
        }
        return crc;
    }

    public long calculate(byte[] data, int offset, int length) {
        long crc = initialValue;
        for (int i = 0; i < length; i++) {
            int idx = (int) (((crc >>> 56) ^ (data[offset + i] & 0xFF)) & 0xFF);
            crc = (crc << 8) ^ TABLE[idx];
        }
        return crc;
    }
} 