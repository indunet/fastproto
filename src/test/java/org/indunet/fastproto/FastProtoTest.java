package org.indunet.fastproto;

import org.indunet.fastproto.tesla.Battery;
import org.indunet.fastproto.tesla.Motor;
import org.indunet.fastproto.tesla.Tesla;
import org.indunet.fastproto.util.EncodeUtils;
import org.indunet.fastproto.weather.Device;
import org.junit.Test;
import java.sql.Timestamp;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class FastProtoTest {
    @Test
    public void testTesla() {
        byte[] datagram = new byte[44];
        Tesla tesla = Tesla.builder()
                .id(101)
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

        // Test decode.
        assertEquals(FastProto.decode(datagram, Tesla.class).toString(), tesla.toString());
        // System.out.println(FastProto.decode(datagram, Tesla.class));

        // Test encode.
        byte[] cache = new byte[44];
        FastProto.encode(tesla, cache);
        assertArrayEquals(cache, datagram);
    }

    @Test
    public void testWeather() {
        byte[] datagram = new byte[26];
        Device device = Device.builder()
                .id((short) 101)
                .time(new Timestamp(System.currentTimeMillis()))
                .humidity(85)
                .temperature(25.2f)
                .pressure(7.5)
                .build();

        // Init datagram.
        EncodeUtils.type(datagram, 0, device.getId());
        EncodeUtils.type(datagram, 2, device.getTime().getTime());
        EncodeUtils.type(datagram, 10, device.getHumidity());
        EncodeUtils.type(datagram, 14, device.getTemperature());
        EncodeUtils.type(datagram, 18, device.getPressure());

        // Test decode.
        assertEquals(FastProto.decode(datagram, Device.class).toString(), device.toString());

        // Test encode.
        byte[] cache = new byte[26];
        FastProto.encode(device, cache);
        assertArrayEquals(cache, datagram);
    }
}
