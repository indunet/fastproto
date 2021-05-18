package org.indunet.fastproto.decoder;

@FunctionalInterface
public interface TypeDecoder<T> {
    T decode(DecodeContext context);
}
