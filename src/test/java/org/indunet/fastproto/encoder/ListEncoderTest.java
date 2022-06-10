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
import lombok.var;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Deng Ran
 * @see ListEncoder
 * @since 2.3.0
 */
public class ListEncoderTest {
    ListEncoder encoder = new ListEncoder();

    @Test
    public void testEncode1() {
        List<Integer> values1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Short> values2 = Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
        val datagram = new byte[10];

        datagram[0] = 1;
        datagram[2] = 2;
        datagram[4] = 3;
        datagram[6] = 4;
        datagram[8] = 5;

        encoder.encode(datagram, 0, 5, ProtocolType.INT16, EndianPolicy.LITTLE, values1);
        encoder.encode(datagram, 0, 5, ProtocolType.UINT16, EndianPolicy.LITTLE, values1);
        encoder.encode(datagram, 0, 5, ProtocolType.SHORT, EndianPolicy.LITTLE, values2);
    }

    @Test
    public void testEncode2() {
        List<Integer> values1 = Arrays.asList(1);
        val datagram = new byte[4];

        datagram[0] = 1;
        encoder.encode(datagram, 0, 1, ProtocolType.INT32, EndianPolicy.LITTLE, values1);

        List<Long> values2 = Arrays.asList(1L);
        datagram[0] = 1;
        encoder.encode(datagram, 0, 1, ProtocolType.UINT32, EndianPolicy.LITTLE, values2);
    }

    @Test
    public void testEncode3() {
        List<Float> values1 = Arrays.asList(3.14f);
        var datagram = BinaryUtils.valueOf(3.14f);

        datagram[0] = 1;
        encoder.encode(datagram, 0, 1, ProtocolType.FLOAT, EndianPolicy.LITTLE, values1);

        List<Double> values2 = Arrays.asList(3.14);
        datagram = BinaryUtils.valueOf(3.14);
        encoder.encode(datagram, 0, 1, ProtocolType.DOUBLE, EndianPolicy.LITTLE, values2);
    }
}
