package org.indunet.fastproto.decoder;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.annotation.type.UInteger64Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class UInteger64Decoder implements TypeDecoder<BigInteger> {
    @Override
    public BigInteger decode(DecodeContext context) {
        val type = context.getTypeAnnotation(UInteger64Type.class);

        return this.decode(context.getDatagram(), type.value(), context.getEndianPolicy());
    }

    public BigInteger decode(@NonNull final byte[] datagram, int byteOffset, @NonNull EndianPolicy endian) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + UInteger64Type.SIZE > datagram.length) {
            throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        long low = 0, high = 0;

        if (endian == EndianPolicy.LITTLE) {
            low |= (datagram[byteOffset] & 0xFF);
            low |= ((datagram[byteOffset + 1] & 0xFFL) << 8);
            low |= ((datagram[byteOffset + 2] & 0xFFL) << 16);
            low |= ((datagram[byteOffset + 3] & 0xFFL) << 24);

            high |= (datagram[byteOffset + 4] & 0xFFL);
            high |= ((datagram[byteOffset + 5] & 0xFFL) << 8);
            high |= ((datagram[byteOffset + 6] & 0xFFL) << 16);
            high |= ((datagram[byteOffset + 7] & 0xFFL) << 24);
        } else if (endian == EndianPolicy.BIG) {
            low |= (datagram[byteOffset + 7] & 0xFF);
            low |= ((datagram[byteOffset + 6] & 0xFFL) << 8);
            low |= ((datagram[byteOffset + 5] & 0xFFL) << 16);
            low |= ((datagram[byteOffset + 4] & 0xFFL) << 24);

            high |= (datagram[byteOffset + 3] & 0xFFL);
            high |= ((datagram[byteOffset + 2] & 0xFFL) << 8);
            high |= ((datagram[byteOffset + 1] & 0xFFL) << 16);
            high |= ((datagram[byteOffset] & 0xFFL) << 24);
        }

        return new BigInteger(String.valueOf(high))
                .multiply(new BigInteger(String.valueOf(UInteger32Type.MAX_VALUE + 1)))
                .add(new BigInteger(String.valueOf(low)));
    }
}
