package org.indunet.fastproto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class EncodeException extends CodecException {
    public EncodeException(EncodeError error) {
        this(error.getMessage());
    }

    public EncodeException(EncodeError error, Throwable cause) {
        this(error.getMessage(), cause);
    }

    public EncodeException(String message) {
        super(message);
    }

    public EncodeException(String message, Throwable cause) {
        super(message, cause);
    }

    @AllArgsConstructor
    @Getter
    public enum EncodeError {
        NO_VALID_ENCODER_FOUND("No valid encoder found for {0}"),
        EXCEEDED_DATAGRAM_SIZE("Exceeded datagram size."),
        ILLEGAL_BIT_OFFSET("Illegal bit offset."),
        FAIL_INITIALIZING_ENCODER("Fail initializing encoder of {0}."),
        FAIL_INITIALIZING_ENCODE_FORMULA("Fail initilizing encode formula of {0}."),
        FAIL_GETTING_FIELD_VALUE("Unable to get the value of filed {0}."),
        ILLEGAL_TIMESTAMP_PARAMETERS("Illgeal timestamp parameters.");

        String message;
    }
}
