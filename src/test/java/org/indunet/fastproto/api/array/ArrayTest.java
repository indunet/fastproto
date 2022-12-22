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
import org.indunet.fastproto.FastProto;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
        val bytes = expected.toBytes();

        assertEquals(expected, FastProto.parse(bytes, ArrayObject.class));
    }

    @Test
    public void testToBytes() throws IOException {
        val object = new ArrayObject();
        val expected = object.toBytes();

        assertArrayEquals(expected, FastProto.toBytes(object, expected.length));
    }

    @Test
    public void testWrapperParse() throws IOException {
        val expected = new WrapperArrayObject();
        val bytes = expected.toBytes();

        assertEquals(expected, FastProto.parse(bytes, WrapperArrayObject.class));
    }

    @Test
    public void testWrapperToBytes() throws IOException {
        val object = new WrapperArrayObject();
        val expected = object.toBytes();

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
