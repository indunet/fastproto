package org.indunet.fastproto.entity;

import org.indunet.fastproto.annotation.type.BooleanType;

public class Seat {
    @BooleanType(byteOffset = 6, bitOffset = 2)
    public boolean heaterState;
}
