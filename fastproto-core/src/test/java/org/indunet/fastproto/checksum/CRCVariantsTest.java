package org.indunet.fastproto.checksum;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CRCVariantsTest {
    
    @Test
    public void testCRC16CCITT() {
        byte[] data = {0x31, 0x32, 0x33, 0x34, 0x35};
        CRC16CCITT crc = new CRC16CCITT();
        
        // Test basic calculation
        int result = crc.calculate(data);
        assertTrue(result >= 0 && result <= 0xFFFF);
        
        // Test with offset
        byte[] paddedData = {0, 0, 0x31, 0x32, 0x33, 0x34, 0x35, 0, 0};
        int resultWithOffset = crc.calculate(paddedData, 2, 5);
        assertEquals(result, resultWithOffset);
        
        // Test getters
        assertEquals(CRC16CCITT.POLYNOMIAL, crc.getPolynomial());
        assertEquals(CRC16CCITT.INITIAL_VALUE, crc.getInitialValue());
        
        // Test setters
        crc.setPolynomial(0x1234);
        assertEquals(0x1234, crc.getPolynomial());
        
        crc.setInitialValue(0x5678);
        assertEquals(0x5678, crc.getInitialValue());
        
        // Test with custom polynomial
        int resultCustom = crc.calculate(data);
        assertNotEquals(result, resultCustom);
    }
    
    @Test
    public void testCRC8Maxim() {
        byte[] data = {0x31, 0x32, 0x33, 0x34, 0x35};
        CRC8Maxim crc = new CRC8Maxim();
        
        // Test basic calculation
        int result = crc.calculate(data);
        assertTrue(result >= 0 && result <= 0xFF);
        
        // Test with offset
        byte[] paddedData = {0, 0, 0x31, 0x32, 0x33, 0x34, 0x35, 0, 0};
        int resultWithOffset = crc.calculate(paddedData, 2, 5);
        assertEquals(result, resultWithOffset);
        
        // Test getters
        assertEquals(CRC8Maxim.POLYNOMIAL, crc.getPolynomial());
        assertEquals(CRC8Maxim.INITIAL_VALUE, crc.getInitialValue());
        
        // Test setters
        crc.setPolynomial(0x07);
        assertEquals(0x07, crc.getPolynomial());
        
        crc.setInitialValue(0xFF);
        assertEquals(0xFF, crc.getInitialValue());
        
        // Test with custom initial value
        int resultCustom = crc.calculate(data);
        assertNotEquals(result, resultCustom);
    }
    
    @Test
    public void testCRC32C() {
        byte[] data = {0x31, 0x32, 0x33, 0x34, 0x35};
        CRC32C crc = new CRC32C();
        
        // Test basic calculation
        int result = crc.calculate(data);
        assertNotEquals(0, result);
        
        // Test with offset
        byte[] paddedData = {0, 0, 0x31, 0x32, 0x33, 0x34, 0x35, 0, 0};
        int resultWithOffset = crc.calculate(paddedData, 2, 5);
        assertEquals(result, resultWithOffset);
        
        // Test getters
        assertEquals(CRC32C.DEFAULT_POLYNOMIAL, crc.getPolynomial());
        assertEquals(CRC32C.DEFAULT_INITIAL_VALUE, crc.getInitialValue());
        
        // Test custom constructor
        CRC32C customCrc = new CRC32C(0x12345678, 0xABCDEF00);
        assertEquals(0x12345678, customCrc.getPolynomial());
        assertEquals(0xABCDEF00, customCrc.getInitialValue());
        
        int customResult = customCrc.calculate(data);
        assertNotEquals(result, customResult);
        
        // Test setters
        crc.setPolynomial(0x87654321);
        assertEquals(0x87654321, crc.getPolynomial());
        
        crc.setInitialValue(0x11111111);
        assertEquals(0x11111111, crc.getInitialValue());
        
        // Test with changed polynomial
        int resultChanged = crc.calculate(data);
        assertNotEquals(result, resultChanged);
        
        // Test setting back to default polynomial uses default table
        crc.setPolynomial(CRC32C.DEFAULT_POLYNOMIAL);
        assertEquals(CRC32C.DEFAULT_POLYNOMIAL, crc.getPolynomial());
    }
    
    @Test
    public void testCRC64ECMA182() {
        byte[] data = {0x31, 0x32, 0x33, 0x34, 0x35};
        CRC64ECMA182 crc = new CRC64ECMA182();
        
        // Test basic calculation
        long result = crc.calculate(data);
        assertNotEquals(0L, result);
        
        // Test with offset
        byte[] paddedData = {0, 0, 0x31, 0x32, 0x33, 0x34, 0x35, 0, 0};
        long resultWithOffset = crc.calculate(paddedData, 2, 5);
        assertEquals(result, resultWithOffset);
    }
    
    @Test
    public void testCRC64ISO() {
        byte[] data = {0x31, 0x32, 0x33, 0x34, 0x35};
        CRC64ISO crc = new CRC64ISO();
        
        // Test basic calculation
        long result = crc.calculate(data);
        assertNotEquals(0L, result);
        
        // Test with offset
        byte[] paddedData = {0, 0, 0x31, 0x32, 0x33, 0x34, 0x35, 0, 0};
        long resultWithOffset = crc.calculate(paddedData, 2, 5);
        assertEquals(result, resultWithOffset);
    }
}

