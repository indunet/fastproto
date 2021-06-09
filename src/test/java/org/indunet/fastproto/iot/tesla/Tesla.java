package org.indunet.fastproto.iot.tesla;

import lombok.*;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.type.BooleanType;
import org.indunet.fastproto.annotation.type.FloatType;
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.annotation.type.TimestampType;

import java.sql.Timestamp;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Endian(EndianPolicy.LITTLE)
public class Tesla {
    @LongType(0)
    Long id;

    @TimestampType(8)
    Timestamp time;

    @FloatType(16)
    float speed;

    @BooleanType(value = 20, bitOffset = 0)
    boolean active;

    Battery battery;
    Motor motor;
}
