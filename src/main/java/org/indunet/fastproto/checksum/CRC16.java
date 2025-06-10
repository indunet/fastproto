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
 * The CRC16 class implements the CRC interface and defines the CRC16 checksum calculation.
 * It provides the polynomial and initial value for CRC16-IBM, CRC16-CCITT, and CRC16-MODBUS.
 * The calculate method is used to calculate the CRC16 value of the given data.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
public class CRC16 extends CRC {
    public static final int CRC16_IBM_POLYNOMIAL = 0x8005;
    public static final int CRC16_IBM_INITIAL_VALUE = 0x0000;

    public static final int CRC16_CCITT_POLYNOMIAL = 0x1021;
    public static final int CRC16_CCITT_INITIAL_VALUE = 0xFFFF;

    public static final int CRC16_MODBUS_POLYNOMIAL = 0x8005;
    public static final int CRC16_MODBUS_INITIAL_VALUE = 0xFFFF;

    protected int polynomial;
    protected int initialValue;

    public CRC16() {
        this.initialValue = CRC16_IBM_INITIAL_VALUE;
        this.polynomial = CRC16_IBM_POLYNOMIAL;
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
            b = reverseBits(b);  // 输入数据翻转
            crc ^= ((b & 0xFF) << 8);

            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ polynomial;
                } else {
                    crc <<= 1;
                }
            }
        }

        crc &= 0xFFFF;
        crc = reverseBits(crc, 16);  // 输出数据翻转
        return crc;
    }
}
