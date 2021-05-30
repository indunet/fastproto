package org.indunet.fastproto.iot.tesla;

import lombok.*;
import org.indunet.fastproto.annotation.type.FloatType;
import org.indunet.fastproto.annotation.type.IntegerType;
import org.indunet.fastproto.annotation.type.ShortType;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Motor {
    @ShortType(34)
    short voltage;
    @IntegerType(36)
    int current;
    @FloatType(40)
    float temperature;
}
