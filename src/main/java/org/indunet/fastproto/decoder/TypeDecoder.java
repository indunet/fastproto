package org.indunet.fastproto.decoder;

import java.util.function.Function;

@FunctionalInterface
public interface TypeDecoder<T> {
    T decode(DecodeContext context);

    default TypeDecoder<T> identify() {
        return this;
    }
}
