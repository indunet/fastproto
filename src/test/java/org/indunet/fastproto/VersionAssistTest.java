package org.indunet.fastproto;

import org.indunet.fastproto.annotation.type.UInteger64Type;
import org.indunet.fastproto.iot.Everything;
import org.indunet.fastproto.util.EncodeUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Deng Ran
 * @since 1.5.3
 */
public class VersionAssistTest {
    byte[] datagram = new byte[80];
    Everything everything = Everything.builder()
            .aBoolean(true)
            .aByte((byte) -12)
            .aByteArray(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
            .aCharacter('A')
            .aDouble(3.14)
            .aFloat(0.618f)
            .aInteger(102)
            .aInteger8(32)
            .aInteger16(13)
            .aLong(-50000l)
            .aShort((short) 12)
            .aString("abcABC")
            .aTimestamp(new Timestamp(System.currentTimeMillis()))
            .aUInteger8(8)
            .aUInteger16(16)
            .aUInteger32(32l)
            .speed(10.1f)
            .aUInteger64(new BigInteger(String.valueOf(UInteger64Type.MAX_VALUE)))
            .build();

    public VersionAssistTest() {
        // Init datagram.
        EncodeUtils.type(datagram, 0, 1, everything.getABoolean());
        EncodeUtils.type(datagram, 1, everything.getAByte());
        EncodeUtils.type(datagram, 2, everything.getAShort());
        EncodeUtils.type(datagram, 4, everything.getAInteger());
        EncodeUtils.type(datagram, 8, everything.getALong());
        EncodeUtils.type(datagram, 16, everything.getAFloat());
        EncodeUtils.type(datagram, 20, everything.getADouble());
        EncodeUtils.integer8Type(datagram, 28, everything.getAInteger8());
        EncodeUtils.integer16Type(datagram, 30, everything.getAInteger16());
        EncodeUtils.uInteger8Type(datagram, 32, everything.getAUInteger8());
        EncodeUtils.uInteger16Type(datagram, 34, everything.getAUInteger16());
        EncodeUtils.uInteger32Type(datagram, 36, everything.getAUInteger32());
        EncodeUtils.type(datagram, 40, everything.getAByteArray());
        EncodeUtils.type(datagram, 50, everything.getAString());
        EncodeUtils.type(datagram, 56, everything.getATimestamp().getTime());
        EncodeUtils.type(datagram, 64, everything.getACharacter());
        EncodeUtils.type(datagram, 70, everything.getAUInteger64());
        EncodeUtils.uInteger16Type(datagram, 78, 17);
    }

    @Test
    public void testValidate() {
        assertTrue(VersionAssist.validate(datagram, Everything.class));
    }

    @Test
    public void testDecode() {
        assertEquals(17, VersionAssist.decode(datagram, Everything.class));
    }

    @Test
    public void testEncode() {
        byte[] datagram = new byte[80];
        VersionAssist.encode(datagram, Everything.class);

        assertEquals(17, datagram[78]);
    }
}
