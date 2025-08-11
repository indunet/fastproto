package org.indunet.fastproto.checksum;

/**
 * BCC/XOR checksum (8-bit): XOR of all bytes.
 */
public class BCC extends CRC {
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
        int x = 0;
        for (byte b : data) {
            x ^= (b & 0xFF);
        }
        return x & 0xFF;
    }
} 