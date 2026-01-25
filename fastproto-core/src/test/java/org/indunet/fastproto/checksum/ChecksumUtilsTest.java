package org.indunet.fastproto.checksum;

import org.indunet.fastproto.annotation.Checksum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChecksumUtilsTest {
    @Test
    public void testCrc8LittleAndBig() {
        byte[] data = {0x31,0x32,0x33,0x34,0x35};
        int crc = new CRC8().calculate(data);
        assertEquals(0xCB, crc);
    }

    @Test
    public void testCrc8Smbus() {
        byte[] data = {0x31,0x32,0x33,0x34,0x35};
        int crc = new CRC8SMBus().calculate(data);
        // SMBus PEC over ASCII '1'..'5' equals CRC8(0x07, init=0x00) result; value depends on dataset
        assertEquals(ChecksumUtils.crc8smbus(data), crc);
    }

    @Test
    public void testCrc16LittleAndBig() {
        byte[] data = {0x31,0x32,0x33,0x34,0x35};
        int crc = new CRC16().calculate(data);
        assertEquals(0xA455, crc);
    }

    @Test
    public void testCrc16Modbus() {
        byte[] data = {0x31,0x32,0x33,0x34,0x35};
        int crc = new CRC16Modbus().calculate(data);
        assertEquals(ChecksumUtils.crc16modbus(data), crc);
    }

    @Test
    public void testCrc32LittleAndBig() {
        byte[] data = {0x31,0x32,0x33,0x34,0x35};
        int crc = new CRC32().calculate(data);
        assertEquals(0xCBF53A1C, crc);
    }

    @Test
    public void testCrc32C() {
        byte[] data = {0x31,0x32,0x33,0x34,0x35};
        int crc = new CRC32C().calculate(data);
        assertEquals(ChecksumUtils.crc32c(data), crc);
    }

    @Test
    public void testLrcAndBcc() {
        byte[] data = {1,2,3,4,5};
        assertEquals(ChecksumUtils.lrc(data) & 0xFF, new LRC().calculate(data) & 0xFF);
        assertEquals(ChecksumUtils.bcc(data) & 0xFF, new BCC().calculate(data) & 0xFF);
    }

    @Test
    public void testBccWithOffset() {
        byte[] data = {0, 0, 1, 2, 3, 4, 5, 0, 0};
        BCC bcc = new BCC();
        int result = bcc.calculate(data, 2, 5);
        assertEquals(1 ^ 2 ^ 3 ^ 4 ^ 5, result);
        
        // Test getters/setters
        assertEquals(0, bcc.getPolynomial());
        assertEquals(0, bcc.getInitialValue());
        bcc.setPolynomial(123); // should be no-op
        bcc.setInitialValue(456); // should be no-op
    }

    @Test
    public void testLrcWithOffset() {
        byte[] data = {0, 0, 1, 2, 3, 4, 5, 0, 0};
        LRC lrc = new LRC();
        int result = lrc.calculate(data, 2, 5);
        int sum = (1 + 2 + 3 + 4 + 5) & 0xFF;
        int expected = ((~sum) + 1) & 0xFF;
        assertEquals(expected, result);
        
        // Test getters/setters
        assertEquals(0, lrc.getPolynomial());
        assertEquals(0, lrc.getInitialValue());
        lrc.setPolynomial(123); // should be no-op
        lrc.setInitialValue(456); // should be no-op
    }

    @Test
    public void testCalculateRange() {
        byte[] data = {1,2,3,4,5};
        long crc8 = ChecksumUtils.calculate(data,0,5, Checksum.Type.CRC8);
        long crc16 = ChecksumUtils.calculate(data,0,5, Checksum.Type.CRC16);
        long crc32 = ChecksumUtils.calculate(data,0,5, Checksum.Type.CRC32);
        assertEquals(new CRC8().calculate(data) & 0xFF, crc8);
        assertEquals(new CRC16().calculate(data) & 0xFFFF, crc16);
        assertEquals(new CRC32().calculate(data) & 0xFFFFFFFFL, crc32);
    }
}
