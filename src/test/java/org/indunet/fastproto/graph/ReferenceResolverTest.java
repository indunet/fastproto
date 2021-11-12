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

package org.indunet.fastproto.graph;

import lombok.val;
import org.indunet.fastproto.annotation.type.BooleanType;
import org.indunet.fastproto.annotation.type.DoubleType;
import org.indunet.fastproto.annotation.type.Integer8Type;
import org.indunet.fastproto.annotation.type.UInteger8Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Reference Resolver Test.
 *
 * @author Deng Ran
 * @since 3.0.0
 */
public class ReferenceResolverTest {
    public static class TestObject1 {
        @UInteger8Type(0)
        int value1;

        @DoubleType(2)
        double value2;

        TestObject2 value3;
        TestObject3 value4;

        TestObject2 value5;
    }

    public static class TestObject2 {
        TestObject3 value1;

        @Integer8Type(3)
        int value2;
    }

    public static class TestObject3 {
        TestObject2 value1;

        @BooleanType(value = 3, bitOffset = 1)
        boolean value2;
    }

    @Test
    public void testResolve() throws NoSuchFieldException {
        val graph = ReferenceResolver.resolve(TestObject1.class);

        graph.print();
        assertNotNull(graph);
    }
}
