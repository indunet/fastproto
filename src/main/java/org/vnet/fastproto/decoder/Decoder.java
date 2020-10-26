package org.vnet.fastproto.decoder;

import org.vnet.fastproto.CodecSession;

public interface Decoder<T> {
    boolean validate(CodecSession session);
    T decode(CodecSession session);
}
