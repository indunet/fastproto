/*
 * Copyright 2019-2021 indunet.org
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

package org.indunet.fastproto.api;

import lombok.SneakyThrows;
import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.DefaultByteOrder;
import org.indunet.fastproto.annotation.UInt64Type;
import org.indunet.fastproto.domain.Everything;
import org.indunet.fastproto.domain.Sensor;
import org.indunet.fastproto.domain.Weather;
import org.indunet.fastproto.domain.color.Phone;
import org.indunet.fastproto.domain.datagram.StateDatagram;
import org.indunet.fastproto.domain.tesla.Battery;
import org.indunet.fastproto.domain.tesla.Motor;
import org.indunet.fastproto.domain.tesla.Tesla;
import org.indunet.fastproto.util.CodecUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
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
        CodecUtils.int64Type(datagram, 0, ByteOrder.LITTLE, tesla.getId());
        CodecUtils.int64Type(datagram, 8, ByteOrder.LITTLE, tesla.getTime().getTime());
        CodecUtils.floatType(datagram, 16, ByteOrder.LITTLE, tesla.getSpeed());
        CodecUtils.boolType(datagram, 20, 0, BitOrder.LSB_0, tesla.isActive());
        CodecUtils.shortType(datagram, 22, ByteOrder.LITTLE, tesla.getBattery().getCapacity());
        CodecUtils.boolType(datagram, 24, 0, BitOrder.LSB_0, tesla.getBattery().isLocked());
        CodecUtils.int32Type(datagram, 26, ByteOrder.LITTLE, tesla.getBattery().getVoltage());
        CodecUtils.floatType(datagram, 30, ByteOrder.LITTLE, tesla.getBattery().getTemperature());
        CodecUtils.int16Type(datagram, 34, ByteOrder.LITTLE, tesla.getMotor().getVoltage());
        CodecUtils.int32Type(datagram, 36, ByteOrder.LITTLE, tesla.getMotor().getCurrent());
        CodecUtils.floatType(datagram, 40, ByteOrder.LITTLE, tesla.getMotor().getTemperature());

        // Test decode with multi-thread.
        IntStream.range(0, 10).parallel()
                .forEach(__ -> assertEquals(FastProto.decode(datagram, Tesla.class).toString(), tesla.toString()));

        // Test encode with multi-thread.
        IntStream.range(0, 10).parallel()
                .forEach(__ -> {
                    byte[] bytes = FastProto.encode(tesla, 44);
                    assertArrayEquals(datagram, bytes);

                    FastProto.encode(tesla, bytes);
                    assertArrayEquals(datagram, bytes);
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
        CodecUtils.uint8Type(datagram, 0, metrics.getId());
        CodecUtils.int64Type(datagram, 2, ByteOrder.LITTLE, metrics.getTime().getTime());
        CodecUtils.uint16Type(datagram, 10, ByteOrder.LITTLE, metrics.getHumidity());
        CodecUtils.int16Type(datagram, 12, ByteOrder.LITTLE, metrics.getTemperature());
        CodecUtils.uint32Type(datagram, 14, ByteOrder.LITTLE, metrics.getPressure());
        CodecUtils.boolType(datagram, 18, 0, BitOrder.LSB_0, metrics.isHumidityValid());
        CodecUtils.boolType(datagram, 18, 1, BitOrder.LSB_0, metrics.isTemperatureValid());
        CodecUtils.boolType(datagram, 18, 2, BitOrder.LSB_0, metrics.isPressureValid());

        // Test decode.
        assertEquals(
                FastProto.decode(datagram, Weather.class).toString(), metrics.toString());

        // Test encode.
        byte[] cache = FastProto.encode(metrics, 30);
        assertArrayEquals(datagram, cache);
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

        // Init datagram.
        CodecUtils.uint8Type(datagram, 0, weather.getId());
        CodecUtils.int64Type(datagram, 2, ByteOrder.LITTLE, weather.getTime().getTime());
        CodecUtils.uint16Type(datagram, 10, ByteOrder.LITTLE, weather.getHumidity());
        CodecUtils.int16Type(datagram, 12, ByteOrder.LITTLE, weather.getTemperature());
        CodecUtils.uint32Type(datagram, 14, ByteOrder.LITTLE, weather.getPressure());
        CodecUtils.boolType(datagram, 18, 0, BitOrder.LSB_0, weather.isHumidityValid());
        CodecUtils.boolType(datagram, 18, 1, BitOrder.LSB_0, weather.isTemperatureValid());
        CodecUtils.boolType(datagram, 18, 2, BitOrder.LSB_0, weather.isPressureValid());

        // Test decode.
        assertEquals(
                FastProto.decode(datagram, Weather.class).toString(), weather.toString());

        // Test encode.
        byte[] cache = FastProto.encode(weather, 23);
        assertArrayEquals(cache, datagram);
    }

    @Test
    public void testEverything() {
        byte[] datagram = new byte[102];
        long millis = System.currentTimeMillis();
        val calendar = Calendar.getInstance();

        calendar.setTimeInMillis(millis);
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
                .aLong(-50000)
                .aShort((short) 12)
                .aString("abcABC")
                .aTimestamp(new Timestamp(millis))
                .date(new Date(millis))
                .calendar(calendar)
                .aUInteger8(8)
                .aUInteger16(16)
                .aUInteger32(32L)
                .speed(10.1f)
                .instant(Instant.ofEpochMilli(millis))
                .aUInteger64(new BigInteger(String.valueOf(UInt64Type.MAX_VALUE)))
                .build();

        // Init datagram.
        CodecUtils.boolType(datagram, 0, 1, BitOrder.LSB_0, everything.getABoolean());
        CodecUtils.byteType(datagram, 1, everything.getAByte());
        CodecUtils.shortType(datagram, 2, ByteOrder.LITTLE, everything.getAShort());
        CodecUtils.int32Type(datagram, 4, ByteOrder.LITTLE, everything.getAInteger());
        CodecUtils.int64Type(datagram, 8, ByteOrder.LITTLE, everything.getALong());
        CodecUtils.floatType(datagram, 16, ByteOrder.LITTLE, everything.getAFloat());
        CodecUtils.doubleType(datagram, 20, ByteOrder.LITTLE, everything.getADouble());
        CodecUtils.int8Type(datagram, 28, everything.getAInteger8());
        CodecUtils.int16Type(datagram, 30, ByteOrder.LITTLE, everything.getAInteger16());
        CodecUtils.uint8Type(datagram, 32, everything.getAUInteger8());
        CodecUtils.uint16Type(datagram, 34, ByteOrder.LITTLE, everything.getAUInteger16());
        CodecUtils.uint32Type(datagram, 36, ByteOrder.LITTLE, everything.getAUInteger32());
        CodecUtils.binaryType(datagram, 40, -1, everything.getAByteArray());
        CodecUtils.binaryType(datagram, 50, -1, everything.getAString().getBytes());
        CodecUtils.int64Type(datagram, 56, ByteOrder.LITTLE, everything.getATimestamp().getTime());
        CodecUtils.int8Type(datagram, 64, everything.getACharacter());
        CodecUtils.uint64Type(datagram, 70, ByteOrder.LITTLE, everything.getAUInteger64());
        CodecUtils.int64Type(datagram, 78, ByteOrder.LITTLE, millis);
        CodecUtils.int64Type(datagram, 86, ByteOrder.LITTLE, millis);
        CodecUtils.int64Type(datagram, 94, ByteOrder.LITTLE, millis);

        // There is a formula.
        CodecUtils.uint8Type(datagram, 66, (int) (everything.getSpeed() * 10));

        // Test with gzip
        byte[] compressed = FastProto.encode(everything, 103);
        assertEquals(FastProto.decode(compressed, Everything.class).toString(), everything.toString());
    }

    @Test
    public void testMotor() {
        Motor motor = Motor.builder()
                .temperature(26.6f)
                .current(12)
                .voltage((short) 24)
                .build();

        byte[] datagram = FastProto.encode(motor);
        assertEquals(44, datagram.length);
        assertEquals(motor.toString(), FastProto.decode(datagram, Motor.class).toString());
    }

    @Test
    public void testSensor() {
        val sensor = new Sensor(10, 11);
        val datagram = FastProto.encode(sensor, 10);

        assertEquals(10, datagram.length);
    }

    @Test
    @SneakyThrows
    public void testStateDatagram() {
        byte[] datagram = new byte[600];

        StateDatagram stateDatagram = FastProto.decode(datagram, StateDatagram.class);
        assertNotNull(stateDatagram);
    }

    @Test
    public void testNonObject() {
        val datagram = new byte[10];
        val nonObject = FastProto.decode(datagram, NonObject.class);

        assertNotNull(nonObject);

        val bytes = FastProto.encode(nonObject, 10);

        assertNotNull(bytes);
    }

    @SneakyThrows
    @Test
    public void testColor() {
        val datagram = Phone.getDatagram();
        val phone = Phone.getDefault();

        assertEquals(phone.toString(), FastProto.decode(datagram, Phone.class).toString());
        assertArrayEquals(datagram, FastProto.encode(phone, Phone.getLength()));
    }

    @DefaultByteOrder(ByteOrder.BIG)
    public static class NonObject {

    }
}
