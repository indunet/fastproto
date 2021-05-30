package org.indunet.fastproto.decoder;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
@FunctionalInterface
public interface TypeDecoder<T> {
    T decode(DecodeContext context);
}
