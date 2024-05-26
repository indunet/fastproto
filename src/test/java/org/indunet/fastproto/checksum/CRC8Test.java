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
 * This class contains unit tests for the CRC8 class.
 * It tests the functionality of the CRC8 class, including getting and setting the polynomial and initial value, and calculating the CRC value.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
public class CRC8Test {
    protected CRC8 crc8;

    @BeforeEach
    void setUp() {
        crc8 = new CRC8();
    }

    @Test
    void getPolynomial_returnsCorrectPolynomial() {
        assertEquals(CRC8.CRC8_POLYNOMIAL, crc8.getPolynomial());
    }

    @Test
    void setPolynomial_setsCorrectPolynomial() {
        crc8.setPolynomial(CRC8.CRC8_CCITT_POLYNOMIAL);
        assertEquals(CRC8.CRC8_CCITT_POLYNOMIAL, crc8.getPolynomial());
    }

    @Test
    void getInitialValue_returnsCorrectInitialValue() {
        assertEquals(CRC8.CRC8_INITIAL_VALUE, crc8.getInitialValue());
    }

    @Test
    void setInitialValue_setsCorrectInitialValue() {
        crc8.setInitialValue(CRC8.CRC8_CCITT_INITIAL_VALUE);
        assertEquals(CRC8.CRC8_CCITT_INITIAL_VALUE, crc8.getInitialValue());
    }

    @Test
    void calculate_returnsCorrectCRCValue() {
        byte[] data = {0x31, 0x32, 0x33, 0x34, 0x35};
        int expectedCRC = 0xCB;

        assertEquals(expectedCRC, crc8.calculate(data));
    }

    @Test
    void calculate_returnsCorrectCRCValueForEmptyData() {
        byte[] data = {};
        int expectedCRC = CRC8.CRC8_INITIAL_VALUE;

        assertEquals(expectedCRC, crc8.calculate(data));
    }
}