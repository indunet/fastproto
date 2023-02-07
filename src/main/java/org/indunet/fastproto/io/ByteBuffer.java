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
 * Dynamic byte array, which can automatically expand according to writing offset.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public final class ByteBuffer {
    byte[] bytes;
    int length;
    int readIndex;
    int writeIndex;
    boolean fixed;

    public ByteBuffer() {
        this.fixed = false;
        this.bytes = new byte[256];

        this.length = 0;
        this.readIndex = 0;
        this.writeIndex = 0;
    }

    public ByteBuffer(int length) {
        this(new byte[length]);
    }

    public ByteBuffer(byte[] bytes) {
        this.fixed = true;
        this.bytes = bytes;

        this.length = bytes.length;
        this.writeIndex = 0;
        this.readIndex = 0;
    }

    public int getWriteIndex() {
        return this.writeIndex;
    }

    public int getReadIndex() {
        return this.readIndex;
    }

    public void resetReadIndex() {
        this.readIndex = 0;
    }

    public void nextReadIndex() {
        this.readIndex ++;
    }

    public void resetWriteIndex() {
        this.writeIndex = 0;
    }

    public void nextWriteIndex() {
        this.expand();
        this.writeIndex ++;
    }

    public void write(byte value) {
        this.expand();

        bytes[writeIndex ++] = value;
    }

    // return value at current index and read index doesn't move.
    public byte read() {
        return this.get(this.readIndex);
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
        this.writeIndex = this.reverse(offset);
        this.expand();

        bytes[this.writeIndex ++] = value;
    }

    private void expand() {
        if (this.writeIndex >= length) {
            length = this.writeIndex + 1;
        }

        if (length == bytes.length && !this.fixed) {
            bytes = Arrays.copyOf(bytes, bytes.length * 2);
        }
    }

    public byte get(int offset) {
        this.readIndex = this.reverse(offset);

        if (this.readIndex >= this.length) {
            throw new IndexOutOfBoundsException();
        }

        return bytes[this.readIndex ++];
    }


    public int reverse(int offset) {
        if (offset >= 0) {
            return offset;
        } else if (this.fixed) {    // offset less than 0 and non-fixed.
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