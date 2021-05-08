package org.indunet.fastproto.exception;

// TODO, exception enum.
public class CodecException extends RuntimeException {
    public CodecException(String message) {
        super(message);
    }

    public enum Error {
        Datagram_Not_Found(0x0001, "The datagram cannot be found."),
        Datagram_Out_Of_Range(0x0002, "The offset is out of range of the datagram."),
        Decoder_Field_Mismatch(0x0004, "The decoder and the field are unmatch."),
        Encoder_Field_Mismatch(0x0008, "The decoder and the field are unmatch."),
        Decoder_Formula_Mismatch(0x0010, "The decoder and the formula are unmatch."),
        Encoder_Formula_Mismatch(0x0020, "The encoder and the formula are unmatch.");

        int code;
        String message;

        Error(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return this.code;
        }

        public String getMessage() {
            return this.message;
        }
    }
}
