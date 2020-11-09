package org.indunet.fastproto.object;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.Datagram;
import org.indunet.fastproto.annotation.EndianMode;
import org.indunet.fastproto.annotation.ObjectType;
import org.indunet.fastproto.annotation.StringType;
import org.indunet.fastproto.object.Motor;

@EndianMode(Endian.Little)
@Datagram("Tesla")
public class Tesla {
    @StringType(byteOffset = 10, length = 8)
    String vehicleCode;

    @ObjectType
    Motor motor = new Motor();
}
