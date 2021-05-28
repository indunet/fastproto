package org.indunet.fastproto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.ElementType;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class DecodeException extends CodecException {

    public DecodeException(DecodeError error) {
        this(error.getMessage());
    }

    public DecodeException(DecodeError error, Throwable cause) {
        this(error.getMessage(), cause);
    }

    public DecodeException(String message) {
        super(message);
    }

    public DecodeException(String message, Throwable cause) {
        super(message);
    }

    @AllArgsConstructor
    @Getter
    public enum DecodeError {
        NO_VALID_DECODER_FOUND("No valid decoder found for {0}."),
        EXCEEDED_DATAGRAM_SIZE("Exceeded datagram size."),
        FAIL_INITIALIZING_DECODER("Fail initializing decoder {0}."),
        FAIL_INITIALIZING_DECODE_FORMULA("Fail initializing decode formula {0}."),
        FAIL_INITIALIZING_DECODE_OBJECT("Fail initializing decode object {0}"),
        NOT_FOUND_DECODER("Decoder for data type {0} cannot be found."),
        ILLEGAL_TIMESTAMP_PARAMETERS("Illgeal timestamp parameters.");

        String message;
    }
}
