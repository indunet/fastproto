/*
 * Copyright 2019-2022 indunet.org
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

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test of Formula Builder.
 *
 * @author Deng Ran
 * @since 3.7.0
 */
public class FormulaBuilderTest {
    @Test
    public void testBuild() {
        val lambda = "x -> x * 0.1";
        val builder = FormulaBuilder.create(Integer.class, lambda);

        assertNotNull(builder.build());
    }
}
