package org.indunet.fastproto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Codec exception.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
public class CodecException extends RuntimeException {
    public CodecException() {
        super();
    }

    public CodecException(CodecError error) {
        this(error.getMessage());
    }

    public CodecException(String message) {
        super(message);
    }

    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }

    @AllArgsConstructor
    @Getter
    public static enum CodecError {
        INVALID_ENDIAN_POLICY("Invalid endian policy."),
        INVALID_COMPRESS_POLICY("Invalid compress policy.");

        String message;
    }
}
