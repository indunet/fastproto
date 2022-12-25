package org.indunet.fastproto;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.exception.CodecException;

import java.util.Arrays;

public class DynamicByteBuffer {
    protected byte[] bytes = new byte[32];
    protected int size = 0;
    protected int index = 0;
    protected boolean fixed = false;

    public DynamicByteBuffer() {
        this.bytes = new byte[16];
        this.size = 0;
    }

    public void setCapacity(int capacity) {
        this.fixed = true;
        this.bytes = new byte[capacity];
        this.size = capacity;
    }

    public void append(byte value) {
        if (size == bytes.length && !this.fixed) {
            bytes = Arrays.copyOf(bytes, bytes.length * 2);
        }

        bytes[size ++] = value;
    }

    public void andEq(int offset, byte value) {
        byte tmp = 0;

        if (offset < this.size) {
            tmp = (byte) (this.get(offset) & value);
        }

        this.set(offset, tmp);
    }

    public void orEq(int offset, byte value) {
        byte tmp = value;

        if (offset < this.size) {
            tmp = (byte) (this.get(offset) | value);
        }

        this.set(offset, tmp);
    }

    public void set(int offset, byte value) {
        int o = this.reverse(offset);

        if (o >= size) {
            size = o + 1;
        }

        if (size == bytes.length && !this.fixed) {
            bytes = Arrays.copyOf(bytes, bytes.length * 2);
        }

        bytes[o] = value;
    }

    public byte get(int offset) {
        int o = this.reverse(offset);

        if (o >= size) {
            throw new IndexOutOfBoundsException();
        }

        return bytes[o];
    }

    protected int reverse(int offset) {
        if (offset >= 0) {
            return offset;
        } else if (this.fixed && offset < 0) {
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

    public int size() {
        return size;
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, size);
    }
}