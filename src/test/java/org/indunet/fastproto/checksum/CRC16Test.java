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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the CRC16 class.
 * It tests the functionality of the CRC16 class, including getting and setting the polynomial and initial value, and calculating the CRC value.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
public class CRC16Test {
    protected CRC16 crc16;

    @BeforeEach
    void setUp() {
        crc16 = new CRC16();
    }

    @Test
    void getPolynomial_returnsCorrectPolynomial() {
        assertEquals(CRC16.CRC16_IBM_POLYNOMIAL, crc16.getPolynomial());
    }

    @Test
    void setPolynomial_setsCorrectPolynomial() {
        crc16.setPolynomial(CRC16.CRC16_CCITT_POLYNOMIAL);
        assertEquals(CRC16.CRC16_CCITT_POLYNOMIAL, crc16.getPolynomial());
    }

    @Test
    void getInitialValue_returnsCorrectInitialValue() {
        assertEquals(CRC16.CRC16_IBM_INITIAL_VALUE, crc16.getInitialValue());
    }

    @Test
    void setInitialValue_setsCorrectInitialValue() {
        crc16.setInitialValue(CRC16.CRC16_CCITT_INITIAL_VALUE);
        assertEquals(CRC16.CRC16_CCITT_INITIAL_VALUE, crc16.getInitialValue());
    }

    // @Test
    void calculate_returnsCorrectCRCValue() {
        byte[] data = {0x31, 0x32, 0x33, 0x34, 0x35};
        int expectedCRC = 0x55A4;

        System.out.println(String.format("0x%x", crc16.calculate(data)));

        assertEquals(expectedCRC, crc16.calculate(data));
    }

    @Test
    void calculate_returnsCorrectCRCValueForEmptyData() {
        byte[] data = {};
        int expectedCRC = CRC16.CRC16_IBM_INITIAL_VALUE;

        assertEquals(expectedCRC, crc16.calculate(data));
    }
}