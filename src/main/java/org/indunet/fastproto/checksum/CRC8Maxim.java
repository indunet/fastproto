package org.indunet.fastproto.checksum;

/**
 * CRC-8/DALLAS-MAXIM
 * poly=0x31, init=0x00, refin=true, refout=true, xorout=0x00.
 * Implemented in LSB-first form using reversed polynomial 0x8C.
 */
public class CRC8Maxim extends CRC {
    public static final int POLYNOMIAL = 0x31;      // nominal
    public static final int POLY_REVERSED = 0x8C;   // for LSB-first
    public static final int INITIAL_VALUE = 0x00;

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
        int crc = initialValue & 0xFF;
        for (byte b0 : data) {
            int b = b0 & 0xFF;
            for (int i = 0; i < 8; i++) {
                int mix = (crc ^ b) & 0x01;
                crc >>>= 1;
                if (mix != 0) {
                    crc ^= POLY_REVERSED;
                }
                b >>>= 1;
            }
        }
        return crc & 0xFF; // refout=true, xorout=0
    }

    @Override
    public int calculate(byte[] data, int offset, int length) {
        int crc = initialValue & 0xFF;
        for (int idx = 0; idx < length; idx++) {
            int b = data[offset + idx] & 0xFF;
            for (int i = 0; i < 8; i++) {
                int mix = (crc ^ b) & 0x01;
                crc >>>= 1;
                if (mix != 0) {
                    crc ^= POLY_REVERSED;
                }
                b >>>= 1;
            }
        }
        return crc & 0xFF; // refout=true, xorout=0
    }
} 