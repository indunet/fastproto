package org.indunet.fastproto.decoder;

@FunctionalInterface
public interface TypeDecoder<R> {
    R decode(DecodeContext context);
}
