package org.indunet.fastproto.checksum;

/**
 * CRC-16/CCITT
 * poly=0x1021, init=0xFFFF, refin=false, refout=false, xorout=0x0000.
 */
public class CRC16CCITT extends CRC {
    public static final int POLYNOMIAL = 0x1021;
    public static final int INITIAL_VALUE = 0xFFFF;

    protected int polynomial = POLYNOMIAL;
    protected int initialValue = INITIAL_VALUE;

    @Override
    public int getPolynomial() { return polynomial; }

    @Override
    public void setPolynomial(int polynomial) { this.polynomial = polynomial; }

    @Override
    public int getInitialValue() { return initialValue; }

    @Override
    public void setInitialValue(int initialValue) { this.initialValue = initialValue; }

    @Override
    public int calculate(byte[] data) {
        int crc = initialValue & 0xFFFF;
        for (byte b : data) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = ((crc << 1) ^ polynomial) & 0xFFFF;
                } else {
                    crc = (crc << 1) & 0xFFFF;
                }
            }
        }
        return crc & 0xFFFF;
    }
} 