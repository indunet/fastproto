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
 * The CRC32 class implements the CRC interface and defines the CRC32 checksum calculation.
 * It provides the polynomial and initial value for CRC32.
 * The calculate method is used to calculate the CRC32 value of the given data.
 * Note that the polynomial and initial value for CRC32 are fixed and cannot be set.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
public class CRC32 extends CRC {
    public static final int DEFAULT_POLYNOMIAL = 0x04C11DB7;
    public static final int DEFAULT_INITIAL_VALUE = 0xFFFFFFFF;
    protected static final int[] CRC32_TABLE = new int[256];

    protected int polynomial;
    protected int initialValue;

    static {
        // Initialize the CRC32 lookup table
        for (int i = 0; i < 256; i++) {
            int crc = i << 24;

            for (int j = 0; j < 8; j++) {
                if ((crc & 0x80000000) != 0) {
                    crc = (crc << 1) ^ DEFAULT_POLYNOMIAL;
                } else {
                    crc <<= 1;
                }
            }

            CRC32_TABLE[i] = crc;
        }
    }

    public CRC32() {
        this.initialValue = DEFAULT_INITIAL_VALUE;
        this.polynomial = DEFAULT_POLYNOMIAL;
    }

    @Override
    public int getPolynomial() {
        return this.polynomial;
    }

    @Override
    public void setPolynomial(int polynomial) {
        throw new UnsupportedOperationException("CRC32 polynomial is fixed and cannot be set.");
    }

    @Override
    public int getInitialValue() {
        return this.initialValue;
    }

    @Override
    public void setInitialValue(int initialValue) {
        throw new UnsupportedOperationException("CRC32 initial value is fixed and cannot be set.");
    }

    @Override
    public int calculate(byte[] data) {
        int crc = initialValue;

        for (byte b : data) {
            b = reverseBits(b); // 输入数据翻转
            int tableIndex = (crc >>> 24) ^ (b & 0xFF);
            crc = (crc << 8) ^ CRC32_TABLE[tableIndex];
        }

        return reverseBits(crc, 32) ^ 0xFFFFFFFF;
    }
}

