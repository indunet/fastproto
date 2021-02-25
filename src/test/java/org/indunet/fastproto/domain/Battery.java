package org.indunet.fastproto.domain;

import org.indunet.fastproto.annotation.ByteType;
import org.indunet.fastproto.annotation.IntegerType;
import org.indunet.fastproto.annotation.ShortType;

public class Battery {
    @ShortType(byteOffset = 8)
    public short current;
    @ByteType(byteOffset = 9)
    public byte voltage;
    @IntegerType(byteOffset = 4)
    public int temperature;
}
