package org.indunet.fastproto.encoder;

@FunctionalInterface
public interface Encoder<T> {
    void encode(EncodeContext context, T value);
}
