package org.indunet.fastproto.encoder;

import java.util.function.Consumer;

@FunctionalInterface
public interface TypeEncoder<T>  {
    void encode(EncodeContext<T> context);
}
