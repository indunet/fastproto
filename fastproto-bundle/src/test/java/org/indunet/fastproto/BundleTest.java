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

package org.indunet.fastproto;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the fastproto bundle module.
 * These tests verify that both core functionality and annotation processor work correctly together.
 *
 * @author Deng Ran
 * @since 4.1.0
 */
public class BundleTest {

    /**
     * Test basic encode/decode functionality (verifies core module works).
     */
    @Test
    public void testBasicEncodeDecode() {
        val original = new SimpleProtocol();
        original.setId(42);
        original.setValue(12345);
        original.setFlag(true);

        // Encode
        byte[] bytes = FastProto.encode(original, 8);
        assertNotNull(bytes);
        assertEquals(8, bytes.length);

        // Decode
        SimpleProtocol decoded = FastProto.decode(bytes, SimpleProtocol.class);
        assertNotNull(decoded);
        assertEquals(original.getId(), decoded.getId());
        assertEquals(original.getValue(), decoded.getValue());
        assertEquals(original.isFlag(), decoded.isFlag());
    }

    /**
     * Test lambda formula functionality (verifies processor-generated code works).
     */
    @Test
    public void testLambdaFormula() {
        val original = new FormulaProtocol();
        original.setTemperature(25.5);  // Engineering value
        original.setPressure(1013.25);  // Engineering value

        // Encode - formulas should convert engineering values to raw values
        byte[] bytes = FastProto.encode(original, 8);
        assertNotNull(bytes);

        // Decode - formulas should convert raw values back to engineering values
        FormulaProtocol decoded = FastProto.decode(bytes, FormulaProtocol.class);
        assertNotNull(decoded);
        
        // Allow small floating point tolerance
        assertEquals(original.getTemperature(), decoded.getTemperature(), 0.1);
        assertEquals(original.getPressure(), decoded.getPressure(), 0.1);
    }

    /**
     * Test array types (verifies more complex core functionality).
     */
    @Test
    public void testArrayTypes() {
        val original = new ArrayProtocol();
        original.setValues(new int[]{100, 200, 300});

        byte[] bytes = FastProto.encode(original, 12);
        assertNotNull(bytes);

        ArrayProtocol decoded = FastProto.decode(bytes, ArrayProtocol.class);
        assertNotNull(decoded);
        assertArrayEquals(original.getValues(), decoded.getValues());
    }
}
