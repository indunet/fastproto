package org.indunet.fastproto.decoder;

import org.indunet.fastproto.Endian;
import org.indunet.fastproto.annotation.BooleanType;

import java.lang.annotation.Annotation;

public class BooleanDecoder implements Decoder<Boolean> {
    public final static int MAX_BIT_OFFSET = 7;
    public final static int MIN_BIT_OFFSET = 0;

    @Override
    public Boolean decode(final byte[] datagram, Endian endian, Annotation dataTypeAnnotation) {
        int byteOffset = ((BooleanType) dataTypeAnnotation).byteOffset();
        int bitOffset = ((BooleanType) dataTypeAnnotation).bitOffset();

        if (datagram.length <= byteOffset) {
            // TODO
            throw new ArrayIndexOutOfBoundsException();
        }

        return (datagram[byteOffset] & (0x01 << bitOffset)) != 0;
    }
}
