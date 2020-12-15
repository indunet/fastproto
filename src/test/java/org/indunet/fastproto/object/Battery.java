package org.indunet.fastproto.object;

import org.indunet.fastproto.annotation.ByteType;
import org.indunet.fastproto.annotation.IntegerType;
import org.indunet.fastproto.annotation.ShortType;

public class Battery {
    @ShortType(byteOffset = 8)
    short current;
    @ByteType(byteOffset = 9)
    byte voltage;
    @IntegerType(byteOffset = 4)
    int temperature;
}
