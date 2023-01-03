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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

/**
 * Collection object.
 *
 * @author Deng Ran
 * @since 3.6.2
 */
@Data
public class CollectionObject {
    @BinaryType(offset = 0, length = 16)
    List<Byte> bytes;
    @Int16ArrayType(offset = 16, length = 16)
    Set<Short> shorts;
    @Int8ArrayType(offset = 48, length = 16)
    Deque<Integer> int8s;
    @Int16ArrayType(offset = 64, length = 16)
    ArrayList<Integer> int16s;
    @Int32ArrayType(offset = 96, length = 16)
    HashSet<Integer> int32s;
    @Int64ArrayType(offset = 160, length = 16)
    ArrayDeque<Long> int64s;
    @UInt8ArrayType(offset = 288, length = 16)
    LinkedList<Integer> uint8s;
    @UInt16ArrayType(offset = 304, length = 16)
    List<Integer> uint16s;
    @UInt32ArrayType(offset = 336, length = 16)
    Collection<Long> uint32s;
    @UInt64ArrayType(offset = 400, length = 16)
    Collection<BigInteger> uint64s;
    @FloatArrayType(offset = 528, length = 16)
    List<Float> floats;
    @DoubleArrayType(offset = 592, length = 16)
    List<Double> doubles;
    @BoolArrayType(byteOffset = 720, bitOffset = 3, length = 5, bitOrder = BitOrder.MSB_0)
    List<Boolean> bools;

    public CollectionObject() {
        val random = new Random();

        this.bytes = IntStream.range(0, 16)
                .mapToObj(__ -> (byte) random.nextInt(Int8Type.MAX_VALUE))
                .collect(Collectors.toList());
        this.shorts = IntStream.range(0, 16)
                .mapToObj(__ -> (short) random.nextInt(Int16Type.MAX_VALUE))
                .collect(Collectors.toSet());
        this.int8s = IntStream.range(0, 16)
                .mapToObj(__ -> random.nextInt(Int8Type.MAX_VALUE))
                .collect(Collectors.toCollection(ArrayDeque::new));
        this.int16s = IntStream.range(0, 16)
                .mapToObj(__ -> random.nextInt(Int16Type.MAX_VALUE))
                .collect(Collectors.toCollection(ArrayList::new));
        this.int32s = IntStream.range(0, 16)
                .mapToObj(__ -> random.nextInt())
                .collect(Collectors.toCollection(HashSet::new));
        this.int64s = IntStream.range(0, 16)
                .mapToObj(__ -> random.nextLong())
                .collect(Collectors.toCollection(ArrayDeque::new));
        this.uint8s = IntStream.range(0, 16)
                .mapToObj(__ -> abs(random.nextInt(UInt8Type.MAX_VALUE)))
                .collect(Collectors.toCollection(LinkedList::new));
        this.uint16s = IntStream.range(0, 16)
                .mapToObj(__ -> abs(random.nextInt(UInt16Type.MAX_VALUE)))
                .collect(Collectors.toList());
        this.uint32s = IntStream.range(0, 16)
                .mapToObj(__ -> (long) abs(random.nextInt()))
                .collect(Collectors.toList());
        this.uint64s = IntStream.range(0 ,16)
                .mapToObj(__ -> BigInteger.valueOf(abs(random.nextLong())))
                .collect(Collectors.toList());
        this.floats = IntStream.range(0, 16)
                .mapToObj(i -> random.nextFloat())
                .collect(Collectors.toList());
        this.doubles = IntStream.range(0, 16)
                .mapToObj(__ -> random.nextDouble())
                .collect(Collectors.toList());

        this.bools = new ArrayList<Boolean>();
        this.bools.add(false);
        this.bools.add(true);
        this.bools.add(false);
        this.bools.add(true);
        this.bools.add(false);
    }

    public byte[] toBytes() throws IOException {
        val stream = new ByteArrayOutputStream();

        stream.write(BinaryUtils.valueOf(this.getBytes().stream()
                .toArray(Byte[]::new)));
        stream.write(BinaryUtils.valueOf(this.getShorts().stream()
                .toArray(Short[]::new), ByteOrder.LITTLE));
        stream.write(BinaryUtils.int8Of(this.getInt8s().stream()
                        .mapToInt(Integer::intValue)
                        .toArray()));
        stream.write(BinaryUtils.int16Of(this.getInt16s().stream()
                .mapToInt(Integer::intValue)
                .toArray(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.int32Of(this.getInt32s().stream()
                .mapToInt(Integer::intValue)
                .toArray(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.valueOf(this.getInt64s().stream()
                .mapToLong(Long::longValue)
                .toArray(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.uint8Of(this.getUint8s().stream()
                .mapToInt(Integer::intValue)
                .toArray()));
        stream.write(BinaryUtils.uint16Of(this.getUint16s().stream()
                .mapToInt(Integer::intValue)
                .toArray(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.uint32Of(this.getUint32s().stream()
                .mapToLong(Long::longValue)
                .toArray(), ByteOrder.LITTLE));
        stream.write(BinaryUtils.uint64Of(this.getUint64s().stream()
                .toArray(BigInteger[]::new), ByteOrder.LITTLE));
        stream.write(BinaryUtils.valueOf(this.getFloats().stream()
                .toArray(Float[]::new), ByteOrder.LITTLE));
        stream.write(BinaryUtils.valueOf(this.getDoubles().stream()
                .mapToDouble(Double::doubleValue)
                .toArray(), ByteOrder.LITTLE));
        stream.write(new byte[] {(byte) 0b0000_1010});

        stream.flush();

        return stream.toByteArray();
    }
}
