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
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class TimestampEncoderTest {
    TimestampEncoder encoder = new TimestampEncoder();

    @ParameterizedTest
    @MethodSource
    public void testEncode1(byte[] datagram, int byteOffset, ProtocolType type, EndianPolicy policy, TimeUnit unit, Timestamp value, byte[] expected) {
        encoder.encode(datagram, byteOffset, type, policy, unit, value);
        assertArrayEquals(datagram, expected);
    }

    public static List<Arguments> testEncode1() {
        long current = System.currentTimeMillis();

        return Stream.of(
                Arguments.arguments(new byte[8], 0, ProtocolType.LONG,
                        EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, new Timestamp(current), BinaryUtils.valueOf(current)),
                Arguments.arguments(new byte[8], -8, ProtocolType.LONG,
                        EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, new Timestamp(current), BinaryUtils.valueOf(current)),
                Arguments.arguments(new byte[4], 0, ProtocolType.UINTEGER32,
                        EndianPolicy.LITTLE, TimeUnit.SECONDS, new Timestamp(current), BinaryUtils.uint32of(current / 1000))
        ).collect(Collectors.toList());
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class,
                () -> this.encoder.encode(null, 0, ProtocolType.LONG, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, new Timestamp(System.currentTimeMillis())));
        assertThrows(NullPointerException.class,
                () -> this.encoder.encode(null, 0, ProtocolType.LONG, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, null));

        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, -1, ProtocolType.LONG, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, new Timestamp(System.currentTimeMillis())));
        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, 10, ProtocolType.LONG, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, new Timestamp(System.currentTimeMillis())));
    }
}