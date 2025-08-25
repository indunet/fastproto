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
 * CRC (Cyclic Redundancy Check) is a common method for data verification.
 * Its main function is to detect whether there are errors in the data transmission process.
 * This abstract class defines the basic methods of CRC verification, including getting and setting polynomials, initial values, and calculating CRC values.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
public abstract class CRC {
    /**
     * Get the polynomial for CRC verification.
     *
     * @return The polynomial for CRC verification.
     */
    abstract int getPolynomial();

    /**
     * Set the polynomial for CRC verification.
     *
     * @param polynomial The polynomial for CRC verification.
     */
    abstract void setPolynomial(int polynomial);

    /**
     * Get the initial value for CRC verification.
     *
     * @return The initial value for CRC verification.
     */
    abstract int getInitialValue();

    /**
     * Set the initial value for CRC verification.
     *
     * @param initialValue The initial value for CRC verification.
     */
    abstract void setInitialValue(int initialValue);

    /**
     * Calculate the CRC value of the given data.
     *
     * @param data The data to be verified by CRC.
     * @return The calculated CRC value.
     */
    abstract int calculate(byte[] data);

    /**
     * Calculate the CRC value of the given data range to avoid byte array copying.
     *
     * @param data The data to be verified by CRC.
     * @param offset The offset to start calculation from.
     * @param length The length of data to calculate.
     * @return The calculated CRC value.
     */
    abstract int calculate(byte[] data, int offset, int length);

    public int reverseBits(int value, int bitSize) {
        int reversed = 0;
        for (int i = 0; i < bitSize; i++) {
            reversed = (reversed << 1) | (value & 1);
            value >>= 1;
        }
        return reversed;
    }

    protected static byte reverseBits(byte value) {
        int reversed = 0;
        for (int i = 0; i < 8; i++) {
            reversed = (reversed << 1) | (value & 1);
            value >>= 1;
        }
        return (byte) reversed;
    }
}
