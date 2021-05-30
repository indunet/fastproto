package org.indunet.fastproto.util;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.decoder.*;

import java.nio.charset.Charset;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class DecodeUtils {
    protected static BinaryDecoder binaryDecoder = new BinaryDecoder();
    protected static BooleanDecoder booleanDecoder = new BooleanDecoder();
    protected static ByteDecoder byteDecoder = new ByteDecoder();
    protected static ShortDecoder shortDecoder = new ShortDecoder();
    protected static IntegerDecoder integerDecoder = new IntegerDecoder();
    protected static LongDecoder longDecoder = new LongDecoder();
    protected static FloatDecoder floatDecoder = new FloatDecoder();
    protected static DoubleDecoder doubleDecoder = new DoubleDecoder();
    protected static StringDecoder stringDecoder = new StringDecoder();

    public static byte[] binaryType(final byte[] datagram, int byteOffset, int length) {
        return binaryDecoder.decode(datagram, byteOffset, length);
    }

    public static boolean booleanType(final byte[] datagram, int byteOffset, int bitOffset) {
        return booleanDecoder.decode(datagram, byteOffset, bitOffset);
    }

    public static byte byteType(final byte[] datagram, int byteOffset) {
        return byteDecoder.decode(datagram, byteOffset);
    }

    public static short shortType(final byte[] datagram, int byteOffset) {
        return shortType(datagram, byteOffset, EndianPolicy.LITTLE);
    }

    public static short shortType(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        return shortDecoder.decode(datagram, byteOffset, policy);
    }

    public static int integerType(final byte[] datagram, int byteOffset) {
        return integerType(datagram, byteOffset, EndianPolicy.LITTLE);
    }

    public static int integerType(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        return integerDecoder.decode(datagram, byteOffset, policy);
    }

    public static long longType(final byte[] datagram, int byteOffset) {
        return longType(datagram, byteOffset, EndianPolicy.LITTLE);
    }

    public static long longType(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        return longDecoder.decode(datagram, byteOffset, policy);
    }

    public static float floatType(final byte[] datagram, int byteOffset) {
        return floatType(datagram, byteOffset, EndianPolicy.LITTLE);
    }

    public static float floatType(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        return floatDecoder.decode(datagram, byteOffset, policy);
    }

    public static double doubleType(final byte[] datagram, int byteOffset) {
        return doubleType(datagram, byteOffset, EndianPolicy.LITTLE);
    }

    public static double doubleType(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        return doubleDecoder.decode(datagram, byteOffset, policy);
    }

    public static String stringType(final byte[] datagram, int byteOffset, int length) {
        return stringDecoder.decode(datagram, byteOffset, length, Charset.defaultCharset());
    }

    public static String stringType(final byte[] datagram, int byteOffset, int length, Charset set) {
        return stringDecoder.decode(datagram, byteOffset, length, set);
    }
}
