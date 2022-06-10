/*
 * Copyright 2019-2021 indunet
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

package org.indunet.fastproto.encoder;

import lombok.val;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.type.BoolType;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class EncoderFactoryTest {
    @Test
    public void testGetEncoder() {
        val clazz = Optional.of(BoolType.class)
                .map(c -> c.getAnnotation(Encoder.class))
                .map(Encoder::value)
                .get();
        val consumer = EncoderFactory.getEncoder(clazz);

        assertNotNull(consumer);
    }

    @Test
    public void testGetFormula() {
        Function func = EncoderFactory.getFormula(Formula.class);

        assertNotNull(func);
    }

    public static class Formula implements Function<Boolean, Integer> {
        @Override
        public Integer apply(Boolean value) {
            return value ? 1 : 0;
        }
    }
}
