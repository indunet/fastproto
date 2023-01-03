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

package org.indunet.fastproto.api.array;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.util.BinaryUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

/**
 * Array object.
 *
 * @author Deng Ran
 * @since 3.6.1
 */
@Data
public class WrapperArrayObject {
    @BinaryType(offset = 0, length = 16)
    Byte[] bytes;
    @Int16ArrayType(offset = 16, length = 16)
    Short[] shorts;
    @Int8ArrayType(offset = 48, length = 16)
    Integer[] int8s;
    @Int16ArrayType(offset = 64, length = 16)
    Integer[] int16s;
    @Int32ArrayType(offset = 96, length = 16)
    Integer[] int32s;
    @Int64ArrayType(offset = 160, length = 16)
    Long[] int64s;
    @UInt8ArrayType(offset = 288, length = 16)
    Integer[] uint8s;
    @UInt16ArrayType(offset = 304, length = 16)
    Integer[] uint16s;
    @UInt32ArrayType(offset = 336, length = 16)
    Long[] uint32s;
    @UInt64ArrayType(offset = 400, length = 16)
    BigInteger[] uint64s;
    @FloatArrayType(offset = 528, length = 16)
    Float[] floats = new Float[16];
    @DoubleArrayType(offset = 592, length = 16)
    Double[] doubles;
    @BoolArrayType(byteOffset = 720, bitOffset = 3, length = 5, bitOrder = BitOrder.MSB_0)
    Boolean[] bools;

    public WrapperArrayObject() {
        val random = new Random();

        this.bytes = IntStream.range(0, 16)
                .mapToObj(__ -> (byte) random.nextInt(Int8Type.MAX_VALUE))
                .toArray(Byte[]::new);
        this.shorts = IntStream.range(0, 16)
                .mapToObj(__ -> (short) random.nextInt(Int16Type.MAX_VALUE))
                .toArray(Short[]::new);
        this.int8s = IntStream.range(0, 16)
                .mapToObj(__ -> random.nextInt(Int8Type.MAX_VALUE))
                .toArray(Integer[]::new);
        this.int16s = IntStream.range(0, 16)
                .mapToObj(__ -> random.nextInt(Int16Type.MAX_VALUE))
                .toArray(Integer[]::new);
        this.int32s = IntStream.range(0, 16)
                .mapToObj(__ -> random.nextInt())
                .toArray(Integer[]::new);
        this.int64s = IntStream.range(0, 16)
                .mapToObj(__ -> random.nextLong())
                .toArray(Long[]::new);
        this.uint8s = IntStream.range(0, 16)
                .mapToObj(__ -> abs(random.nextInt(UInt8Type.MAX_VALUE)))
                .toArray(Integer[]::new);
        this.uint16s = IntStream.range(0, 16)
                .mapToObj(__ -> abs(random.nextInt(UInt16Type.MAX_VALUE)))
                .toArray(Integer[]::new);
        this.uint32s = IntStream.range(0, 16)
                .mapToObj(__ -> (long) abs(random.nextInt()))
                .toArray(Long[]::new);
        this.uint64s = IntStream.range(0 ,16)
                .mapToObj(__ -> BigInteger.valueOf(abs(random.nextLong())))
                .toArray(BigInteger[]::new);
       this.floats = IntStream.range(0, 16)
                .mapToObj(i -> random.nextFloat())
               .toArray(Float[]::new);
        this.doubles = IntStream.range(0, 16)
                .mapToObj(__ -> random.nextDouble())
                .toArray(Double[]::new);
        this.bools = new Boolean[] {false, true, false, true, false};
    }

    public byte[] toBytes() throws IOException {
        val stream = new ByteArrayOutputStream();

        stream.write(BinaryUtils.valueOf(this.getBytes()));
        stream.write(BinaryUtils.valueOf(this.getShorts(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.int8Of(Arrays.stream(this.getInt8s())
                .mapToInt(Integer::intValue)
                .toArray()));
        stream.write(BinaryUtils.int16Of(Arrays.stream(this.getInt16s())
                .mapToInt(Integer::intValue)
                .toArray(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.int32Of(Arrays.stream(this.getInt32s())
                .mapToInt(Integer::intValue)
                .toArray(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.valueOf(Arrays.stream(this.getInt64s())
                .mapToLong(Long::longValue)
                .toArray(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.uint8Of(Arrays.stream(this.getUint8s())
                .mapToInt(Integer::intValue)
                .toArray()));
        stream.write(BinaryUtils.uint16Of(Arrays.stream(this.getUint16s())
                .mapToInt(Integer::intValue)
                .toArray(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.uint32Of(Arrays.stream(this.getUint32s())
                .mapToLong(Long::longValue)
                .toArray(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.uint64Of(this.getUint64s(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.valueOf(this.getFloats(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.valueOf(Arrays.stream(this.getDoubles())
                .mapToDouble(Double::doubleValue)
                .toArray(), ByteOrder.LITTLE));
        stream.write(new byte[] {(byte) 0b0000_1010});

        stream.flush();

        return stream.toByteArray();
    }
}
