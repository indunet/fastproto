package org.indunet.fastproto.entity;

import lombok.ToString;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.type.LongType;

@ToString
@Endian(EndianPolicy.LITTLE)
public class Tesla {
    public String vehicleCode;
    public Motor motor;
    public Battery battery;
    public AirConditioner airConditioner;
    public Seat seat;
    @LongType(byteOffset = 0)
    Long mileage;
}
