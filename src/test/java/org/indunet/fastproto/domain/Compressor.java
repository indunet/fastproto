package org.indunet.fastproto.domain;

import org.indunet.fastproto.annotation.type.FloatType;

public class Compressor {
    @FloatType(byteOffset = 6)
    float pressure;
}
