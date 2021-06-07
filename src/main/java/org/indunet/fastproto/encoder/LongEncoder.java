package org.indunet.fastproto.encoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

/**
 * Long type encoder.
 *
 * @author Deng Ran
 * @since 1.0.0
 * @see TypeEncoder
 */
public class LongEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        LongType type = context.getDataType(LongType.class);
        Long value = context.getValue(Long.class);

        this.encode(context.getDatagram(), type.value(), context.getEndianPolicy(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, @NonNull EndianPolicy policy, long value) {
        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (byteOffset + LongType.SIZE > datagram.length) {
            throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
        }

        if (policy == EndianPolicy.BIG) {
            datagram[byteOffset + 7] = (byte) (value & 0xFFL);
            datagram[byteOffset + 6] = (byte) (value >> 8 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (value >> 16 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (value >> 24 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (value >> 32 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (value >> 40 & 0xFFL);
            datagram[byteOffset + 1] = (byte) (value >> 48 & 0xFFL);
            datagram[byteOffset] = (byte) (value >> 56 & 0xFFL);
        } else {
            datagram[byteOffset] = (byte) (value & 0xFFL);
            datagram[byteOffset + 1] = (byte) (value >> 8 & 0xFFL);
            datagram[byteOffset + 2] = (byte) (value >> 16 & 0xFFL);
            datagram[byteOffset + 3] = (byte) (value >> 24 & 0xFFL);
            datagram[byteOffset + 4] = (byte) (value >> 32 & 0xFFL);
            datagram[byteOffset + 5] = (byte) (value >> 40 & 0xFFL);
            datagram[byteOffset + 6] = (byte) (value >> 48 & 0xFFL);
            datagram[byteOffset + 7] = (byte) (value >> 56 & 0xFFL);
        }
    }
}
