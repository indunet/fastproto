package org.vnet.fastproto.encoder;

import org.vnet.fastproto.CodecSession;

public interface Encoder<T> {
    boolean validate(CodecSession session);
    T encode(CodecSession session);
}
