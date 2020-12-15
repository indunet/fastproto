package org.indunet.fastproto.object;

import org.indunet.fastproto.annotation.FloatType;
import org.indunet.fastproto.annotation.ObjectType;

public class Compressor {
    @FloatType(byteOffset = 6)
    float pressure;
}
