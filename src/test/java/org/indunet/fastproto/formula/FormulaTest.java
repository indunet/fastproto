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

package org.indunet.fastproto.formula;

import lombok.AllArgsConstructor;
import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;
import org.indunet.fastproto.annotation.Int8Type;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @since 2.0.0
 */
public class FormulaTest {
    @AllArgsConstructor
    public static class TestObject1 {
        @Int8Type(offset = 0)
        @EncodingFormula(PressureEncodeFormula.class)
        int pressure;
    }

    public static class PressureEncodeFormula implements Function<Double, Integer> {
        @Override
        public Integer apply(Double value) {
            return (int) (value * 10);
        }
    }

    @Test
    public void testEncodeFormula() {
        val object = new TestObject1(101);
        assertThrows(EncodingException.class, () -> FastProto.encode(object, 10));
    }

    @AllArgsConstructor
    public static class TestObject2 {
        @Int8Type(offset = 0)
        @DecodingFormula(PressureDecodeFormula.class)
        int pressure;
    }

    public static class PressureDecodeFormula implements Function<Integer, Double> {
        @Override
        public Double apply(Integer value) {
            return value * 0.1;
        }
    }

    @Test
    public void testDecodeFormula() {
        val datagram = new byte[10];

        assertThrows(DecodingException.class, () -> FastProto.decode(datagram, TestObject2.class));
    }

    @Test
    public void testDecodingLambda() throws IOException {
        val expected = new LambdaObject();
        val bytes = expected.toBytes();

        assertEquals(expected, FastProto.decode(bytes, LambdaObject.class));
    }

    @Test
    public void testEncodingLambda() throws IOException {
        val object = new LambdaObject();
        val expected = object.toBytes();

        assertArrayEquals(expected, FastProto.encode(object, expected.length));
    }
}
