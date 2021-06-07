package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.util.NumberUtils;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class TimestampDecoderTest {
    TimestampDecoder decoder = new TimestampDecoder();

    @Test
    public void testDecode1() {
        long current = System.currentTimeMillis();
        Timestamp timestamp = decoder.decode(NumberUtils.longToBinary(current), 0, Type.DataType.LONG_TYPE, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS);

        assertEquals(current, timestamp.getTime());
    }

    @Test
    public void testDecode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.decoder.decode(null));
        assertThrows(NullPointerException.class, () -> this.decoder.decode(null, 0, Type.DataType.LONG_TYPE, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS));

        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, -1, Type.DataType.LONG_TYPE, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS));
        assertThrows(DecodeException.class, () -> this.decoder.decode(datagram, 10, Type.DataType.LONG_TYPE, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS));
    }
}