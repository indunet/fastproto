package org.indunet.fastproto;

import lombok.val;
import org.indunet.fastproto.annotation.type.UInteger64Type;
import org.indunet.fastproto.compress.DeflateCompressor;
import org.indunet.fastproto.iot.Everything;
import org.indunet.fastproto.iot.Weather;
import org.indunet.fastproto.iot.tesla.Battery;
import org.indunet.fastproto.iot.tesla.Motor;
import org.indunet.fastproto.iot.tesla.Tesla;
import org.indunet.fastproto.util.EncodeUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        byte[] datagram = new byte[26];
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

        // Test decode.
        assertEquals(
                FastProto.parseFrom(datagram, Weather.class, false).toString(), metrics.toString());

        // Test encode.
        byte[] cache = FastProto.toByteArray(metrics, 26, false);
        assertArrayEquals(cache, datagram);
    }

    @Test
    public void testWeather2() {
        byte[] datagram = new byte[26];
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
        val compressor = new DeflateCompressor(2);

        // Init datagram.
        EncodeUtils.uInteger8Type(datagram, 0, weather.getId());
        EncodeUtils.type(datagram, 2, weather.getTime().getTime());
        EncodeUtils.uInteger16Type(datagram, 10, weather.getHumidity());
        EncodeUtils.integer16Type(datagram, 12, weather.getTemperature());
        EncodeUtils.uInteger32Type(datagram, 14, weather.getPressure());
        EncodeUtils.type(datagram, 18, 0, weather.isHumidityValid());
        EncodeUtils.type(datagram, 18, 1, weather.isTemperatureValid());
        EncodeUtils.type(datagram, 18, 2, weather.isPressureValid());

        // Compress the datagram.
        datagram = compressor.compress(datagram);

        // Test decode.
        assertEquals(
                FastProto.parseFrom(datagram, Weather.class).toString(), weather.toString());

        // Test encode.
        byte[] cache = FastProto.toByteArray(weather, 26);
        assertArrayEquals(cache, datagram);
    }

    @Test
    public void testEverything() {
        byte[] datagram = new byte[78];
        Everything everything = Everything.builder()
                .aBoolean(true)
                .aByte((byte) -12)
                .aByteArray(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
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

        // There is a formula.
        EncodeUtils.uInteger8Type(datagram, 66, (int) (everything.getSpeed() * 10));

        // Test decode.
        assertEquals(FastProto.parseFrom(datagram, Everything.class, false).toString(), everything.toString());

        // Test encode.
        byte[] cache = FastProto.toByteArray(everything, 78, false);
        assertArrayEquals(cache, datagram);


        // Test with gzip
        byte[] compressed = FastProto.toByteArray(everything, 78);
        assertEquals(FastProto.parseFrom(compressed, Everything.class).toString(), everything.toString());
    }
}
