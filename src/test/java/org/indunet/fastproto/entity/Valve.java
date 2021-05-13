package org.indunet.fastproto.entity;

import org.indunet.fastproto.annotation.type.BooleanType;

public class Valve {
    @BooleanType(byteOffset = 4, bitOffset = 0)
    boolean state;
}
