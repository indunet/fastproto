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

package org.indunet.fastproto.formula;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FormulaRegistry}.
 *
 * @author Deng Ran
 * @since 3.13.0
 */
public class FormulaRegistryTest {

    @Test
    public void testBuildKeyWithClass() {
        String key = FormulaRegistry.buildKey(TestClass.class, "testField", "decoding");
        assertEquals("org.indunet.fastproto.formula.FormulaRegistryTest$TestClass#testField#decoding", key);
    }

    @Test
    public void testBuildKeyWithField() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("testField");
        String key = FormulaRegistry.buildKey(field, "encoding");
        assertEquals("org.indunet.fastproto.formula.FormulaRegistryTest$TestClass#testField#encoding", key);
    }

    @Test
    public void testGetReturnsNullWhenNotAvailable() {
        // When no generated registry exists, get should return null
        assertNull(FormulaRegistry.get("nonexistent#field#decoding"));
    }

    @Test
    public void testContainsReturnsFalseWhenNotAvailable() {
        // When no generated registry exists, contains should return false
        assertFalse(FormulaRegistry.contains("nonexistent#field#decoding"));
    }

    @Test
    public void testGetWithFieldReturnsNull() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("testField");
        assertNull(FormulaRegistry.get(field, "decoding"));
    }

    @Test
    public void testContainsWithFieldReturnsFalse() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("testField");
        assertFalse(FormulaRegistry.contains(field, "decoding"));
    }

    // Test helper class
    private static class TestClass {
        private int testField;
    }
}
