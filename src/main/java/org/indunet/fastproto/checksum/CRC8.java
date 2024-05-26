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
 * The CRC8 class implements the CRC interface and defines the CRC8 checksum calculation.
 * It provides the polynomial and initial value for CRC8, CRC8-CCITT, and CRC8-DALLAS-MAXIM.
 * The calculate method is used to calculate the CRC8 value of the given data.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
public class CRC8 implements CRC {
    public static final int CRC8_POLYNOMIAL = 0x07;
    public static final int CRC8_INITIAL_VALUE = 0x00;

    public static final int CRC8_CCITT_POLYNOMIAL = 0x07;
    public static final int CRC8_CCITT_INITIAL_VALUE = 0xFF;

    public static final int CRC8_DALLAS_MAXIM_POLYNOMIAL = 0x31;
    public static final int CRC8_DALLAS_MAXIM_INITIAL_VALUE = 0x00;

    protected int polynomial;
    protected int initialValue;

    public CRC8() {
        this.polynomial = CRC8_POLYNOMIAL;
        this.initialValue = CRC8_INITIAL_VALUE;
    }

    @Override
    public int getPolynomial() {
        return this.polynomial;
    }

    @Override
    public void setPolynomial(int polynomial) {
        this.polynomial = polynomial;
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
            crc ^= b & 0xFF;

            for (int i = 0; i < 8; i++) {
                if ((crc & 0x80) != 0) {
                    crc = (crc << 1) ^ polynomial;
                } else {
                    crc <<= 1;
                }
            }
        }

        return crc & 0xFF;
    }
}

