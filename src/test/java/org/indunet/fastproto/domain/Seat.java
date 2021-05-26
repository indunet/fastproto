package org.indunet.fastproto.domain;

import lombok.ToString;
import org.indunet.fastproto.annotation.type.BooleanType;
import org.indunet.fastproto.annotation.type.TimestampType;

@ToString
public class Seat {
    // @TimestampType(@BooleanType(byteOffset = 10, bitOffset = 10))
    @BooleanType(byteOffset = 6, bitOffset = 2)
    public boolean heaterState;
}
