package org.indunet.fastproto.checksum;

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.Checksum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChecksumUtilsTest {
    @Test
    public void testCrc16LittleAndBig() {
        byte[] data = {0x31,0x32,0x33,0x34,0x35};
        int crc = CRC16.CRC16_IBM_INITIAL_VALUE;
        crc = new CRC16().calculate(data);
        byte[] little = {(byte) crc, (byte) (crc >>> 8)};
        byte[] big = {(byte) (crc >>> 8), (byte) crc};
        assertEquals(0xA455, crc);
        assertArrayEquals(new byte[]{(byte)0x55,(byte)0xA4}, little);
        assertArrayEquals(new byte[]{(byte)0xA4,(byte)0x55}, big);
    }

    @Test
    public void testCrc32LittleAndBig() {
        byte[] data = {0x31,0x32,0x33,0x34,0x35};
        int crc = new CRC32().calculate(data);
        byte[] little = {(byte)crc,(byte)(crc>>>8),(byte)(crc>>>16),(byte)(crc>>>24)};
        byte[] big = {(byte)(crc>>>24),(byte)(crc>>>16),(byte)(crc>>>8),(byte)crc};
        assertEquals(0xCBF53A1C, crc);
        assertArrayEquals(new byte[]{(byte)0x1C,(byte)0x3A,(byte)0xF5,(byte)0xCB}, little);
        assertArrayEquals(new byte[]{(byte)0xCB,(byte)0xF5,(byte)0x3A,(byte)0x1C}, big);
    }

    @Test
    public void testCalculateRange() {
        byte[] data = {1,2,3,4,5};
        long crc16 = ChecksumUtils.calculate(data,0,5, Checksum.Type.CRC16);
        long crc32 = ChecksumUtils.calculate(data,0,5, Checksum.Type.CRC32);
        assertEquals(new CRC16().calculate(data) & 0xFFFF, crc16);
        assertEquals(new CRC32().calculate(data) & 0xFFFFFFFFL, crc32);
    }
}
