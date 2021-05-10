package org.indunet.fastproto.decoder;

import com.sun.xml.internal.ws.util.xml.CDATA;
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
        return new String(Arrays.copyOfRange(datagram, byteOffset, byteOffset + length), charset);
    }
}
