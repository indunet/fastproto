package org.indunet.fastproto.checksum;

/**
 * CRC-8/SMBus (PEC)
 * poly=0x07, init=0x00, refin=false, refout=false, xorout=0x00.
 */
public class CRC8SMBus extends CRC8 {
    public CRC8SMBus() {
        this.initialValue = CRC8_INITIAL_VALUE; // 0x00 for SMBus
        this.polynomial = CRC8_POLYNOMIAL;     // 0x07
    }
} 