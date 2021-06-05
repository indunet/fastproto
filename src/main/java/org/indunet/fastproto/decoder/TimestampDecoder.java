package org.indunet.fastproto.decoder;

import lombok.NonNull;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Type.DataType;
import org.indunet.fastproto.annotation.type.IntegerType;
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.annotation.type.TimestampType;
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
        TimestampType type = context.getDataType(TimestampType.class);
        DataType dataType = type.dataType();

        return this.decode(context.getDatagram(), type.value(), dataType, policy, type.unit());
    }

    public Timestamp decode(@NonNull final byte[] datagram, int byteOffset, @NonNull DataType dataType, @NonNull EndianPolicy policy, @NonNull TimeUnit unit) {
        if (byteOffset < 0) {
            throw new DecodeException(DecodeError.ILLEGAL_BYTE_OFFSET);
        } else if (dataType == DataType.LONG_TYPE && unit == TimeUnit.MILLISECONDS) {
            if (byteOffset + LongType.SIZE > datagram.length) {
                throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
            }

            long value = DecodeUtils.longType(datagram, byteOffset);

            return new Timestamp(value);
        } else if (dataType == DataType.INTEGER_TYPE && unit == TimeUnit.SECONDS) {
            if (byteOffset + IntegerType.SIZE > datagram.length) {
                throw new DecodeException(DecodeError.EXCEEDED_DATAGRAM_SIZE);
            }

            int value = DecodeUtils.integerType(datagram, byteOffset);

            return new Timestamp(value * 1000);
        } else {
            throw new DecodeException(DecodeError.ILLEGAL_TIMESTAMP_PARAMETERS);
        }
    }
}
