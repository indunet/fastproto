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

import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.util.BinaryUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of array type.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
public class ArrayTest {
    @Test
    public void testParse() throws IOException {
        val expected = new ArrayObject();
        val stream = new ByteArrayOutputStream();

        stream.write(expected.getBytes());
        stream.write(BinaryUtils.valueOf(expected.getShorts(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.int8Of(expected.getInt8s()));
        stream.write(BinaryUtils.int16Of(expected.getInt16s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.int32Of(expected.getInt32s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(expected.getInt64s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint8Of(expected.getUint8s()));
        stream.write(BinaryUtils.uint16Of(expected.getUint16s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint32Of(expected.getUint32s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint64Of(expected.getUint64s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(expected.getFloats(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(expected.getDoubles(), EndianPolicy.LITTLE));

        stream.flush();
        val bytes = stream.toByteArray();

        assertEquals(expected, FastProto.parse(bytes, ArrayObject.class));
    }

    @Test
    public void testToBytes() throws IOException {
        val object = new ArrayObject();
        val stream = new ByteArrayOutputStream();

        stream.write(object.getBytes());
        stream.write(BinaryUtils.valueOf(object.getShorts(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.int8Of(object.getInt8s()));
        stream.write(BinaryUtils.int16Of(object.getInt16s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.int32Of(object.getInt32s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(object.getInt64s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint8Of(object.getUint8s()));
        stream.write(BinaryUtils.uint16Of(object.getUint16s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint32Of(object.getUint32s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint64Of(object.getUint64s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(object.getFloats(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(object.getDoubles(), EndianPolicy.LITTLE));

        stream.flush();
        val expected = stream.toByteArray();

        assertArrayEquals(expected, FastProto.toBytes(object, expected.length));
    }

    @Test
    public void testWrapperParse() throws IOException {
        val expected = new WrapperArrayObject();
        val stream = new ByteArrayOutputStream();

        stream.write(BinaryUtils.valueOf(expected.getBytes()));
        stream.write(BinaryUtils.valueOf(expected.getShorts(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.int8Of(Arrays.stream(expected.getInt8s())
                .mapToInt(Integer::intValue)
                .toArray()));
        stream.write(BinaryUtils.int16Of(Arrays.stream(expected.getInt16s())
                .mapToInt(Integer::intValue)
                .toArray(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.int32Of(Arrays.stream(expected.getInt32s())
                .mapToInt(Integer::intValue)
                .toArray(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(Arrays.stream(expected.getInt64s())
                .mapToLong(Long::longValue)
                .toArray(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint8Of(Arrays.stream(expected.getUint8s())
                .mapToInt(Integer::intValue)
                .toArray()));
        stream.write(BinaryUtils.uint16Of(Arrays.stream(expected.getUint16s())
                .mapToInt(Integer::intValue)
                .toArray(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint32Of(Arrays.stream(expected.getUint32s())
                .mapToLong(Long::longValue)
                .toArray(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint64Of(expected.getUint64s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(expected.getFloats(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(Arrays.stream(expected.getDoubles())
                .mapToDouble(Double::doubleValue)
                .toArray(), EndianPolicy.LITTLE));

        stream.flush();
        val bytes = stream.toByteArray();

        assertEquals(expected, FastProto.parse(bytes, WrapperArrayObject.class));
    }

    @Test
    public void testWrapperToBytes() throws IOException {
        val object = new WrapperArrayObject();
        val stream = new ByteArrayOutputStream();

        stream.write(BinaryUtils.valueOf(object.getBytes()));
        stream.write(BinaryUtils.valueOf(object.getShorts(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.int8Of(Arrays.stream(object.getInt8s())
                .mapToInt(Integer::intValue)
                .toArray()));
        stream.write(BinaryUtils.int16Of(Arrays.stream(object.getInt16s())
                .mapToInt(Integer::intValue)
                .toArray(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.int32Of(Arrays.stream(object.getInt32s())
                .mapToInt(Integer::intValue)
                .toArray(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(Arrays.stream(object.getInt64s())
                .mapToLong(Long::longValue)
                .toArray(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint8Of(Arrays.stream(object.getUint8s())
                .mapToInt(Integer::intValue)
                .toArray()));
        stream.write(BinaryUtils.uint16Of(Arrays.stream(object.getUint16s())
                .mapToInt(Integer::intValue)
                .toArray(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint32Of(Arrays.stream(object.getUint32s())
                .mapToLong(Long::longValue)
                .toArray(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.uint64Of(object.getUint64s(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(object.getFloats(), EndianPolicy.LITTLE));
        stream.write(BinaryUtils.valueOf(Arrays.stream(object.getDoubles())
                .mapToDouble(Double::doubleValue)
                .toArray(), EndianPolicy.LITTLE));

        stream.flush();
        val expected = stream.toByteArray();

        assertArrayEquals(expected, FastProto.toBytes(object, expected.length));
    }

    @Test
    public void testCollectionParse() throws IOException {
        val expected = new CollectionObject();
        val bytes = expected.toBytes();

        assertEquals(expected.toString(), FastProto.parse(bytes, CollectionObject.class).toString());
    }

    @Test
    public void testCollectionToBytes() throws IOException {
        val object = new CollectionObject();
        val expected = object.toBytes();

        assertArrayEquals(expected, FastProto.toBytes(object, expected.length));
    }
}
