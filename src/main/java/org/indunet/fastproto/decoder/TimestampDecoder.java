package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.annotation.Type.DataType;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class TimestampDecoder implements TypeDecoder<Timestamp> {
   protected IntegerDecoder integerDecoder = new IntegerDecoder();
   protected LongDecoder longDecoder = new LongDecoder();

    @Override
    public Timestamp decode(DecodeContext context) {
        return null;
    }

    // TODO, think of data type and unit reasonable.
    public Timestamp decode(final byte[] datagram, int byteOffset, DataType dataType, TimeUnit unit) {
//        if (dataType == DataType.LONG_TYPE) {
//             value = Decoders.getDecoder(dataType)
//        } else if (dataType == DataType.INTEGER_TYPE) {
//
//        } else {
//
//        }

        return null;
    }
}
