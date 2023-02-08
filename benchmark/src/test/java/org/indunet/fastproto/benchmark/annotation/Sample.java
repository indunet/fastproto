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
 * Benchmark sample.
 *
 * @author Deng Ran
 * @since 1.4.0
 */
@Data
public class Sample {
    @BoolType(byteOffset = 0, bitOffset = 1)
    Boolean bool1;

    @Int8Type(offset = 1)
    Byte byte8;

    @Int16Type(offset = 2)
    Short short16;

    @Int32Type(offset = 4)
    Integer int32;

    @Int64Type(offset = 8)
    Long long64;

    @FloatType(offset = 16)
    Float float32;

    @DoubleType(offset = 20)
    Double double64;

    @Int8Type(offset = 28)
    Integer int8;

    @Int16Type(offset = 30)
    Integer int16;

    @UInt8Type(offset = 32)
    Integer uint8;

    @UInt16Type(offset = 34)
    Integer uint16;

    @UInt32Type(offset = 36)
    Long uint32;

    @BinaryType(offset = 40, length = 10)
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

        EncodeUtils.writeBool(bytes, 0, 1, BitOrder.LSB_0, this.getBool1());
        EncodeUtils.writeByte(bytes, 1, this.getByte8());
        EncodeUtils.writeShort(bytes, 2, ByteOrder.LITTLE, this.getShort16());
        EncodeUtils.writeInt32(bytes, 4, ByteOrder.LITTLE, this.getInt32());
        EncodeUtils.writeInt64(bytes, 8, ByteOrder.LITTLE, this.getLong64());
        EncodeUtils.writeFloat(bytes, 16, ByteOrder.LITTLE, this.getFloat32());
        EncodeUtils.writeDouble(bytes, 20, ByteOrder.LITTLE, this.getDouble64());
        EncodeUtils.writeInt8(bytes, 28, this.getInt8());
        EncodeUtils.writeInt16(bytes, 30, ByteOrder.LITTLE, this.getInt16());
        EncodeUtils.writeUInt8(bytes, 32, this.getUint8());
        EncodeUtils.writeUInt16(bytes, 34, ByteOrder.LITTLE, this.getUint16());
        EncodeUtils.writeUInt32(bytes, 36, ByteOrder.LITTLE, this.getUint32());
        EncodeUtils.writeBytes(bytes, 40, this.getBytes());

        return bytes;
    }
}
