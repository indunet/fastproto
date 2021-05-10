package org.indunet.fastproto.domain;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.type.LongType;

@Endian(EndianPolicy.Little)
// @Datagram("Tesla")
public class Tesla {
    public String vehicleCode;

    @LongType(byteOffset = 0)
    Long a;


    public Motor motor = new Motor();
    public Battery battery = new Battery();
    public AirConditioner airConditioner = new AirConditioner();
    public Seat seat = new Seat();
}
