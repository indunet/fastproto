package org.indunet.fastproto.entity;

import lombok.ToString;
import org.indunet.fastproto.annotation.type.BooleanType;

@ToString
public class Seat {
    @BooleanType(byteOffset = 6, bitOffset = 2)
    public boolean heaterState;
}
