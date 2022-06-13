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

package org.indunet.fastproto.resolve;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.EnableProtocolVersion;
import org.indunet.fastproto.exception.ResolveException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 3.2.0
 */
public class EnableProtocolVersionTest {
    @Test
    public void testResolve() {
        val bytes = new byte[10];

        assertThrows(ResolveException.class, () -> FastProto.parseFrom(bytes, Vehicle.class));
    }

    @EnableProtocolVersion(offset = 0, version = -10)
    public static class Vehicle {

    }
}
