package org.indunet.fastproto.decoder;

@FunctionalInterface
public interface Decoder<R> {
    R decode(DecodeContext context);
}
