package org.indunet.fastproto.iot.datagram;

/**
 * @author Deng Ran
 * @since  1.6.2
 */
public enum CheckCode {
    Valid(1, "Valid"),
    Invalid(2, "Invalid");

    int code;
    String message;

    CheckCode(int value, String message) {
        this.code = value;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
