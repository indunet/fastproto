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
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.ListType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
                ProtocolType.UINTEGER16, EndianPolicy.LITTLE);
        List<Integer> values2 = decoder.decode(datagram, 0, 5,
                ProtocolType.INTEGER16, EndianPolicy.LITTLE);
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
                ProtocolType.UINTEGER16, EndianPolicy.BIG);
        List<Integer> values2 = decoder.decode(datagram, 0, 5,
                ProtocolType.INTEGER16, EndianPolicy.BIG);
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
                ProtocolType.UINTEGER8, EndianPolicy.BIG);
        List<Integer> values2 = decoder.decode(datagram, 0, 1,
                ProtocolType.INTEGER, EndianPolicy.BIG);
        List<Long> value3 = decoder.decode(datagram, 0, 1,
                ProtocolType.UINTEGER32, EndianPolicy.LITTLE);
        List<Integer> value4 = decoder.decode(datagram, 0, 4,
                ProtocolType.INTEGER8, EndianPolicy.BIG);
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
        assertThrows(DecodeException.class, () -> {
            decoder.decode(datagram, 0, 11, ProtocolType.BYTE, EndianPolicy.LITTLE);
        });
        assertThrows(DecodeException.class, () -> {
            decoder.decode(datagram, 0, 6, ProtocolType.UINTEGER16, EndianPolicy.LITTLE);
        });
        assertThrows(DecodeException.class, () -> {
            decoder.decode(datagram, 0, -2, ProtocolType.BYTE, EndianPolicy.LITTLE);
        });
        assertThrows(DecodeException.class, () -> {
            decoder.decode(datagram, -3, 10, ProtocolType.BYTE, EndianPolicy.LITTLE);
        });
    }

    public static class TestObject1 {
        @ListType(value = 0, protocolType = ProtocolType.UINTEGER8, length = 2)
        List<Integer> list;
    }

    public static class TestObject2 {
        @ListType(value = 0, protocolType = ProtocolType.UINTEGER8, length = 2)
        List<Double> list;
    }

    public static class TestObject3 {
        @ListType(value = 0, protocolType = ProtocolType.BINARY, length = 2)
        List<Double> list;
    }

    @Test
    public void testDecode6() {
        val datagram = new byte[10];
        val object = FastProto.parseFrom(datagram, TestObject1.class);

        assertNotNull(object);
        assertThrows(ResolveException.class, () -> FastProto.parseFrom(datagram, TestObject2.class));
        assertThrows(ResolveException.class, () -> FastProto.parseFrom(datagram, TestObject3.class));
    }
}
