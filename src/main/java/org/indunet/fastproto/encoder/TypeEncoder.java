package org.indunet.fastproto.encoder;

@FunctionalInterface
public interface TypeEncoder<T> {
    void encode(EncodeContext<T> context);
}
