package org.indunet.fastproto.domain;

import org.indunet.fastproto.annotation.BinaryType;
import org.indunet.fastproto.annotation.Int32Type;
import org.indunet.fastproto.annotation.Int64Type;
import org.indunet.fastproto.annotation.StringType;

public class MyPojo {
    @Int32Type(offset = 0)
    public int id;

    @Int64Type(offset = 4)
    public long timestamp;

    @StringType(offset = 12, length = 16)
    public String name;

    @BinaryType(offset = 28, length = 32)
    public byte[] payload;

    public MyPojo() {}

    public MyPojo(int id, long timestamp, String name, byte[] payload) {
        this.id = id;
        this.timestamp = timestamp;
        this.name = name;
        this.payload = payload;
    }
} 