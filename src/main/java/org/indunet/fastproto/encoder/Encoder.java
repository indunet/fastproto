package org.indunet.fastproto.encoder;

import org.indunet.fastproto.Endian;

import java.lang.annotation.Annotation;

public interface Encoder<T> {
    default boolean validate(EncodeContext context) {
        return true;
    }

    void encode(byte[] datagram, Endian endian, Annotation dataTypeAnnotation, T value);
}
