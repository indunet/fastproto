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
 * This class contains unit tests for the CRC32 class.
 * It tests the functionality of the CRC32 class, including getting and setting the polynomial and initial value, and calculating the CRC value.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
public class CRC32Test {
    protected CRC32 crc32;

    @BeforeEach
    public void setUp() {
        crc32 = new CRC32();
    }

    @Test
    public void testCalculate1() {
        byte[] data = {0x31, 0x32, 0x33, 0x34, 0x35};
        int expectedCRC = 0xCBF53A1C;

        assertEquals(expectedCRC, crc32.calculate(data));
    }

    @Test
    public void testCalculate2() {
        byte[] data = {0x41, 0x42, 0x43, 0x44, 0x45};
        int expectedCRC = 0x72D31AD5;

        assertEquals(expectedCRC, crc32.calculate(data));
    }

    @Test
    public void testCalculate3() {
        byte[] data = {};   // empty data
        int expectedCRC = CRC32.DEFAULT_INITIAL_VALUE ^ 0xFFFFFFFF;

        assertEquals(expectedCRC, crc32.calculate(data));
    }
}