/*
 * Copyright 2019-2022 indunet.org
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

package org.indunet.fastproto.api.auto;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.AutoType;
import org.indunet.fastproto.util.CodecUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Test object2.
 *
 * @author Deng Ran
 * @since 3.7.2
 */
@Data
public class TestObject1 {
    @AutoType(byteOffset = 0, bitOffset = 1)
    Boolean bool1;

    @AutoType(offset = 2)
    Byte byte8;

    @AutoType(offset = 4)
    Short short16;

    @AutoType(offset = 6)
    Integer int32;

    @AutoType(offset = 10)
    Long long64;

    @AutoType(offset = 18, byteOrder = ByteOrder.BIG)
    Date date;

    @AutoType(offset = 26, length = 8)
    List<Integer> ints;

    public TestObject1() {
        val random = new Random();

        this.bool1 = random.nextBoolean();
        this.byte8 = (byte) random.nextInt(128);
        this.short16 = (short) random.nextInt(Short.MAX_VALUE);
        this.int32 = random.nextInt();
        this.long64 = random.nextLong();
        this.date = new Date();
        this.ints = IntStream.range(0, 8)
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toList());
    }

    public byte[] toBytes() {
        val bytes = new byte[58];

        CodecUtils.boolType(bytes, 0, 1, BitOrder.LSB_0, this.bool1);
        CodecUtils.byteType(bytes, 2, this.byte8);
        CodecUtils.shortType(bytes, 4, ByteOrder.LITTLE, this.short16);
        CodecUtils.int32Type(bytes, 6, ByteOrder.LITTLE, this.int32);
        CodecUtils.int64Type(bytes, 10, ByteOrder.LITTLE, this.long64);
        CodecUtils.int64Type(bytes, 18, ByteOrder.BIG, this.date.getTime());

        IntStream.range(0, this.ints.size())
                .forEach(i -> CodecUtils.int32Type(bytes, 26 + i * 4, ByteOrder.LITTLE, this.ints.get(i)));

        return bytes;
    }
}
