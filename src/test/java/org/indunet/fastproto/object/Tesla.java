package org.indunet.fastproto.object;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.EndianMode;
import org.indunet.fastproto.annotation.ObjectType;

@EndianMode(Endian.Little)
// @Datagram("Tesla")
public class Tesla {
    String vehicleCode;

    @ObjectType
    Motor motor = new Motor();
    @ObjectType
    Battery battery = new Battery();
    @ObjectType
    AirConditioner airConditioner = new AirConditioner();
    @ObjectType
    Seat seat = new Seat();
}
