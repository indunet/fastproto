package org.indunet.fastproto.encoder;

public interface Encoder {
    default boolean validate(EncodeContext context) {
        return true;
    }

    void encode(EncodeContext context);
}
