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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Array decoder.
 *
 * @author Deng Ran
 * @since 2.2.0
 */
public class ArrayDecoderTest {
    ArrayDecoder decoder = new ArrayDecoder();

    @Test
    public void testDecode1() {
        val datagram = new byte[10];

        datagram[0] = 1;
        datagram[2] = 2;
        datagram[4] = 3;
        datagram[6] = 4;
        datagram[8] = 5;

        Integer[] values1 = (Integer[]) (decoder.decode(datagram, 0, 5,
                        ProtocolType.UINTEGER16, EndianPolicy.LITTLE));
        Integer[] values2 = (Integer[]) (decoder.decode(datagram, 0, 5,
                ProtocolType.INTEGER16, EndianPolicy.LITTLE));
        Short[] values3 = (Short[]) (decoder.decode(datagram, 0, 5,
                ProtocolType.SHORT, EndianPolicy.LITTLE));

        int[] values4 = (int[]) (decoder.decode(datagram, 0, 5,
                ProtocolType.UINTEGER16, EndianPolicy.LITTLE, true));
        int[] values5 = (int[]) (decoder.decode(datagram, 0, 5,
                ProtocolType.INTEGER16, EndianPolicy.LITTLE, true));
        short[] values6 = (short[]) (decoder.decode(datagram, 0, 5,
                ProtocolType.SHORT, EndianPolicy.LITTLE, true));

        assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, values1);
        assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, values2);
        assertArrayEquals(new Short[] {1, 2, 3, 4, 5}, values3);

        assertArrayEquals(new int[] {1, 2, 3, 4, 5}, values4);
        assertArrayEquals(new int[] {1, 2, 3, 4, 5}, values5);
        assertArrayEquals(new short[] {1, 2, 3, 4, 5}, values6);
    }

    @Test
    public void testDecode2() {
        val datagram = new byte[10];

        datagram[1] = 1;
        datagram[3] = 2;
        datagram[5] = 3;
        datagram[7] = 4;
        datagram[9] = 5;

        Integer[] values1 = (Integer[]) (decoder.decode(datagram, 0, 5,
                ProtocolType.UINTEGER16, EndianPolicy.BIG));
        Integer[] values2 = (Integer[]) (decoder.decode(datagram, 0, 5,
                ProtocolType.INTEGER16, EndianPolicy.BIG));
        Short[] values3 = (Short[]) (decoder.decode(datagram, 0, 5,
                ProtocolType.SHORT, EndianPolicy.BIG));

        assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, values1);
        assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, values2);
        assertArrayEquals(new Short[] {1, 2, 3, 4, 5}, values3);
    }

    @Test
    public void testDecode3() {
        val datagram = new byte[4];

        datagram[0] = 1;

        Integer[] values1 = (Integer[]) (decoder.decode(datagram, 0, 4,
                ProtocolType.UINTEGER8, EndianPolicy.BIG));
        Integer[] values2 = (Integer[]) (decoder.decode(datagram, 0, 1,
                ProtocolType.INTEGER, EndianPolicy.BIG));
        Long[] value3 = (Long[]) (decoder.decode(datagram, 0, 1,
                ProtocolType.UINTEGER32, EndianPolicy.LITTLE));
        Integer[] value4 = (Integer[]) (decoder.decode(datagram, 0, 4,
                ProtocolType.INTEGER8, EndianPolicy.BIG));
        Byte[] value5 = (Byte[]) (decoder.decode(datagram, 0, 4,
                ProtocolType.BYTE, EndianPolicy.BIG));

        assertArrayEquals(new Integer[] {1, 0, 0, 0}, values1);
        assertArrayEquals(new Integer[] {256 * 256 * 256}, values2);
        assertArrayEquals(new Long[] {1L}, value3);
        assertArrayEquals(new Integer[] {1, 0, 0, 0}, value4);
        assertArrayEquals(new Byte[] {1, 0, 0, 0}, value5);
    }

    @Test
    public void testDecode4() {
        // Long
        var datagram = BinaryUtils.valueOf(10L, EndianPolicy.BIG);
        Long[] value = (Long[]) (decoder.decode(datagram, 0, 1,
                ProtocolType.LONG, EndianPolicy.BIG));
        assertArrayEquals(new Long[] {10L}, value);

        // Float
        datagram = BinaryUtils.valueOf(3.14f);
        Float[] value2 = (Float[]) (decoder.decode(datagram, 0, 1,
                ProtocolType.FLOAT, EndianPolicy.LITTLE));
        assertArrayEquals(new Float[] {3.14f}, value2);

        // Double
        datagram = BinaryUtils.valueOf(3.14);
        Double[] value3 = (Double[]) (decoder.decode(datagram, 0, 1,
                ProtocolType.DOUBLE, EndianPolicy.LITTLE));
        assertArrayEquals(new Double[] {3.14}, value3);
    }

    @Test
    public void testDecode5() {
        val datagram = new byte[10];
        assertThrows(OutOfBoundsException.class, () -> {
            decoder.decode(datagram, 0, 11, ProtocolType.BYTE, EndianPolicy.LITTLE);
        });
        assertThrows(OutOfBoundsException.class, () -> {
            decoder.decode(datagram, 0, 6, ProtocolType.UINTEGER16, EndianPolicy.LITTLE);
        });
        assertThrows(DecodingException.class, () -> {
            decoder.decode(datagram, 0, -2, ProtocolType.BYTE, EndianPolicy.LITTLE);
        });
        assertThrows(DecodingException.class, () -> {
            decoder.decode(datagram, -3, 10, ProtocolType.BYTE, EndianPolicy.LITTLE);
        });
    }
}
