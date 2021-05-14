package org.indunet.fastproto.entity;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.type.LongType;

@Endian(EndianPolicy.Little)
public class Tesla {
    public String vehicleCode;

    @LongType(byteOffset = 0)
    Long a;


    public Motor motor;
    public Battery battery;
    public AirConditioner airConditioner;
    public Seat seat;
}
