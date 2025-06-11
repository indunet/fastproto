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
import org.indunet.fastproto.domain.color.Phone;
import org.indunet.fastproto.domain.datagram.StateDatagram;
import org.indunet.fastproto.domain.tesla.Battery;
import org.indunet.fastproto.domain.tesla.Motor;
import org.indunet.fastproto.domain.tesla.Tesla;
import org.indunet.fastproto.util.EncodeUtils;
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
        EncodeUtils.writeInt64(datagram, 0, ByteOrder.LITTLE, tesla.getId());
        EncodeUtils.writeInt64(datagram, 8, ByteOrder.LITTLE, tesla.getTime().getTime());
        EncodeUtils.writeFloat(datagram, 16, ByteOrder.LITTLE, tesla.getSpeed());
        EncodeUtils.writeBool(datagram, 20, 0, BitOrder.LSB_0, tesla.isActive());
        EncodeUtils.writeShort(datagram, 22, ByteOrder.LITTLE, tesla.getBattery().getCapacity());
        EncodeUtils.writeBool(datagram, 24, 0, BitOrder.LSB_0, tesla.getBattery().isLocked());
        EncodeUtils.writeInt32(datagram, 26, ByteOrder.LITTLE, tesla.getBattery().getVoltage());
        EncodeUtils.writeFloat(datagram, 30, ByteOrder.LITTLE, tesla.getBattery().getTemperature());
        EncodeUtils.writeInt16(datagram, 34, ByteOrder.LITTLE, tesla.getMotor().getVoltage());
        EncodeUtils.writeInt32(datagram, 36, ByteOrder.LITTLE, tesla.getMotor().getCurrent());
        EncodeUtils.writeFloat(datagram, 40, ByteOrder.LITTLE, tesla.getMotor().getTemperature());

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
        EncodeUtils.writeBool(datagram, 0, 1, BitOrder.LSB_0, everything.getABoolean());
        EncodeUtils.writeByte(datagram, 1, everything.getAByte());
        EncodeUtils.writeShort(datagram, 2, ByteOrder.LITTLE, everything.getAShort());
        EncodeUtils.writeInt32(datagram, 4, ByteOrder.LITTLE, everything.getAInteger());
        EncodeUtils.writeInt64(datagram, 8, ByteOrder.LITTLE, everything.getALong());
        EncodeUtils.writeFloat(datagram, 16, ByteOrder.LITTLE, everything.getAFloat());
        EncodeUtils.writeDouble(datagram, 20, ByteOrder.LITTLE, everything.getADouble());
        EncodeUtils.writeInt8(datagram, 28, everything.getAInteger8());
        EncodeUtils.writeInt16(datagram, 30, ByteOrder.LITTLE, everything.getAInteger16());
        EncodeUtils.writeUInt8(datagram, 32, everything.getAUInteger8());
        EncodeUtils.writeUInt16(datagram, 34, ByteOrder.LITTLE, everything.getAUInteger16());
        EncodeUtils.writeUInt32(datagram, 36, ByteOrder.LITTLE, everything.getAUInteger32());
        EncodeUtils.writeBytes(datagram, 40, everything.getAByteArray());
        EncodeUtils.writeBytes(datagram, 50, everything.getAString().getBytes());
        EncodeUtils.writeInt64(datagram, 56, ByteOrder.LITTLE, everything.getATimestamp().getTime());
        EncodeUtils.writeInt8(datagram, 64, everything.getACharacter());
        EncodeUtils.writeUInt64(datagram, 70, ByteOrder.LITTLE, everything.getAUInteger64());
        EncodeUtils.writeInt64(datagram, 78, ByteOrder.LITTLE, millis);
        EncodeUtils.writeInt64(datagram, 86, ByteOrder.LITTLE, millis);
        EncodeUtils.writeInt64(datagram, 94, ByteOrder.LITTLE, millis);

        // There is a formula.
        EncodeUtils.writeUInt8(datagram, 66, (int) (everything.getSpeed() * 10));

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
