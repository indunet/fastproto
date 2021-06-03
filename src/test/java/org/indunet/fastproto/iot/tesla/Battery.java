package org.indunet.fastproto.iot.tesla;

import lombok.*;
import org.indunet.fastproto.annotation.type.BooleanType;
import org.indunet.fastproto.annotation.type.FloatType;
import org.indunet.fastproto.annotation.type.IntegerType;
import org.indunet.fastproto.annotation.type.ShortType;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Battery {
    @ShortType(22)
    short capacity;
    @BooleanType(value = 24, bitOffset = 0)
    boolean locked;
    @IntegerType(26)
    int voltage;
    @FloatType(30)
    float temperature;
}
