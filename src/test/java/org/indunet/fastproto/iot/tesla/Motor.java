package org.indunet.fastproto.iot.tesla;

import lombok.*;
import org.indunet.fastproto.annotation.type.AutoType;
import org.indunet.fastproto.annotation.type.FloatType;
import org.indunet.fastproto.annotation.type.IntegerType;
import org.indunet.fastproto.annotation.type.ShortType;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Motor {
    @AutoType(34)
    short voltage;

    @AutoType(36)
    int current;

    @AutoType(40)
    float temperature;
}
