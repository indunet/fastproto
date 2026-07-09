package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Duration;
import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Ros2FastProtoBuiltinInterfacesMsgsTest {
    @Test
    void testTimeExactBytes() {
        Time time = Time.builder().sec(12).nanosec(345).build();
        byte[] bytes = time.encode();

        assertArrayEquals(new byte[]{
                0x00, 0x01, 0x00, 0x00,
                0x0c, 0x00, 0x00, 0x00,
                0x59, 0x01, 0x00, 0x00
        }, bytes);
        assertEquals(time, Time.decode(bytes));
    }

    @Test
    void testDurationExactBytes() {
        Duration duration = Duration.builder().sec(-2).nanosec(300_000_000L).build();
        byte[] bytes = duration.encode();

        assertArrayEquals(new byte[]{
                0x00, 0x01, 0x00, 0x00,
                (byte) 0xfe, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                0x00, (byte) 0xa3, (byte) 0xe1, 0x11
        }, bytes);
        assertEquals(duration, Duration.decode(bytes));
    }
}
