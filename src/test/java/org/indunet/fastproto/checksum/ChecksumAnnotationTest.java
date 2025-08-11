package org.indunet.fastproto.checksum;

import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.Checksum;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChecksumAnnotationTest {
    @Test
    public void testObjectCrcGroup() {
        ChecksumAnnotationObject obj = new ChecksumAnnotationObject(
                0x31, 0x32, 0x33, 0x34, 0x35,
                0, 0, 0, 0L, BigInteger.ZERO, BigInteger.ZERO);

        byte[] bytes = FastProto.encode(obj, 30);

        // CRC8 at offset 5
        long crc8 = ChecksumUtils.calculate(bytes, 0, 5, Checksum.Type.CRC8);
        assertEquals(bytes[5] & 0xFF, crc8);

        // CRC8-MAXIM at offset 6
        long crc8Maxim = ChecksumUtils.calculate(bytes, 0, 5, Checksum.Type.CRC8_MAXIM);
        assertEquals(bytes[6] & 0xFF, crc8Maxim);

        // CRC16-CCITT at offset 8..9 (little-endian)
        long ccitt = ChecksumUtils.calculate(bytes, 0, 5, Checksum.Type.CRC16_CCITT);
        int low = bytes[8] & 0xFF;
        int high = bytes[9] & 0xFF;
        int actual16 = low | (high << 8);
        assertEquals((int) ccitt, actual16);

        // CRC32C at offset 10..13 (big-endian)
        long c32c = ChecksumUtils.calculate(bytes, 0, 5, Checksum.Type.CRC32C);
        long actual32 = ((bytes[10] & 0xFFL) << 24) |
                ((bytes[11] & 0xFFL) << 16) |
                ((bytes[12] & 0xFFL) << 8) |
                (bytes[13] & 0xFFL);
        assertEquals(c32c, actual32);

        // CRC64-ECMA at 14..21 (big-endian)
        long ecma = ChecksumUtils.calculate(bytes, 0, 5, Checksum.Type.CRC64_ECMA182);
        long actual64ecma = ((bytes[14] & 0xFFL) << 56) |
                ((bytes[15] & 0xFFL) << 48) |
                ((bytes[16] & 0xFFL) << 40) |
                ((bytes[17] & 0xFFL) << 32) |
                ((bytes[18] & 0xFFL) << 24) |
                ((bytes[19] & 0xFFL) << 16) |
                ((bytes[20] & 0xFFL) << 8) |
                ((bytes[21] & 0xFFL));
        assertEquals(ecma, actual64ecma);

        // CRC64-ISO at 22..29 (little-endian)
        long iso = ChecksumUtils.calculate(bytes, 0, 5, Checksum.Type.CRC64_ISO);
        long actual64iso = ((bytes[22] & 0xFFL)) |
                ((bytes[23] & 0xFFL) << 8) |
                ((bytes[24] & 0xFFL) << 16) |
                ((bytes[25] & 0xFFL) << 24) |
                ((bytes[26] & 0xFFL) << 32) |
                ((bytes[27] & 0xFFL) << 40) |
                ((bytes[28] & 0xFFL) << 48) |
                ((bytes[29] & 0xFFL) << 56);
        assertEquals(iso, actual64iso);
    }
}
