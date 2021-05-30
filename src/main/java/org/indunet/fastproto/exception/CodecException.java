package org.indunet.fastproto.exception;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class CodecException extends RuntimeException {
    public CodecException() {
        super();
    }

    public CodecException(String message) {
        super(message);
    }

    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }
}
