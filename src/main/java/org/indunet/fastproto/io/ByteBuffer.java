/**
 * Copyright 2019-2023 indunet.org
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

package org.indunet.fastproto.io;

import org.indunet.fastproto.exception.CodecException;

import java.util.Arrays;

/**
 * ByteBuffer is a dynamic byte array that can automatically expand based on the writing offset.
 * It provides a flexible way to handle byte data, especially when dealing with network or file data.
 * ByteBuffer supports both forward and reverse addressing, making it more convenient when dealing with complex data structures.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public final class ByteBuffer {
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    byte[] bytes;
    int length;
    boolean fixed;

    public ByteBuffer() {
        this.fixed = false;
        this.bytes = new byte[256];
        this.length = 0;
    }

    public ByteBuffer(int length) {
        this(new byte[length]);
    }

    public ByteBuffer(byte[] bytes) {
        this.fixed = true;
        this.bytes = bytes;
        this.length = bytes.length;
    }

    public void andEq(int offset, byte value) {
        byte tmp = 0;

        if (offset < this.length) {
            tmp = (byte) (this.get(offset) & value);
        }

        this.set(offset, tmp);
    }

    public void orEq(int offset, byte value) {
        byte tmp = value;

        if (offset < this.length) {
            tmp = (byte) (this.get(offset) | value);
        }

        this.set(offset, tmp);
    }

    public void set(int offset, byte value) {
        int o = this.reverse(offset);

        this.grow(o);

        bytes[o] = value;
    }

    private void grow(int index) {
        if (index >= length) {
            length = index + 1;
        } else if (length == bytes.length && !this.fixed) {
            int newLength = Math.min(bytes.length * 2, MAX_ARRAY_SIZE);
            bytes = Arrays.copyOf(bytes, newLength);

            this.grow(index);
        }
    }

    public byte get(int offset) {
        int o = this.reverse(offset);

        if (o >= this.length) {
            throw new IndexOutOfBoundsException();
        }

        return bytes[o];
    }

    public int reverse(int offset) {
        if (offset >= 0) {
            return offset;
        } else if (this.fixed) {    // offset < 0 on fixed-length buffer.
            int o = this.bytes.length + offset;

            if (o >= 0) {
                return o;
            } else {
                throw new IllegalArgumentException(String.format("Illegal offset %d", o));
            }
        } else {
            throw new CodecException("Reverse addressing is only available with fixed length");
        }
    }

    public int reverse(int offset, int length) {
        if (length > 0) {
            return length;
        } else if (this.fixed && length < 0) {
            int o = this.reverse(offset);
            int l = this.bytes.length + length - o + 1;

            if (l > 0) {
                return l;
            } else {
                throw new IllegalArgumentException(String.format("Illegal length %d", l));
            }
        } else {
            throw new CodecException("Reverse addressing is only available with fixed length");
        }
    }

    public int size() {
        return length;
    }

    public boolean isFixed() {
        return this.fixed;
    }

    public byte[] toBytes() {
        return Arrays.copyOf(bytes, length);
    }
}