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

package org.indunet.fastproto.benchmark.annotation;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.util.EncodeUtils;

import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

/**
 * Benchmark sample with optimized field alignment and primitive types.
 *
 * @author Deng Ran
 * @since 3.11.0
 */
@Data
@DefaultByteOrder(ByteOrder.LITTLE)
public class Sample {
    // 1-byte aligned fields
    @BoolType(byteOffset = 0, bitOffset = 0)
    boolean bool1;

    @Int8Type(offset = 1)
    byte byte8;

    // 2-byte aligned fields
    @Int16Type(offset = 2)
    short short16;

    // 4-byte aligned fields
    @Int32Type(offset = 4)
    int int32;

    @UInt32Type(offset = 8)
    long uint32;

    @FloatType(offset = 12)
    float float32;

    // 8-byte aligned fields
    @Int64Type(offset = 16)
    long long64;

    @DoubleType(offset = 24)
    double double64;

    // Additional fields
    @Int8Type(offset = 32)
    byte int8;

    @Int16Type(offset = 34)
    short int16;

    @UInt8Type(offset = 36)
    int uint8;

    @UInt16Type(offset = 38)
    int uint16;

    @BinaryType(offset = 40, length = 10)
    byte[] bytes;

    public Sample() {
        this(true);
    }

    public Sample(boolean initializeData) {
        if (initializeData) {
            val random = new Random();

            this.bool1 = random.nextBoolean();
            this.byte8 = (byte) random.nextInt(Byte.MAX_VALUE);
            this.short16 = (short) random.nextInt(Short.MAX_VALUE);
            this.int32 = random.nextInt();
            this.long64 = random.nextLong();
            this.float32 = random.nextFloat();
            this.double64 = random.nextDouble();
            this.int8 = (byte) random.nextInt(Byte.MAX_VALUE);
            this.int16 = (short) random.nextInt(Short.MAX_VALUE);
            this.uint8 = abs(random.nextInt(UInt8Type.MAX_VALUE));
            this.uint16 = abs(random.nextInt(UInt16Type.MAX_VALUE));
            this.uint32 = (long) abs(random.nextInt());

            val bytes = new byte[10];
            IntStream.range(0, bytes.length)
                    .forEach(i -> bytes[i] = (byte) random.nextInt(Byte.MAX_VALUE));
            this.bytes = bytes;
        }
    }

    public byte[] toBytes() {
        val bytes = new byte[60];

        EncodeUtils.writeBool(bytes, 0, 0, BitOrder.LSB_0, this.bool1);
        EncodeUtils.writeByte(bytes, 1, this.byte8);
        EncodeUtils.writeShort(bytes, 2, this.short16);
        EncodeUtils.writeInt32(bytes, 4, this.int32);
        EncodeUtils.writeUInt32(bytes, 8, this.uint32);
        EncodeUtils.writeFloat(bytes, 12, this.float32);
        EncodeUtils.writeInt64(bytes, 16, this.long64);
        EncodeUtils.writeDouble(bytes, 24, this.double64);
        EncodeUtils.writeInt8(bytes, 32, this.int8);
        EncodeUtils.writeInt16(bytes, 34, this.int16);
        EncodeUtils.writeUInt8(bytes, 36, this.uint8);
        EncodeUtils.writeUInt16(bytes, 38, this.uint16);
        EncodeUtils.writeBytes(bytes, 40, this.bytes);

        return bytes;
    }
}
