package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.util.NumberUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class TimestampEncoderTest {
    TimestampEncoder encoder = new TimestampEncoder();

    @Test
    public void testEncode1() {
        byte[] datagram = new byte[8];
        long current = System.currentTimeMillis();

        encoder.encode(datagram, 0, Type.DataType.LONG_TYPE, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, new Timestamp(current));
        assertArrayEquals(datagram, NumberUtils.longToBinary(current));
    }

    @Test
    public void testEncode2() {
        byte[] datagram = new byte[10];

        assertThrows(NullPointerException.class, () -> this.encoder.encode(null));
        assertThrows(NullPointerException.class,
                () -> this.encoder.encode(null, 0, Type.DataType.LONG_TYPE, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, new Timestamp(System.currentTimeMillis())));
        assertThrows(NullPointerException.class,
                () -> this.encoder.encode(null, 0, Type.DataType.LONG_TYPE, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, null));

        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, -1, Type.DataType.LONG_TYPE, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, new Timestamp(System.currentTimeMillis())));
        assertThrows(EncodeException.class,
                () -> this.encoder.encode(datagram, 10, Type.DataType.LONG_TYPE, EndianPolicy.LITTLE, TimeUnit.MILLISECONDS, new Timestamp(System.currentTimeMillis())));
    }
}