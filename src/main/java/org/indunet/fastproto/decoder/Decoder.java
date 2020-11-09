package org.indunet.fastproto.decoder;

public interface Decoder<T> {
    default boolean validate(DecodeContext context) {
        return true;
    };

    T decode(DecodeContext context);
}
