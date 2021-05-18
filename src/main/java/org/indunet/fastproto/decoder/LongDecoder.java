package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.LongType;

public class LongDecoder implements TypeDecoder<Long> {
    @Override
    public Long decode(DecodeContext context) {
        LongType type = context.getDataType(LongType.class);

        return this.decode(context.getDatagram(), type.byteOffset(), context.getEndianPolicy());
    }

    public long decode(final byte[] datagram, int byteOffset, EndianPolicy endian) {
        if (datagram.length - LongType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        long value = 0;

        if (endian == EndianPolicy.LITTLE) {
            value |= (datagram[byteOffset] & 0xFF);
            value |= ((long) (datagram[byteOffset + 1] & 0xFFL) << 8);
            value |= ((long) (datagram[byteOffset + 2] & 0xFFL) << 16);
            value |= ((long) (datagram[byteOffset + 3] & 0xFFL) << 24);
            value |= ((long) (datagram[byteOffset + 4] & 0xFFL) << 32);
            value |= ((long) (datagram[byteOffset + 5] & 0xFFL) << 40);
            value |= ((long) (datagram[byteOffset + 6] & 0xFFL) << 48);
            value |= ((long) (datagram[byteOffset + 7] & 0xFFL) << 56);
        } else if (endian == EndianPolicy.BIG) {
            value |= (datagram[byteOffset + 7] & 0xFF);
            value |= ((long) (datagram[byteOffset + 6] & 0xFFL) << 8);
            value |= ((long) (datagram[byteOffset + 5] & 0xFFL) << 16);
            value |= ((long) (datagram[byteOffset + 4] & 0xFFL) << 24);
            value |= ((long) (datagram[byteOffset + 3] & 0xFFL) << 32);
            value |= ((long) (datagram[byteOffset + 2] & 0xFFL) << 40);
            value |= ((long) (datagram[byteOffset + 1] & 0xFFL) << 48);
            value |= ((long) (datagram[byteOffset] & 0xFFL) << 56);
        }

        return value;
    }
}
