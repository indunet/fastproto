package org.indunet.fastproto.encoder;

@FunctionalInterface
public interface TypeEncoder {
    void encode(EncodeContext context);
}
