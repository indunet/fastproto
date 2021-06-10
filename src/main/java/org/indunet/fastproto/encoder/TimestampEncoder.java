package org.indunet.fastproto.encoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.annotation.type.TimestampType;
import org.indunet.fastproto.annotation.type.UInteger32Type;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;
import org.indunet.fastproto.util.EncodeUtils;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * Timestamp type encoder.
 *
 * @author Deng Ran
 * @see TypeEncoder
 * @since 1.1.0
 */
public class TimestampEncoder implements TypeEncoder {
    @Override
    public void encode(@NonNull EncodeContext context) {
        EndianPolicy policy = context.getEndianPolicy();
        TimestampType type = context.getTypeAnnotation(TimestampType.class);
        Timestamp value = context.getValue(Timestamp.class);

        this.encode(context.getDatagram(), type.value(), type.protocolType(), policy, type.unit(), value);
    }

    public void encode(@NonNull byte[] datagram, int byteOffset, @NonNull ProtocolType dataType, @NonNull EndianPolicy policy, @NonNull TimeUnit unit, @NonNull Timestamp value) {
        byteOffset = byteOffset >= 0 ? byteOffset : datagram.length + byteOffset;

        if (byteOffset < 0) {
            throw new EncodeException(EncodeError.ILLEGAL_BYTE_OFFSET);
        } else if (dataType == ProtocolType.LONG && unit == TimeUnit.MILLISECONDS) {
            if (byteOffset + LongType.SIZE > datagram.length) {
                throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
            }

            EncodeUtils.longType(datagram, byteOffset, policy, value.getTime());
        } else if (dataType == ProtocolType.UINTEGER32 && unit == TimeUnit.SECONDS) {
            if (byteOffset + UInteger32Type.SIZE > datagram.length) {
                throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
            }

            EncodeUtils.integerType(datagram, byteOffset, policy, (int) (value.getTime() / 1000));
        } else {
            throw new EncodeException(EncodeError.ILLEGAL_TIMESTAMP_PARAMETERS);
        }
    }
}
