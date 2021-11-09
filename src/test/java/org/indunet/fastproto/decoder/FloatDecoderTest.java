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

package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FloatDecoderTest {
    FloatDecoder decoder = new FloatDecoder();
    float pi = 3.141f;
    float e = 2.718f;

    @Test
    public void testDecode1() {
        assertEquals(decoder.decode(BinaryUtils.valueOf(pi), 0, EndianPolicy.LITTLE), pi, 0.0001);
        assertEquals(decoder.decode(BinaryUtils.valueOf(e), 0, EndianPolicy.LITTLE), e, 0.0001);

        assertEquals(decoder.decode(BinaryUtils.valueOf(e, EndianPolicy.BIG), 0, EndianPolicy.BIG), e, 0.0001);
        assertEquals(decoder.decode(BinaryUtils.valueOf(e, EndianPolicy.BIG), -4, EndianPolicy.BIG), e, 0.0001);
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 0, EndianPolicy.LITTLE));

        assertThrows(DecodingException.class, () -> this.decoder.decode(datagram, -1, EndianPolicy.BIG));
        assertThrows(DecodingException.class, () -> this.decoder.decode(datagram, 8, EndianPolicy.BIG));
    }
}
