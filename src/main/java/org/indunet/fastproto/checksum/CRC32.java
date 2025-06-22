/*
 * Copyright 2019-2024 indunet.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.indunet.fastproto.checksum;


/**
 * The CRC32 class implements the CRC interface and calculates CRC32 checksums.
 * It uses {@code DEFAULT_POLYNOMIAL} and {@code DEFAULT_INITIAL_VALUE} as defaults.
 * These constants may be changed in the constructor or dynamically via
 * {@link #setPolynomial(int)} and {@link #setInitialValue(int)}.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
public class CRC32 extends CRC {
    public static final int DEFAULT_POLYNOMIAL = 0x04C11DB7;
    public static final int DEFAULT_INITIAL_VALUE = 0xFFFFFFFF;
    protected static final int[] DEFAULT_TABLE = new int[256];

    protected int polynomial;
    protected int initialValue;
    protected int[] crcTable;

    static {
        // Initialize the CRC32 lookup table for default polynomial
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

    public CRC32() {
        this.initialValue = DEFAULT_INITIAL_VALUE;
        this.polynomial = DEFAULT_POLYNOMIAL;
        this.crcTable = DEFAULT_TABLE;
    }

    public CRC32(int polynomial, int initialValue) {
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
            b = reverseBits(b); // 输入数据翻转
            int tableIndex = (crc >>> 24) ^ (b & 0xFF);
            crc = (crc << 8) ^ crcTable[tableIndex];
        }

        return reverseBits(crc, 32) ^ 0xFFFFFFFF;
    }
}

