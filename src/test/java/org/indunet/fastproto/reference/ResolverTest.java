/*
 * Copyright 2019-2021 indunet.org
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

package org.indunet.fastproto.reference;

import lombok.val;
import org.indunet.fastproto.annotation.type.BoolType;
import org.indunet.fastproto.annotation.type.DoubleType;
import org.indunet.fastproto.annotation.type.Int8Type;
import org.indunet.fastproto.annotation.type.UInt8Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Reference Resolver Test.
 *
 * @author Deng Ran
 * @since 3.0.0
 */
public class ResolverTest {
    public static class TestObject1 {
        @UInt8Type(offset = 0)
        int value1;

        @DoubleType(offset = 2)
        double value2;

        TestObject2 value3;
        TestObject3 value4;

        TestObject2 value5;
    }

    public static class TestObject2 {
        TestObject3 value1;

        @Int8Type(offset = 3)
        int value2;
    }

    public static class TestObject3 {
        TestObject2 value1;

        @BoolType(byteOffset = 3, bitOffset = 1)
        boolean value2;
    }

    @Test
    public void testResolve() {
        val graph = Resolver.resolve(TestObject1.class);

        // graph.print();
        assertNotNull(graph);
    }

    @Test
    public void testDecodeContext() {
        val graph = Resolver.resolve(TestObject1.class);
        val contexts = graph.decodeReferences();

        assertTrue(contexts.size() > 3);
    }

    @Test
    public void testEncodeContext() {
        val graph = Resolver.resolve(TestObject1.class);
        val contexts = graph.encodeReferences(new TestObject1());

        // No encode context would be generate while null object.
        assertNotNull(contexts);
    }
}
