package org.indunet.fastproto.util;

import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.type.UInt64Type;
import org.indunet.fastproto.graph.ReferenceResolver;
import org.indunet.fastproto.iot.Everything;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Deng Ran
 * @since 1.5.3
 */
public class VersionUtilsTest {
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
            .aUInteger64(new BigInteger(String.valueOf(UInt64Type.MAX_VALUE)))
            .build();

    public VersionUtilsTest() {
        // Init datagram.
        CodecUtils.type(datagram, 0, 1, everything.getABoolean());
        CodecUtils.type(datagram, 1, everything.getAByte());
        CodecUtils.type(datagram, 2, EndianPolicy.LITTLE, everything.getAShort());
        CodecUtils.type(datagram, 4, EndianPolicy.LITTLE, everything.getAInteger());
        CodecUtils.type(datagram, 8, EndianPolicy.LITTLE, everything.getALong());
        CodecUtils.type(datagram, 16, EndianPolicy.LITTLE, everything.getAFloat());
        CodecUtils.type(datagram, 20, EndianPolicy.LITTLE, everything.getADouble());
        CodecUtils.integer8Type(datagram, 28, everything.getAInteger8());
        CodecUtils.integer16Type(datagram, 30, EndianPolicy.LITTLE, everything.getAInteger16());
        CodecUtils.uinteger8Type(datagram, 32, everything.getAUInteger8());
        CodecUtils.uinteger16Type(datagram, 34, EndianPolicy.LITTLE, everything.getAUInteger16());
        CodecUtils.uinteger32Type(datagram, 36, EndianPolicy.LITTLE, everything.getAUInteger32());
        CodecUtils.type(datagram, 40, everything.getAByteArray());
        CodecUtils.type(datagram, 50, everything.getAString().getBytes());
        CodecUtils.type(datagram, 56, EndianPolicy.LITTLE, everything.getATimestamp().getTime());
        CodecUtils.type(datagram, 64, EndianPolicy.LITTLE, everything.getACharacter());
        CodecUtils.type(datagram, 70, EndianPolicy.LITTLE, everything.getAUInteger64());
        CodecUtils.uinteger16Type(datagram, 78, EndianPolicy.LITTLE, 17);
    }

    @Test
    public void testValidate() {
        val graph = ReferenceResolver.resolve(Everything.class);
        assertTrue(VersionUtils.validate(datagram, graph.root()));
    }

    @Test
    public void testDecode() {
        val graph = ReferenceResolver.resolve(Everything.class);
        assertEquals(17, VersionUtils.decode(datagram, graph.root()));
    }

    @Test
    public void testEncode() {
        byte[] datagram = new byte[80];
        val graph = ReferenceResolver.resolve(Everything.class);
        VersionUtils.encode(datagram, graph.root());

        assertEquals(17, datagram[78]);
    }
}
