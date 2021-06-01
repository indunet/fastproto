package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.util.NumberUtils;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class TimestampEncoderTest {
    @Test
    public void testEncode() {
        TimestampEncoder encoder = new TimestampEncoder();
        byte[] datagram = new byte[8];
        long current = System.currentTimeMillis();

        encoder.encode(datagram, 0, Type.DataType.LONG_TYPE, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, new Timestamp(current));
        assertArrayEquals(datagram, NumberUtils.longToBinary(current));
    }
}