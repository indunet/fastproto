package org.indunet.fastproto.checksum;

/**
 * LRC (Longitudinal Redundancy Check), 8-bit two's complement of sum.
 */
public class LRC extends CRC {
    @Override
    public int getPolynomial() { return 0; }

    @Override
    public void setPolynomial(int polynomial) { }

    @Override
    public int getInitialValue() { return 0; }

    @Override
    public void setInitialValue(int initialValue) { }

    @Override
    public int calculate(byte[] data) {
        int sum = 0;
        for (byte b : data) {
            sum = (sum + (b & 0xFF)) & 0xFF;
        }
        int lrc = ((~sum) + 1) & 0xFF; // two's complement
        return lrc;
    }
} 