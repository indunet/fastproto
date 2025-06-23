package org.indunet.fastproto.checksum;

import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.Checksum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChecksumAnnotationTest {
    @Test
    public void testObjectCrc8() {
        ChecksumAnnotationObject obj = new ChecksumAnnotationObject(
                0x31, 0x32, 0x33, 0x34, 0x35, 0xCB);
        byte[] bytes = FastProto.encode(obj, 6);
        long crc = ChecksumUtils.calculate(bytes, 0, 5, Checksum.Type.CRC8);
        assertEquals(bytes[5] & 0xFF, crc);
    }

    @Test
    public void testObjectCrc16() {
        Checksum16AnnotationObject obj = new Checksum16AnnotationObject(
                0x31, 0x32, 0x33, 0x34, 0x35, 0);
        byte[] bytes = FastProto.encode(obj, 7);
        long crc = ChecksumUtils.calculate(bytes, 0, 5, Checksum.Type.CRC16);
        int low = bytes[5] & 0xFF;
        int high = bytes[6] & 0xFF;
        int actual = low | (high << 8);
        assertEquals((int) crc, actual);
    }

    @Test
    public void testObjectCrc32() {
        Checksum32AnnotationObject obj = new Checksum32AnnotationObject(
                0x31, 0x32, 0x33, 0x34, 0x35, 0L);
        byte[] bytes = FastProto.encode(obj, 9);
        long crc = ChecksumUtils.calculate(bytes, 0, 5, Checksum.Type.CRC32);
        long actual = ((bytes[5] & 0xFFL) << 24) |
                ((bytes[6] & 0xFFL) << 16) |
                ((bytes[7] & 0xFFL) << 8) |
                (bytes[8] & 0xFFL);
        assertEquals(crc, actual);
    }
}
