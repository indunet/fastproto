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

package org.indunet.fastproto.util;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.decoder.*;

import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * Decode utils.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
public class DecodeUtils {
    protected static BinaryDecoder binaryDecoder = new BinaryDecoder();
    protected static CharacterDecoder characterDecoder = new CharacterDecoder();
    protected static BooleanDecoder booleanDecoder = new BooleanDecoder();
    protected static ByteDecoder byteDecoder = new ByteDecoder();
    protected static ShortDecoder shortDecoder = new ShortDecoder();
    protected static IntegerDecoder integerDecoder = new IntegerDecoder();
    protected static LongDecoder longDecoder = new LongDecoder();
    protected static FloatDecoder floatDecoder = new FloatDecoder();
    protected static DoubleDecoder doubleDecoder = new DoubleDecoder();
    protected static StringDecoder stringDecoder = new StringDecoder();
    protected static Integer8Decoder integer8Decoder = new Integer8Decoder();
    protected static Integer16Decoder integer16Decoder = new Integer16Decoder();
    protected static UInteger8Decoder uInteger8Decoder = new UInteger8Decoder();
    protected static UInteger16Decoder uInteger16Decoder = new UInteger16Decoder();
    protected static UInteger32Decoder uInteger32Decoder = new UInteger32Decoder();
    protected static UInteger64Decoder uInteger64Decoder = new UInteger64Decoder();

    public static byte[] binaryType(final byte[] datagram, int byteOffset, int length) {
        return binaryDecoder.decode(datagram, byteOffset, length);
    }

    public static char characterType(final byte[] datagram, int byteOffset) {
        return characterDecoder.decode(datagram, byteOffset, EndianPolicy.LITTLE);
    }

    public static char characterType(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        return characterDecoder.decode(datagram, byteOffset, policy);
    }

    public static boolean booleanType(final byte[] datagram, int byteOffset, int bitOffset) {
        return booleanDecoder.decode(datagram, byteOffset, bitOffset);
    }

    public static int integer8Type(final byte[] datagram, int byteOffset) {
        return integer8Decoder.decode(datagram, byteOffset);
    }

    public static int uInteger8Type(final byte[] datagram, int byteOffset) {
        return uInteger8Decoder.decode(datagram, byteOffset);
    }

    public static byte byteType(final byte[] datagram, int byteOffset) {
        return byteDecoder.decode(datagram, byteOffset);
    }

    public static int integer16Type(final byte[] datagram, int byteOffset) {
        return integer16Type(datagram, byteOffset, EndianPolicy.LITTLE);
    }

    public static int integer16Type(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        return integer16Decoder.decode(datagram, byteOffset, policy);
    }

    public static int uInteger16Type(final byte[] datagram, int byteOffset) {
        return uInteger16Type(datagram, byteOffset, EndianPolicy.LITTLE);
    }

    public static int uInteger16Type(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        return uInteger16Decoder.decode(datagram, byteOffset, policy);
    }

    public static short shortType(final byte[] datagram, int byteOffset) {
        return shortType(datagram, byteOffset, EndianPolicy.LITTLE);
    }

    public static short shortType(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        return shortDecoder.decode(datagram, byteOffset, policy);
    }

    public static long uInteger32Type(final byte[] datagram, int byteOffset) {
        return uInteger32Type(datagram, byteOffset, EndianPolicy.LITTLE);
    }

    public static long uInteger32Type(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        return uInteger32Decoder.decode(datagram, byteOffset, policy);
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

    public static BigInteger uInteger64Type(final byte[] datagram, int byteOffset) {
        return uInteger64Type(datagram, byteOffset, EndianPolicy.LITTLE);
    }

    public static BigInteger uInteger64Type(final byte[] datagram, int byteOffset, EndianPolicy policy) {
        return uInteger64Decoder.decode(datagram, byteOffset, policy);
    }
}
