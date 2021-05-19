package org.indunet.fastproto.domain;

import org.indunet.fastproto.annotation.type.BooleanType;

public class Valve {
    @BooleanType(byteOffset = 4, bitOffset = 0)
    boolean state;
}
