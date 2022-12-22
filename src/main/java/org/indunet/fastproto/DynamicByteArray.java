package org.indunet.fastproto;

import java.util.Arrays;

public class DynamicByteArray {
    protected byte[] bytes;
    protected int size;

    public DynamicByteArray() {
        this.bytes = new byte[16];
        this.size = 0;
    }
    public DynamicByteArray(int capacity) {
        this.bytes = new byte[capacity];
        this.size = capacity;
    }

    public void write(int b) {
        if (size == bytes.length) {
            bytes = Arrays.copyOf(bytes, bytes.length * 2);
        }
        bytes[size++] = (byte) b;
    }

    public void write(int b, int offset) {
        if (offset >= size) {
            size = offset + 1;
        }
        if (size == bytes.length) {
            bytes = Arrays.copyOf(bytes, bytes.length * 2);
        }
        bytes[offset] = (byte) b;
    }

    public int read(int offset) {
        if (offset >= size) {
            throw new IndexOutOfBoundsException();
        }
        return bytes[offset] & 0xff;
    }

    public int size() {
        return size;
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, size);
    }
}
