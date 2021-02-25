package org.indunet.fastproto.domain;

import org.indunet.fastproto.annotation.FloatType;

public class Compressor {
    @FloatType(byteOffset = 6)
    float pressure;
}
