package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.util.FieldInfo;

import java.lang.annotation.Annotation;

public interface Decoder<T> {
    default boolean validate(FieldInfo fieldInfo) {
        return true;
    }

    T decode(final byte[] datagram, Endian endian, Annotation dataTypeAnnotation);
}
