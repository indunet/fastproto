package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.BooleanType;

import java.lang.annotation.Annotation;

public class BooleanDecoder implements Decoder<Boolean> {
    public final static int MAX_BIT_OFFSET = 7;
    public final static int MIN_BIT_OFFSET = 0;

    @Override
    public Boolean decode(DecodeContext context) {
        int byteOffset = ((BooleanType) context.getDataTypeAnnotation()).byteOffset();
        int bitOffset = ((BooleanType) context.getDataTypeAnnotation()).bitOffset();
        byte[] datagram = context.getDatagram();

        if (datagram.length <= byteOffset) {
            // TODO
            throw new ArrayIndexOutOfBoundsException();
        }

        return (datagram[byteOffset] & (0x01 << bitOffset)) != 0 ? true : false;
    }
}
