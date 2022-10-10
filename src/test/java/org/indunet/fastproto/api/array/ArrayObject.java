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
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.util.BinaryUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

/**
 * Array object.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
@Data
public class ArrayObject {
    @BinaryType(offset = 0, length = 16)
    byte[] bytes = new byte[16];
    @Int16ArrayType(offset = 16, length = 16)
    short[] shorts = new short[16];
    @Int8ArrayType(offset = 48, length = 16)
    int[] int8s;
    @Int16ArrayType(offset = 64, length = 16)
    int[] int16s;
    @Int32ArrayType(offset = 96, length = 16)
    int[] int32s;
    @Int64ArrayType(offset = 160, length = 16)
    long[] int64s;
    @UInt8ArrayType(offset = 288, length = 16)
    int[] uint8s;
    @UInt16ArrayType(offset = 304, length = 16)
    int[] uint16s;
    @UInt32ArrayType(offset = 336, length = 16)
    long[] uint32s;
    @UInt64ArrayType(offset = 400, length = 16)
    BigInteger[] uint64s;
    @FloatArrayType(offset = 528, length = 16)
    float[] floats = new float[16];
    @DoubleArrayType(offset = 592, length = 16)
    double[] doubles;

    public ArrayObject() {
        val random = new Random();

        IntStream.range(0, 16)
                .forEach(i -> bytes[i] = (byte) random.nextInt(Int8Type.MAX_VALUE));
        IntStream.range(0, 16)
                .forEach(i -> shorts[i] = (short) random.nextInt(Int16Type.MAX_VALUE));
        this.int8s = IntStream.range(0, 16)
                .map(__ -> random.nextInt(Int8Type.MAX_VALUE))
                .toArray();
        this.int16s = IntStream.range(0, 16)
                .map(__ -> random.nextInt(Int16Type.MAX_VALUE))
                .toArray();
        this.int32s = IntStream.range(0, 16)
                .map(__ -> random.nextInt())
                .toArray();
        this.int64s = IntStream.range(0, 16)
                .mapToLong(__ -> random.nextLong())
                .toArray();
        this.uint8s = IntStream.range(0, 16)
                .map(__ -> abs(random.nextInt(UInt8Type.MAX_VALUE)))
                .toArray();
        this.uint16s = IntStream.range(0, 16)
                .map(__ -> abs(random.nextInt(UInt16Type.MAX_VALUE)))
                .toArray();
        this.uint32s = IntStream.range(0, 16)
                .mapToLong(__ -> (long) abs(random.nextInt()))
                .toArray();
        this.uint64s = IntStream.range(0 ,16)
                .mapToObj(__ -> BigInteger.valueOf(abs(random.nextLong())))
                .toArray(BigInteger[]::new);
        IntStream.range(0, 16)
                .forEach(i -> this.floats[i] = random.nextFloat());
        this.doubles = IntStream.range(0, 16)
                .mapToDouble(__ -> random.nextDouble())
                .toArray();
    }

    public byte[] toBytes() throws IOException {
        val stream = new ByteArrayOutputStream();

        stream.write(this.getBytes());
        stream.write(BinaryUtils.valueOf(this.getShorts(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.int8Of(this.getInt8s()));
        stream.write(BinaryUtils.int16Of(this.getInt16s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.int32Of(this.getInt32s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(this.getInt64s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint8Of(this.getUint8s()));
        stream.write(BinaryUtils.uint16Of(this.getUint16s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint32Of(this.getUint32s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint64Of(this.getUint64s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(this.getFloats(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(this.getDoubles(), EndianPolicy.LITTLE));
        stream.flush();

        return stream.toByteArray();
    }
}
