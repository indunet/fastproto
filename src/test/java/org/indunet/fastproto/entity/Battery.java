package org.indunet.fastproto.entity;

import lombok.ToString;
import org.indunet.fastproto.annotation.type.ByteType;
import org.indunet.fastproto.annotation.type.IntegerType;
import org.indunet.fastproto.annotation.type.ShortType;

@ToString
public class Battery {
    @ShortType(byteOffset = 8)
    public short current;
    @ByteType(byteOffset = 9)
    public byte voltage;
    @IntegerType(byteOffset = 4)
    public int temperature;
}
