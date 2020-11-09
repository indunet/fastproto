package org.indunet.fastproto.decoder;

import org.indunet.fastproto.annotation.TimestampType;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class TimestampDecoder implements Decoder<Timestamp> {
    @Override
    public Timestamp decode(DecodeContext context) {
        int byteOffset = context.getDataTypeAnnotation(TimestampType.class).byteOffset();
        TimeUnit unit = context.getDataTypeAnnotation(TimestampType.class).unit();
        byte[] datagram = context.getDatagram();

        if (datagram.length - TimestampType.SIZE < byteOffset) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (unit == TimeUnit.SECONDS) {
            // long value = DecodeUtils.decode();
        } else if (unit == TimeUnit.MILLISECONDS) {
            // long value = this.longDecoder.get(datagram, byteOffset, endian);
        }

        return new Timestamp(System.currentTimeMillis());
    }
}
