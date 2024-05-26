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
 * This interface defines the basic methods of CRC verification, including getting and setting polynomials, initial values, and calculating CRC values.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
public interface CRC {
    /**
     * Get the polynomial for CRC verification.
     *
     * @return The polynomial for CRC verification.
     */
    int getPolynomial();

    /**
     * Set the polynomial for CRC verification.
     *
     * @param polynomial The polynomial for CRC verification.
     */
    void setPolynomial(int polynomial);

    /**
     * Get the initial value for CRC verification.
     *
     * @return The initial value for CRC verification.
     */
    int getInitialValue();

    /**
     * Set the initial value for CRC verification.
     *
     * @param initialValue The initial value for CRC verification.
     */
    void setInitialValue(int initialValue);

    /**
     * Calculate the CRC value of the given data.
     *
     * @param data The data to be verified by CRC.
     * @return The calculated CRC value.
     */
    int calculate(byte[] data);
}
