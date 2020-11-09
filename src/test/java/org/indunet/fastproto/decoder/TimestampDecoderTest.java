package org.indunet.fastproto.decoder;

import org.junit.Test;
import org.vnet.fastproto.Endian;
import org.vnet.fastproto.annotation.TimestampType;
import org.vnet.fastproto.utils.NumberUtils;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

public class TimestampDecoderTest {
    StandardTimestampDecoder decoder = new StandardTimestampDecoder();

    @Test
    public void testGet() {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        byte[] bytes = NumberUtils.longToByteArray(current.getTime());      // Milisecond.

        Timestamp timestamp = decoder.get(bytes, 0, TimestampType.Unit.Eight_Bytes_Millisecond, Endian.Little);
        assertEquals(timestamp, current);

        bytes = NumberUtils.longToByteArray(current.getTime() / 1000);      // Second.

        timestamp = decoder.get(bytes, 0, TimestampType.Unit.Four_Bytes_Second, Endian.Little);
        assertEquals(timestamp.getTime() / 1000, current.getTime() / 1000);
    }
}
