package org.indunet.fastproto.object;

import org.indunet.fastproto.annotation.BooleanType;

public class Seat {
    @BooleanType(byteOffset = 6, bitOffset = 2)
    boolean heaterState;
}
