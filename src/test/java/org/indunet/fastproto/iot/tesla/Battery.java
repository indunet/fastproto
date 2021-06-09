package org.indunet.fastproto.iot.tesla;

import lombok.*;
import org.indunet.fastproto.annotation.type.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Battery {
    @ShortType(22)
    short capacity;

    @AutoType(value = 24, bitOffset = 0)
    boolean locked;

    @IntegerType(26)
    int voltage;

    @FloatType(30)
    float temperature;
}
