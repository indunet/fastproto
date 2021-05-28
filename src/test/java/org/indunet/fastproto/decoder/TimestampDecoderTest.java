package org.indunet.fastproto.decoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.util.NumberUtils;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class TimestampDecoderTest {
    @Test
    public void testDecode() {
        TimestampDecoder decoder = new TimestampDecoder();
        long current = System.currentTimeMillis();
        Timestamp timestamp = decoder.decode(NumberUtils.longToBinary(current), 0, Type.DataType.LONG_TYPE, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS);

        assertEquals(current, timestamp.getTime());
    }
}