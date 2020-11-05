package org.indunet.fastproto;

import org.indunet.fastproto.annotation.Datagram;
import org.indunet.fastproto.annotation.EndianMode;
import org.indunet.fastproto.annotation.ObjectType;
import org.indunet.fastproto.annotation.StringType;

@EndianMode(Endian.Little)
@Datagram("vehicle")
public class Vehicle {
    @StringType(byteOffset = 10, length = 8)
    String vehicleCode;

    @ObjectType
    Motor motor = new Motor();
}
