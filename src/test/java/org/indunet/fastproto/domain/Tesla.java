package org.indunet.fastproto.domain;

import lombok.Data;
import lombok.ToString;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.type.LongType;

@Data
@ToString
@Endian(EndianPolicy.LITTLE)
public class Tesla {
    public String vehicleCode;
    public Motor motor;
    public Battery battery;
    public AirConditioner airConditioner;
    public Seat seat;
    @LongType(value = 0)
    long mileage;
}
