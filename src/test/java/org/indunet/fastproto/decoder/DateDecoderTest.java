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

package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Double type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 2.1.0
 */
public class DateDecoderTest {
    DateDecoder decoder = new DateDecoder();

    @ParameterizedTest
    @MethodSource
    public void testDecode1(byte[] datagram, int byteOffset, ProtocolType type, EndianPolicy policy, TimeUnit unit, Date expected) {
        Date value = decoder.decode(datagram, byteOffset, type, policy, unit);

        assertEquals(expected, value);
    }

    public static List<Arguments> testDecode1() {
        long current = System.currentTimeMillis();

        return Stream.of(
                Arguments.arguments(BinaryUtils.valueOf(current), 0, ProtocolType.LONG, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, new Date(current)),
                Arguments.arguments(BinaryUtils.uint32of(current / 1000), 0, ProtocolType.UINTEGER32, EndianPolicy.LITTLE, TimeUnit.SECONDS, new Date(current / 1000 * 1000)),
                Arguments.arguments(BinaryUtils.uint32of(current / 1000), -4, ProtocolType.UINTEGER32, EndianPolicy.LITTLE, TimeUnit.SECONDS, new Date(current / 1000 * 1000))
        ).collect(Collectors.toList());
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 0, ProtocolType.LONG, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS));

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -1, ProtocolType.LONG, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, ProtocolType.LONG, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS));
    }
}
