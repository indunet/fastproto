package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Type.DataType;
import org.indunet.fastproto.annotation.type.TimestampType;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;
import org.indunet.fastproto.util.DecodeUtils;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class TimestampDecoder implements TypeDecoder<Timestamp> {
    @Override
    public Timestamp decode(DecodeContext context) {
        EndianPolicy policy = context.getEndianPolicy();
        TimestampType type = context.getDataType(TimestampType.class);
        DataType dataType = type.dataType();

        return this.decode(context.getDatagram(), type.value(), dataType, policy, type.unit());
    }

    public Timestamp decode(final byte[] datagram, int byteOffset, DataType dataType, EndianPolicy policy, TimeUnit unit) {
        if (dataType == DataType.LONG_TYPE && unit == TimeUnit.MILLISECONDS) {
            long value = DecodeUtils.longType(datagram, byteOffset);

            return new Timestamp(value);
        } else if (dataType == DataType.INTEGER_TYPE && unit == TimeUnit.SECONDS) {
            int value = DecodeUtils.integerType(datagram, byteOffset);

            return new Timestamp(value * 1000);
        } else {
            throw new DecodeException(DecodeError.ILLEGAL_TIMESTAMP_PARAMETERS);
        }
    }
}
