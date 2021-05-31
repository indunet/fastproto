package org.indunet.fastproto.util;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.encoder.*;

import java.nio.charset.Charset;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class EncodeUtils {
    protected static BinaryEncoder binaryEncoder = new BinaryEncoder();
    protected static CharacterEncoder characterEncoder = new CharacterEncoder();
    protected static BooleanEncoder booleanEncoder = new BooleanEncoder();
    protected static ByteEncoder byteEncoder = new ByteEncoder();
    protected static ShortEncoder shortEncoder = new ShortEncoder();
    protected static IntegerEncoder integerEncoder = new IntegerEncoder();
    protected static LongEncoder longEncoder = new LongEncoder();
    protected static FloatEncoder floatEncoder = new FloatEncoder();
    protected static DoubleEncoder doubleEncoder = new DoubleEncoder();
    protected static StringEncoder stringEncoder = new StringEncoder();

    public static void type(final byte[] datagram, int byteOffset, byte[] values) {
        binaryEncoder.encode(datagram, byteOffset, values);
    }

    public static void binaryType(final byte[] datagram, int byteOffset, byte[] values) {
        binaryEncoder.encode(datagram, byteOffset, values);
    }

    public static void type(final byte[] datagram, int byteOffset, char value) {
        characterType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void characterType(final byte[] datagram, int byteOffset, char value) {
        characterType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void type(final byte[] datagram, int byteOffset, EndianPolicy policy, char value) {
        characterType(datagram, byteOffset, policy, value);
    }

    public static void characterType(final byte[] datagram, int byteOffset, EndianPolicy policy, char value) {
        characterType(datagram, byteOffset, policy, value);
    }

    public static void type(final byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        booleanEncoder.encode(datagram, byteOffset, bitOffset, value);
    }

    public static void booleanType(final byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        booleanEncoder.encode(datagram, byteOffset, bitOffset, value);
    }

    public static void type(final byte[] datagram, int byteOffset, byte value) {
        byteEncoder.encode(datagram, byteOffset, value);
    }

    public static void byteType(final byte[] datagram, int byteOffset, byte value) {
        byteEncoder.encode(datagram, byteOffset, value);
    }

    public static void type(final byte[] datagram, int byteOffset, short value) {
        shortType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void shortType(final byte[] datagram, int byteOffset, short value) {
        shortType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void type(final byte[] datagram, int byteOffset, EndianPolicy policy, short value) {
        shortEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void shortType(final byte[] datagram, int byteOffset, EndianPolicy policy, short value) {
        shortEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void type(final byte[] datagram, int byteOffset, int value) {
        integerType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void integerType(final byte[] datagram, int byteOffset, int value) {
        integerType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void type(final byte[] datagram, int byteOffset, EndianPolicy policy, int value) {
        integerEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void integerType(final byte[] datagram, int byteOffset, EndianPolicy policy, int value) {
        integerEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void type(final byte[] datagram, int byteOffset, long value) {
        longType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void longType(final byte[] datagram, int byteOffset, long value) {
        longType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void type(final byte[] datagram, int byteOffset, EndianPolicy policy, long value) {
        longEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void longType(final byte[] datagram, int byteOffset, EndianPolicy policy, long value) {
        longEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void type(final byte[] datagram, int byteOffset, float value) {
        floatType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void floatType(final byte[] datagram, int byteOffset, float value) {
        floatType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void type(final byte[] datagram, int byteOffset, EndianPolicy policy, float value) {
        floatEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void floatType(final byte[] datagram, int byteOffset, EndianPolicy policy, float value) {
        floatEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void type(final byte[] datagram, int byteOffset, double value) {
        doubleType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void doubleType(final byte[] datagram, int byteOffset, double value) {
        doubleType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void type(final byte[] datagram, int byteOffset, EndianPolicy policy, double value) {
        doubleEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void doubleType(final byte[] datagram, int byteOffset, EndianPolicy policy, double value) {
        doubleEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void type(final byte[] datagram, int byteOffset, int length, String value) {
        stringType(datagram, byteOffset, length, Charset.defaultCharset(), value);
    }

    public static void stringType(final byte[] datagram, int byteOffset, int length, String value) {
        stringType(datagram, byteOffset, length, Charset.defaultCharset(), value);
    }

    public static void type(final byte[] datagram, int byteOffset, int length, Charset set, String value) {
        stringEncoder.encode(datagram, byteOffset, length, set, value);
    }

    public static void stringType(final byte[] datagram, int byteOffset, int length, Charset set, String value) {
        stringEncoder.encode(datagram, byteOffset, length, set, value);
    }
}
