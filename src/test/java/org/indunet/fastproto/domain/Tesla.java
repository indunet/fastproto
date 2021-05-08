package org.indunet.fastproto.domain;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Endian;

@Endian(EndianPolicy.Little)
// @Datagram("Tesla")
public class Tesla {
    public String vehicleCode;

    public Motor motor = new Motor();
    public Battery battery = new Battery();
    public AirConditioner airConditioner = new AirConditioner();
    public Seat seat = new Seat();
}
