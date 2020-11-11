package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;

import java.lang.annotation.Annotation;

public interface Decoder<T> {
    default boolean validate(DecodeContext context) {
        return true;
    };

    T decode(final byte[] datagram, Endian endian, Annotation dataTypeAnnotation);
}
