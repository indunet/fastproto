package org.indunet.fastproto.object;

import org.indunet.fastproto.annotation.BooleanType;

public class Valve {
    @BooleanType(byteOffset = 4, bitOffset = 0)
    boolean state;
}
