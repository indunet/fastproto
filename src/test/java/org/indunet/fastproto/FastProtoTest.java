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

package org.indunet.fastproto;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;
import org.indunet.fastproto.annotation.EnableChecksum;
import org.indunet.fastproto.annotation.EnableCrypto;
import org.indunet.fastproto.annotation.EnableProtocolVersion;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.type.ArrayType;
import org.indunet.fastproto.annotation.type.UInteger64Type;
import org.indunet.fastproto.checksum.Crc32Checker;
import org.indunet.fastproto.compress.DeflateCompressor;
import org.indunet.fastproto.crypto.Crypto;
import org.indunet.fastproto.crypto.CryptoPolicy;
import org.indunet.fastproto.exception.CheckSumException;
import org.indunet.fastproto.exception.CryptoException;
import org.indunet.fastproto.exception.ProtocolVersionException;
import org.indunet.fastproto.iot.Everything;
import org.indunet.fastproto.iot.Weather;
import org.indunet.fastproto.iot.color.Phone;
import org.indunet.fastproto.iot.datagram.StateDatagram;
import org.indunet.fastproto.iot.tesla.Battery;
import org.indunet.fastproto.iot.tesla.Motor;
import org.indunet.fastproto.iot.tesla.Tesla;
import org.indunet.fastproto.util.EncodeUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Deng Ran
 * @see FastProto
 * @since 1.0.0
 */
public class FastProtoTest {
    @Test
    public void testTesla() {
        byte[] datagram = new byte[44];
        Tesla tesla = Tesla.builder()
                .id(101L)
                .time(new Timestamp(System.currentTimeMillis()))
                .active(true)
                .speed(78.9f)
                .battery(Battery.builder()
                        .capacity((short) 90)
                        .locked(false)
                        .temperature(15.6f)
                        .voltage(24)
                        .build())
                .motor(Motor.builder()
                        .current(5)
                        .temperature(38.2f)
                        .voltage((short) 12)
                        .build())
                .build();

        // Init datagram.
        EncodeUtils.type(datagram, 0, tesla.getId());
        EncodeUtils.type(datagram, 8, tesla.getTime().getTime());
        EncodeUtils.type(datagram, 16, tesla.getSpeed());
        EncodeUtils.type(datagram, 20, 0, tesla.isActive());
        EncodeUtils.type(datagram, 22, tesla.getBattery().getCapacity());
        EncodeUtils.type(datagram, 24, 0, tesla.getBattery().isLocked());
        EncodeUtils.type(datagram, 26, tesla.getBattery().getVoltage());
        EncodeUtils.type(datagram, 30, tesla.getBattery().getTemperature());
        EncodeUtils.type(datagram, 34, tesla.getMotor().getVoltage());
        EncodeUtils.type(datagram, 36, tesla.getMotor().getCurrent());
        EncodeUtils.type(datagram, 40, tesla.getMotor().getTemperature());

        // Test decode with multi-thread.
        IntStream.range(0, 10).parallel()
                .forEach(__ -> {
                    assertEquals(FastProto.parseFrom(datagram, Tesla.class).toString(), tesla.toString());
                });

        // Test encode with multi-thread.
        IntStream.range(0, 10).parallel()
                .forEach(__ -> {
                    byte[] cache = FastProto.toByteArray(tesla, 44);
                    assertArrayEquals(cache, datagram);
                });
    }

    @Test
    public void testWeather1() {
        byte[] datagram = new byte[30];
        Weather metrics = Weather.builder()
                .id(101)
                .time(new Timestamp(System.currentTimeMillis()))
                .humidity(85)
                .temperature(-15)
                .pressure(13)
                .humidityValid(true)
                .temperatureValid(true)
                .pressureValid(true)
                .build();

        // Init datagram.
        EncodeUtils.uInteger8Type(datagram, 0, metrics.getId());
        EncodeUtils.type(datagram, 2, metrics.getTime().getTime());
        EncodeUtils.uInteger16Type(datagram, 10, metrics.getHumidity());
        EncodeUtils.integer16Type(datagram, 12, metrics.getTemperature());
        EncodeUtils.uInteger32Type(datagram, 14, metrics.getPressure());
        EncodeUtils.type(datagram, 18, 0, metrics.isHumidityValid());
        EncodeUtils.type(datagram, 18, 1, metrics.isTemperatureValid());
        EncodeUtils.type(datagram, 18, 2, metrics.isPressureValid());
        EncodeUtils.uInteger32Type(datagram, 26, EndianPolicy.BIG, Crc32Checker.getInstance().getValue(datagram, 0, -5));

        // Test decode.
        assertEquals(
                FastProto.parseFrom(datagram, Weather.class, CodecFeature.DISABLE_COMPRESS).toString(), metrics.toString());

        // Test encode.
        byte[] cache = FastProto.toByteArray(metrics, 30, CodecFeature.DISABLE_COMPRESS);
        assertArrayEquals(cache, datagram);
    }

