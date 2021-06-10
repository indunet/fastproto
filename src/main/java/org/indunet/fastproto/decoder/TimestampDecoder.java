package org.indunet.fastproto.decoder;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.annotation.type.TimestampType;
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;
import org.indunet.fastproto.util.DecodeUtils;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;


/**
 * Timestamp type decoder.
 *
 * @author Deng Ran
 * @see TypeDecoder
 * @since 1.1.0
 */
public class TimestampDecoder implements TypeDecoder<Timestamp> {
    @Override
    public Timestamp decode(@NonNull DecodeContext context) {
        EndianPolicy policy = context.getEndianPolicy();
        TimestampType type = context.getTypeAnnotation(TimestampType.class);
        ProtocolType dataType = type.protocolType();

        return this.decode(context.getDatagram(), type.value(), dataType, policy, type.unit());
    }

    public Timestamp decode(@NonNull final byte[] datagram, int byteOffset, @NonNull ProtocolType dataType, @NonNull EndianPolicy policy, @NonNull TimeUnit unit) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (dataType == ProtocolType.LONG && unit == TimeUnit.MILLISECONDS) {
            if (byteOffset + LongType.SIZE > datagram.length) {
                throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
            }

            val value = DecodeUtils.longType(datagram, byteOffset, policy);

            return new Timestamp(value);
        } else if (dataType == ProtocolType.UINTEGER32 && unit == TimeUnit.SECONDS) {
            if (byteOffset + UInteger32Type.SIZE > datagram.length) {
                throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
            }

            val value = DecodeUtils.uInteger32Type(datagram, byteOffset, policy);

            return new Timestamp(value * 1000);
        } else {
            throw new DecodeException(DecodeError.ILLEGAL_TIMESTAMP_PARAMETERS);
        }
    }
}
