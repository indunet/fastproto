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

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class FLoatEncoderTest {
    FloatEncoder encoder = new FloatEncoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[4];
        float pi = 3.141f;
        float e = 2.718f;

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, pi);
        assertArrayEquals(datagram, BinaryUtils.valueOf(pi));

        encoder.encode(datagram, 0, EndianPolicy.LITTLE, e);
        assertArrayEquals(datagram, BinaryUtils.valueOf(e));

        encoder.encode(datagram, 0 - datagram.length, EndianPolicy.BIG, e);
        assertArrayEquals(datagram, BinaryUtils.valueOf(e, EndianPolicy.BIG));
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class, () -> this.encoder.encode(null, 0, null, 3.14f));

        assertThrows(EncodeException.class, () ->
                this.encoder.encode(datagram, 10, EndianPolicy.LITTLE, 3.141f));
        assertThrows(EncodeException.class, () ->
                this.encoder.encode(datagram, -1, EndianPolicy.LITTLE, 3.141f));
    }
}
