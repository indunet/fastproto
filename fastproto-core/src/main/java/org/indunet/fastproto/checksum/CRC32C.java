package org.indunet.fastproto.checksum;

/**
 * CRC-32C (Castagnoli) software implementation.
 * poly=0x1EDC6F41, init=0xFFFFFFFF, refin=true, refout=true, xorout=0xFFFFFFFF.
 */
public class CRC32C extends CRC {
    public static final int DEFAULT_POLYNOMIAL = 0x1EDC6F41;
    public static final int DEFAULT_INITIAL_VALUE = 0xFFFFFFFF;
    protected static final int[] DEFAULT_TABLE = new int[256];

    protected int polynomial;
    protected int initialValue;
    protected int[] crcTable;

    static {
        // Initialize the CRC32C lookup table for default polynomial
        for (int i = 0; i < 256; i++) {
            int crc = i << 24;

            for (int j = 0; j < 8; j++) {
                if ((crc & 0x80000000) != 0) {
                    crc = (crc << 1) ^ DEFAULT_POLYNOMIAL;
                } else {
                    crc <<= 1;
                }
            }

            DEFAULT_TABLE[i] = crc;
        }
    }

    protected static int[] buildTable(int polynomial) {
        int[] table = new int[256];

        for (int i = 0; i < 256; i++) {
            int crc = i << 24;

            for (int j = 0; j < 8; j++) {
                if ((crc & 0x80000000) != 0) {
                    crc = (crc << 1) ^ polynomial;
                } else {
                    crc <<= 1;
                }
            }

            table[i] = crc;
        }

        return table;
    }

    public CRC32C() {
        this.initialValue = DEFAULT_INITIAL_VALUE;
        this.polynomial = DEFAULT_POLYNOMIAL;
        this.crcTable = DEFAULT_TABLE;
    }

    public CRC32C(int polynomial, int initialValue) {
        this.polynomial = polynomial;
        this.initialValue = initialValue;
        if (polynomial == DEFAULT_POLYNOMIAL) {
            this.crcTable = DEFAULT_TABLE;
        } else {
            this.crcTable = buildTable(polynomial);
        }
    }

    @Override
    public int getPolynomial() {
        return this.polynomial;
    }

    @Override
    public void setPolynomial(int polynomial) {
        this.polynomial = polynomial;
        if (polynomial == DEFAULT_POLYNOMIAL) {
            this.crcTable = DEFAULT_TABLE;
        } else {
            this.crcTable = buildTable(polynomial);
        }
    }

    @Override
    public int getInitialValue() {
        return this.initialValue;
    }

    @Override
    public void setInitialValue(int initialValue) {
        this.initialValue = initialValue;
    }

    @Override
    public int calculate(byte[] data) {
        int crc = initialValue;

        for (byte b : data) {
            b = reverseBits(b); // reflect input
            int tableIndex = (crc >>> 24) ^ (b & 0xFF);
            crc = (crc << 8) ^ crcTable[tableIndex];
        }

        return reverseBits(crc, 32) ^ 0xFFFFFFFF; // reflect output and final xor
    }

    @Override
    public int calculate(byte[] data, int offset, int length) {
        int crc = initialValue;

        for (int i = 0; i < length; i++) {
            byte b = reverseBits(data[offset + i]); // reflect input
            int tableIndex = (crc >>> 24) ^ (b & 0xFF);
            crc = (crc << 8) ^ crcTable[tableIndex];
        }

        return reverseBits(crc, 32) ^ 0xFFFFFFFF; // reflect output and final xor
    }
} 