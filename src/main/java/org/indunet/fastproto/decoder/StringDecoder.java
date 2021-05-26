package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.type.StringType;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @author Deng Ran
 * @version 1.0
 */
// TODO, Deng Ran
public class StringDecoder implements TypeDecoder<String> {
    @Override
    public String decode(DecodeContext context) {
        StringType type = context.getDataType(StringType.class);

        return this.decode(context.getDatagram(), type.byteOffset(), type.length(), Charset.forName(type.charsetName()));
    }

    public String decode(byte[] datagram, int byteOffset, int length, Charset charset) {
        if (length == -1) {
            return new String(
                    Arrays.copyOfRange(datagram, byteOffset, datagram.length - byteOffset), charset);
        } else {
            return new String(
                    Arrays.copyOfRange(datagram, byteOffset, byteOffset + length), charset);
        }
    }
}
