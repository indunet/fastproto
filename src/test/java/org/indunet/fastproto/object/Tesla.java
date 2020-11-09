package org.indunet.fastproto.object;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.Datagram;
import org.indunet.fastproto.annotation.EndianMode;
import org.indunet.fastproto.annotation.ObjectType;

@EndianMode(Endian.Little)
@Datagram("Tesla")
public class Tesla {
    String vehicleCode;

    @ObjectType
    Motor motor = new Motor();
}
