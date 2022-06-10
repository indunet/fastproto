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

import lombok.val;
import lombok.var;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.OutOfBoundsException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @see ListDecoder
 * @since 2.3.0
 */
public class ListDecoderTest {
    ListDecoder decoder = new ListDecoder();

    @Test
    public void testDecode1() {
        val datagram = new byte[10];

        datagram[0] = 1;
        datagram[2] = 2;
        datagram[4] = 3;
        datagram[6] = 4;
        datagram[8] = 5;

        List<Integer> values1 = decoder.decode(datagram, 0, 5,
                ProtocolType.UINT16, EndianPolicy.LITTLE);
        List<Integer> values2 = decoder.decode(datagram, 0, 5,
                ProtocolType.INT16, EndianPolicy.LITTLE);
        List<Short> values3 = decoder.decode(datagram, 0, 5,
                ProtocolType.SHORT, EndianPolicy.LITTLE);

        assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, values1.toArray(new Integer[5]));
        assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, values2.toArray(new Integer[5]));
        assertArrayEquals(new Short[] {1, 2, 3, 4, 5}, values3.toArray(new Short[5]));
    }

    @Test
    public void testDecode2() {
        val datagram = new byte[10];

        datagram[1] = 1;
        datagram[3] = 2;
        datagram[5] = 3;
        datagram[7] = 4;
        datagram[9] = 5;

        List<Integer> values1 = decoder.decode(datagram, 0, 5,
                ProtocolType.UINT16, EndianPolicy.BIG);
        List<Integer> values2 = decoder.decode(datagram, 0, 5,
                ProtocolType.INT16, EndianPolicy.BIG);
        List<Short> values3 = decoder.decode(datagram, 0, 5,
                ProtocolType.SHORT, EndianPolicy.BIG);

        assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, values1.toArray(new Integer[5]));
        assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, values2.toArray(new Integer[5]));
        assertArrayEquals(new Short[] {1, 2, 3, 4, 5}, values3.toArray(new Short[5]));
    }

    @Test
    public void testDecode3() {
        val datagram = new byte[4];

        datagram[0] = 1;

        List<Integer> values1 = decoder.decode(datagram, 0, 4,
                ProtocolType.UINT8, EndianPolicy.BIG);
        List<Integer> values2 = decoder.decode(datagram, 0, 1,
                ProtocolType.INT32, EndianPolicy.BIG);
        List<Long> value3 = decoder.decode(datagram, 0, 1,
                ProtocolType.UINT32, EndianPolicy.LITTLE);
        List<Integer> value4 = decoder.decode(datagram, 0, 4,
                ProtocolType.INT8, EndianPolicy.BIG);
        List<Byte> value5 = decoder.decode(datagram, 0, 4,
                ProtocolType.BYTE, EndianPolicy.BIG);

        assertArrayEquals(new Integer[] {1, 0, 0, 0}, values1.toArray(new Integer[4]));
        assertArrayEquals(new Integer[] {256 * 256 * 256}, values2.toArray(new Integer[1]));
        assertArrayEquals(new Long[] {1L}, value3.toArray(new Long[1]));
        assertArrayEquals(new Integer[] {1, 0, 0, 0}, value4.toArray(new Integer[4]));
        assertArrayEquals(new Byte[] {1, 0, 0, 0}, value5.toArray(new Byte[4]));
    }

    @Test
    public void testDecode4() {
        // Long
        var datagram = BinaryUtils.valueOf(10L, EndianPolicy.BIG);
        List<Long> value = decoder.decode(datagram, 0, 1,
                ProtocolType.LONG, EndianPolicy.BIG);
        assertArrayEquals(new Long[] {10L}, value.toArray(new Long[1]));

        // Float
        datagram = BinaryUtils.valueOf(3.14f);
        List<Float> value2 = decoder.decode(datagram, 0, 1,
                ProtocolType.FLOAT, EndianPolicy.LITTLE);
        assertArrayEquals(new Float[] {3.14f}, value2.toArray(new Float[1]));

        // Double
        datagram = BinaryUtils.valueOf(3.14);
        List<Double> value3 = decoder.decode(datagram, 0, 1,
                ProtocolType.DOUBLE, EndianPolicy.LITTLE);
        assertArrayEquals(new Double[] {3.14}, value3.toArray(new Double[1]));
    }

    @Test
    public void testDecode5() {
        val datagram = new byte[10];
        assertThrows(OutOfBoundsException.class, () -> {
            decoder.decode(datagram, 0, 11, ProtocolType.BYTE, EndianPolicy.LITTLE);
        });
        assertThrows(OutOfBoundsException.class, () -> {
            decoder.decode(datagram, 0, 6, ProtocolType.UINT16, EndianPolicy.LITTLE);
        });
        assertThrows(DecodingException.class, () -> {
            decoder.decode(datagram, 0, -2, ProtocolType.BYTE, EndianPolicy.LITTLE);
        });
        assertThrows(DecodingException.class, () -> {
            decoder.decode(datagram, -3, 10, ProtocolType.BYTE, EndianPolicy.LITTLE);
        });
    }
}
