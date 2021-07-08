/*
 * Copyright 2019-2021 indunet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.indunet.fastproto.encoder;

import org.indunet.fastproto.EndianPolicy;

import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * Encode utils.
 *
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
    protected static Integer8Encoder integer8Encoder = new Integer8Encoder();
    protected static Integer16Encoder integer16Encoder = new Integer16Encoder();
    protected static UInteger8Encoder uInteger8Encoder = new UInteger8Encoder();
    protected static UInteger16Encoder uInteger16Encoder = new UInteger16Encoder();
    protected static UInteger32Encoder uInteger32Encoder = new UInteger32Encoder();
    protected static UInteger64Encoder uInteger64Encoder = new UInteger64Encoder();


    public static void type(byte[] datagram, int byteOffset, byte[] values) {
        binaryEncoder.encode(datagram, byteOffset, -1, values);
    }

    public static void type(byte[] datagram, int byteOffset, int length, byte[] values) {
        binaryEncoder.encode(datagram, byteOffset, length, values);
    }

    public static void binaryType(byte[] datagram, int byteOffset, byte[] values) {
        binaryEncoder.encode(datagram, byteOffset, -1, values);
    }

    public static void binaryType(byte[] datagram, int byteOffset, int length, byte[] values) {
        binaryEncoder.encode(datagram, byteOffset, length, values);
    }

    public static void type(final byte[] datagram, int byteOffset, char value) {
        characterType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void characterType(final byte[] datagram, int byteOffset, char value) {
        characterType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void characterType(byte[] datagram, int byteOffset, EndianPolicy policy, char value) {
        type(datagram, byteOffset, policy, value);
    }

    public static void type(byte[] datagram, int byteOffset, EndianPolicy policy, char value) {
        characterEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void type(byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        booleanEncoder.encode(datagram, byteOffset, bitOffset, value);
    }

    public static void booleanType(byte[] datagram, int byteOffset, int bitOffset, boolean value) {
        booleanEncoder.encode(datagram, byteOffset, bitOffset, value);
    }

    public static void type(byte[] datagram, int byteOffset, byte value) {
        byteEncoder.encode(datagram, byteOffset, value);
    }

    public static void integer8Type(byte[] datagram, int byteOffset, int value) {
        integer8Encoder.encode(datagram, byteOffset, value);
    }

    public static void uInteger8Type(byte[] datagram, int byteOffset, int value) {
        uInteger8Encoder.encode(datagram, byteOffset, value);
    }

    public static void byteType(byte[] datagram, int byteOffset, byte value) {
        byteEncoder.encode(datagram, byteOffset, value);
    }

    public static void type(byte[] datagram, int byteOffset, short value) {
        shortType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void integer16Type(byte[] datagram, int byteOffset, int value) {
        integer16Type(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void integer16Type(byte[] datagram, int byteOffset, EndianPolicy policy, int value) {
        integer16Encoder.encode(datagram, byteOffset, policy, value);
    }

    public static void uInteger16Type(byte[] datagram, int byteOffset, int value) {
        uInteger16Type(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void uInteger16Type(byte[] datagram, int byteOffset, EndianPolicy policy, int value) {
        uInteger16Encoder.encode(datagram, byteOffset, policy, value);
    }

    public static void shortType(byte[] datagram, int byteOffset, short value) {
        shortType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void type(byte[] datagram, int byteOffset, EndianPolicy policy, short value) {
        shortEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void shortType(byte[] datagram, int byteOffset, EndianPolicy policy, short value) {
        shortEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void uInteger32Type(byte[] datagram, int byteOffset, long value) {
        uInteger32Type(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void uInteger32Type(byte[] datagram, int byteOffset, EndianPolicy policy, long value) {
        uInteger32Encoder.encode(datagram, byteOffset, policy, value);
    }

    public static void type(byte[] datagram, int byteOffset, int value) {
        integerType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void integerType(byte[] datagram, int byteOffset, int value) {
        integerType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void type(byte[] datagram, int byteOffset, EndianPolicy policy, int value) {
        integerEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void integerType(byte[] datagram, int byteOffset, EndianPolicy policy, int value) {
        integerEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void type(byte[] datagram, int byteOffset, long value) {
        longType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void longType(byte[] datagram, int byteOffset, long value) {
        longType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void type(byte[] datagram, int byteOffset, EndianPolicy policy, long value) {
        longEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void longType(byte[] datagram, int byteOffset, EndianPolicy policy, long value) {
        longEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void type(byte[] datagram, int byteOffset, float value) {
        floatType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void floatType(byte[] datagram, int byteOffset, float value) {
        floatType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void type(byte[] datagram, int byteOffset, EndianPolicy policy, float value) {
        floatEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void floatType(byte[] datagram, int byteOffset, EndianPolicy policy, float value) {
        floatEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void type(byte[] datagram, int byteOffset, double value) {
        doubleType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void doubleType(byte[] datagram, int byteOffset, double value) {
        doubleType(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void type(byte[] datagram, int byteOffset, EndianPolicy policy, double value) {
        doubleEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void doubleType(byte[] datagram, int byteOffset, EndianPolicy policy, double value) {
        doubleEncoder.encode(datagram, byteOffset, policy, value);
    }

    public static void type(byte[] datagram, int byteOffset, String value) {
        stringType(datagram, byteOffset, value.length(), Charset.defaultCharset(), value);
    }

    public static void type(byte[] datagram, int byteOffset, int length, String value) {
        stringType(datagram, byteOffset, length, Charset.defaultCharset(), value);
    }

    public static void stringType(byte[] datagram, int byteOffset, int length, String value) {
        stringType(datagram, byteOffset, length, Charset.defaultCharset(), value);
    }

    public static void type(byte[] datagram, int byteOffset, int length, Charset set, String value) {
        stringEncoder.encode(datagram, byteOffset, length, set, value);
    }

    public static void stringType(byte[] datagram, int byteOffset, int length, Charset set, String value) {
        stringEncoder.encode(datagram, byteOffset, length, set, value);
    }

    public static void type(byte[] datagram, int byteOffset, BigInteger value) {
        uInteger64(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void type(byte[] datagram, int byteOffset, EndianPolicy policy, BigInteger value) {
        uInteger64(datagram, byteOffset, policy, value);
    }

    public static void uInteger64(byte[] datagram, int byteOffset, BigInteger value) {
        uInteger64(datagram, byteOffset, EndianPolicy.LITTLE, value);
    }

    public static void uInteger64(byte[] datagram, int byteOffset, EndianPolicy policy, BigInteger value) {
        uInteger64Encoder.encode(datagram, byteOffset, policy, value);
    }
}
