package org.indunet.fastproto.domain;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.EndianMode;
import org.indunet.fastproto.annotation.ObjectType;

@EndianMode(Endian.Little)
// @Datagram("Tesla")
public class Tesla {
    public String vehicleCode;

    @ObjectType
    public Motor motor = new Motor();
    @ObjectType
    public Battery battery = new Battery();
    @ObjectType
    public AirConditioner airConditioner = new AirConditioner();
    @ObjectType
    public Seat seat = new Seat();
}
