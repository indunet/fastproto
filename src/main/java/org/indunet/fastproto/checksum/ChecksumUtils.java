package org.indunet.fastproto.checksum;

/** Utility methods for checksum calculation. */
public class ChecksumUtils {
    public static int crc8(byte[] data) {
        return new CRC8().calculate(data);
    }
    public static int crc16(byte[] data) {
        return new CRC16().calculate(data);
    }

    public static int crc32(byte[] data) {
        return new CRC32().calculate(data);
    }

    /** Calculate checksum for specific range. */
    public static long calculate(byte[] data, int offset, int length, org.indunet.fastproto.annotation.Checksum.Type type) {
        byte[] slice = new byte[length];
        System.arraycopy(data, offset, slice, 0, length);
        switch (type) {
            case CRC32:
                return crc32(slice) & 0xFFFFFFFFL;
            case CRC8:
                return crc8(slice) & 0xFF;
            default:
                return crc16(slice) & 0xFFFF;
        }
    }
}
