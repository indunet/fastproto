package org.indunet.fastproto.encoder;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
@FunctionalInterface
public interface TypeEncoder {
    void encode(EncodeContext context);
}
