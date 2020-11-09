package org.indunet.fastproto.encoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.assist.FieldAssist;

import java.lang.annotation.Annotation;

public interface Encoder<T> {
    default boolean validate(FieldAssist fieldAssist) {
        return true;
    }

    void encode(byte[] datagram, Endian endian, Annotation dataTypeAnnotation, T value);
}
