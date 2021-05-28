package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.annotation.Type.DataType;
import org.indunet.fastproto.annotation.type.IntegerType;
import org.indunet.fastproto.annotation.type.LongType;
import org.indunet.fastproto.annotation.type.TimestampType;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;
import org.indunet.fastproto.util.EncodeUtils;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class TimestampEncoder implements TypeEncoder {
    @Override
    public void encode(EncodeContext context) {
        EndianPolicy policy = context.getEndianPolicy();
        TimestampType type = context.getDataType(TimestampType.class);
        Timestamp value = context.getValue(Timestamp.class);

        this.encode(context.getDatagram(), type.value(), type.dataType(), policy, type.unit(), value);
    }

    public void encode(byte[] datagram, int byteOffset, DataType dataType, EndianPolicy policy, TimeUnit unit, Timestamp value) {
        if (dataType == Type.DataType.LONG_TYPE && unit == TimeUnit.MILLISECONDS) {
            if (byteOffset + LongType.SIZE >= datagram.length) {
                throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
            }

            EncodeUtils.longType(datagram, byteOffset, policy, value.getTime());
        } else if (dataType == DataType.INTEGER_TYPE && unit == TimeUnit.SECONDS) {
            if (byteOffset + IntegerType.SIZE >= datagram.length) {
                throw new EncodeException(EncodeError.EXCEEDED_DATAGRAM_SIZE);
            }

            EncodeUtils.integerType(datagram, byteOffset, policy, (int) (value.getTime() / 1000));
        } else {
            throw new EncodeException(EncodeError.ILLEGAL_TIMESTAMP_PARAMETERS);
        }
    }
}
