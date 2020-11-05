package org.indunet.fastproto;

public enum Endian {
    Big(0x01, "Big"),
    Little(0x02, "Little");

    int code;
    String name;

    Endian(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}
