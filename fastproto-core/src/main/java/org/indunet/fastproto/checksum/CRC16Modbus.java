package org.indunet.fastproto.checksum;

/**
 * CRC-16/MODBUS
 * poly=0x8005, init=0xFFFF, refin=true, refout=true, xorout=0x0000.
 */
public class CRC16Modbus extends CRC16 {
    public CRC16Modbus() {
        this.initialValue = CRC16_MODBUS_INITIAL_VALUE;
        this.polynomial = CRC16_MODBUS_POLYNOMIAL;
    }
} 