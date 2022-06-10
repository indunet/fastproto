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

package org.indunet.fastproto;

import lombok.SneakyThrows;
import lombok.val;
import org.indunet.fastproto.annotation.type.UInt8Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Deng Ran
 * @since 3.2.0
 */
public class ProtocolTypeTest {
    @Test
    @SneakyThrows
    public void testProxy() {
        val field = TestObj.class.getDeclaredField("value");
        val typeAnnotation = field.getAnnotation(UInt8Type.class);
        val proxy = ProtocolType.proxy(typeAnnotation);

        assertEquals(0, proxy.value());
        assertEquals(UInt8Type.SIZE, proxy.size());
    }

    public static class TestObj {
        @UInt8Type(0)
        int value;
    }
}
