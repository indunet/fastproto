package org.indunet.fastproto.object;

import org.indunet.fastproto.annotation.ObjectType;

public class AirConditioner {
    @ObjectType
    Compressor compressor = new Compressor();

    @ObjectType
    Valve valve = new Valve();
}
