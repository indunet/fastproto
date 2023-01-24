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

package org.indunet.fastproto.benchmark.chain;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.util.CodecUtils;

import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

/**
 * Benchmark sample.
 *
 * @author Deng Ran
 * @since 3.9.1
 */
@Data
public class Sample {
    Boolean bool1;
    Byte byte8;
    Short short16;
    Integer int32;
    Long long64;
    Float float32;
    Double double64;
    Integer int8;
    Integer int16;
    Integer uint8;
    Integer uint16;
    Long uint32;
    byte[] bytes;

    public Sample() {
        val random = new Random();

        this.bool1 = random.nextBoolean();
        this.byte8 = (byte) random.nextInt(Byte.MAX_VALUE);
        this.short16 = (short) random.nextInt(Short.MAX_VALUE);
        this.int32 = random.nextInt();
        this.long64 = random.nextLong();
        this.float32 = random.nextFloat();
        this.double64 = random.nextDouble();
        this.int8 = random.nextInt(Byte.MAX_VALUE);
        this.int16 = random.nextInt(Short.MAX_VALUE);
        this.uint8 = abs(random.nextInt(UInt8Type.MAX_VALUE));
        this.uint16 = abs(random.nextInt(UInt16Type.MAX_VALUE));
        this.uint32 = (long) abs(random.nextInt());

        val bytes = new byte[10];

        IntStream.range(0, bytes.length)
                .forEach(i -> bytes[i] = (byte) random.nextInt(Byte.MAX_VALUE));
        this.bytes = bytes;
    }

    public byte[] toBytes() {
        val bytes = new byte[60];

        CodecUtils.boolType(bytes, 0, 1, BitOrder.LSB_0, this.getBool1());
        CodecUtils.byteType(bytes, 1, this.getByte8());
        CodecUtils.shortType(bytes, 2, ByteOrder.LITTLE, this.getShort16());
        CodecUtils.int32Type(bytes, 4, ByteOrder.LITTLE, this.getInt32());
        CodecUtils.int64Type(bytes, 8, ByteOrder.LITTLE, this.getLong64());
        CodecUtils.floatType(bytes, 16, ByteOrder.LITTLE, this.getFloat32());
        CodecUtils.doubleType(bytes, 20, ByteOrder.LITTLE, this.getDouble64());
        CodecUtils.int8Type(bytes, 28, this.getInt8());
        CodecUtils.int16Type(bytes, 30, ByteOrder.LITTLE, this.getInt16());
        CodecUtils.uint8Type(bytes, 32, this.getUint8());
        CodecUtils.uint16Type(bytes, 34, ByteOrder.LITTLE, this.getUint16());
        CodecUtils.uint32Type(bytes, 36, ByteOrder.LITTLE, this.getUint32());
        CodecUtils.binaryType(bytes, 40, -1, this.getBytes());

        return bytes;
    }
}