    @Test
    public void testWeather2() {
        byte[] datagram = new byte[23];
        Weather weather = Weather.builder()
                .id(101)
                .time(new Timestamp(System.currentTimeMillis()))
                .humidity(85)
                .temperature(-15)
                .pressure(13)
                .humidityValid(true)
                .temperatureValid(true)
                .pressureValid(true)
                .build();
        val compressor = DeflateCompressor.getInstance(2);

        // Init datagram.
        EncodeUtils.uInteger8Type(datagram, 0, weather.getId());
        EncodeUtils.type(datagram, 2, weather.getTime().getTime());
        EncodeUtils.uInteger16Type(datagram, 10, weather.getHumidity());
        EncodeUtils.integer16Type(datagram, 12, weather.getTemperature());
        EncodeUtils.uInteger32Type(datagram, 14, weather.getPressure());
        EncodeUtils.type(datagram, 18, 0, weather.isHumidityValid());
        EncodeUtils.type(datagram, 18, 1, weather.isTemperatureValid());
        EncodeUtils.type(datagram, 18, 2, weather.isPressureValid());
        EncodeUtils.uInteger32Type(datagram, 19, EndianPolicy.BIG, Crc32Checker.getInstance().getValue(datagram, 0, -5));

        // Compress the datagram.
        datagram = compressor.compress(datagram);

        // Test decode.
        assertEquals(
                FastProto.parseFrom(datagram, Weather.class).toString(), weather.toString());
        assertEquals(
                FastProto.parseFrom(datagram, Weather.class, CodecFeature.DEFAULT).toString(), weather.toString());

        // Test encode.
        byte[] cache = FastProto.toByteArray(weather);
        assertArrayEquals(cache, datagram);
    }

    @Test
    public void testEverything() {
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
                .aLong(-50000L)
                .aShort((short) 12)
                .aString("abcABC")
                .aTimestamp(new Timestamp(System.currentTimeMillis()))
                .aUInteger8(8)
                .aUInteger16(16)
                .aUInteger32(32L)
                .speed(10.1f)
                .aUInteger64(new BigInteger(String.valueOf(UInteger64Type.MAX_VALUE)))
                .build();

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

        // There is a formula.
        EncodeUtils.uInteger8Type(datagram, 66, (int) (everything.getSpeed() * 10));

        val crypto = Crypto.getInstance(CryptoPolicy.AES_ECB_PKCS5PADDING);
        val afterEncrypted = crypto.encrypt("330926".getBytes(StandardCharsets.UTF_8), datagram);

        // Test decode.
        assertEquals(FastProto.parseFrom(afterEncrypted, Everything.class, CodecFeature.DISABLE_COMPRESS).toString(), everything.toString());

        // Test encode.
        byte[] cache = FastProto.toByteArray(everything, CodecFeature.DISABLE_COMPRESS);
        assertArrayEquals(cache, afterEncrypted);

        // Test with gzip
        byte[] compressed = FastProto.toByteArray(everything, 80, CodecFeature.DISABLE_CRYPTO);
        assertEquals(FastProto.parseFrom(compressed, Everything.class, CodecFeature.DISABLE_CRYPTO).toString(), everything.toString());
    }

    @Test
    public void testMotor() {
        Motor motor = Motor.builder()
                .temperature(26.6f)
                .current(12)
                .voltage((short) 24)
                .build();

        byte[] datagram = FastProto.toByteArray(motor);
        assertEquals(44, datagram.length);
        assertEquals(motor.toString(), FastProto.parseFrom(datagram, Motor.class).toString());
    }

    @Test
    @SneakyThrows
    public void testStateDatagram() {
        byte[] datagram = new byte[600];

        StateDatagram stateDatagram = FastProto.parseFrom(datagram, StateDatagram.class);
        assertNotNull(stateDatagram);
    }

    @Endian(EndianPolicy.BIG)
    public static class NonObject {

    }

    @Test
    public void testNonObject() {
        val datagram = new byte[10];
        val nonObject = FastProto.parseFrom(datagram, NonObject.class);

        assertNotNull(nonObject);

        val bytes = FastProto.toByteArray(nonObject, 10);

        assertNotNull(bytes);
    }

    @EnableChecksum
    public static class ChecksumObject {

    }

    @Test
    public void testChecksumException() {
        val datagram = new byte[] {0, 1, 2, 3, 4, 5, 6, 7};

        assertThrows(CheckSumException.class, () -> FastProto.parseFrom(datagram, ChecksumObject.class));
    }

    @EnableProtocolVersion(value = 2, version = 10)
    public static class ProtocolVersionObject {

    }

    @Test
    public void testProtocolVersionException() {
        val datagram = new byte[] {0, 1, 2, 3, 4, 5, 6, 7};

        assertThrows(ProtocolVersionException.class, () -> FastProto.parseFrom(datagram, ProtocolVersionObject.class));
    }

    @EnableCrypto(key = "330926")
    public static class CryptoObject {

    }

    @Test
    public void testCryptoException() {
        val datagram = new byte[100];

        assertThrows(CryptoException.class, () -> FastProto.parseFrom(datagram, CryptoObject.class));
    }

    @SneakyThrows
    @Test
    public void testColor() {
        val datagram = Phone.getDatagram();
        val phone = Phone.getDefault();

         assertEquals(phone.toString(), FastProto.parseFrom(datagram, Phone.class).toString());
         assertArrayEquals(datagram, FastProto.toByteArray(phone, Phone.getLength()));
    }

    @Data
    public static class ArrayObject {
        @ArrayType(value = 0, length = 5, protocolType = ProtocolType.UINTEGER16)
        Integer[] ints;
    }

    @Test
    public void testArray() {
        val datagram = new byte[10];

        datagram[0] = 1;
        datagram[2] = 2;
        datagram[4] = 3;
        datagram[6] = 4;
        datagram[8] = 5;

        val expected = new ArrayObject();
        expected.setInts(new Integer[] {1, 2, 3, 4, 5});

        assertEquals(expected.toString(), FastProto.parseFrom(datagram, ArrayObject.class).toString());
        val bytes = FastProto.toByteArray(expected, 10);
        assertArrayEquals(datagram, FastProto.toByteArray(expected, 10));
    }
}
