package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.std_msgs.msg.Bool;
import org.indunet.fastproto.ros2.std_msgs.msg.Char;
import org.indunet.fastproto.ros2.std_msgs.msg.ColorRGBA;
import org.indunet.fastproto.ros2.std_msgs.msg.Float32;
import org.indunet.fastproto.ros2.std_msgs.msg.Float32MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.Float64;
import org.indunet.fastproto.ros2.std_msgs.msg.Float64MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;
import org.indunet.fastproto.ros2.std_msgs.msg.Empty;
import org.indunet.fastproto.ros2.std_msgs.msg.Int8;
import org.indunet.fastproto.ros2.std_msgs.msg.Int8MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.Int16;
import org.indunet.fastproto.ros2.std_msgs.msg.Int16MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.Int32;
import org.indunet.fastproto.ros2.std_msgs.msg.Int32MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.Int64;
import org.indunet.fastproto.ros2.std_msgs.msg.Int64MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.MultiArrayDimension;
import org.indunet.fastproto.ros2.std_msgs.msg.MultiArrayLayout;
import org.indunet.fastproto.ros2.std_msgs.msg.UInt16;
import org.indunet.fastproto.ros2.std_msgs.msg.UInt16MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.UInt32;
import org.indunet.fastproto.ros2.std_msgs.msg.UInt32MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.UInt64;
import org.indunet.fastproto.ros2.std_msgs.msg.UInt64MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.UInt8;
import org.indunet.fastproto.ros2.std_msgs.msg.UInt8MultiArray;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Ros2FastProtoStdMsgsTest {
    @Test
    void testHeaderExactBytes() {
        Header header = Header.builder()
                .stamp(Time.builder().sec(42).nanosec(123).build())
                .frameId("map")
                .build();

        byte[] bytes = header.encode();

        assertArrayEquals(new byte[]{
                0x00, 0x01, 0x00, 0x00,
                0x2a, 0x00, 0x00, 0x00,
                0x7b, 0x00, 0x00, 0x00,
                0x04, 0x00, 0x00, 0x00,
                0x6d, 0x61, 0x70, 0x00
        }, bytes);
        assertEquals(header, Header.decode(bytes));
    }

    @Test
    void testHeaderStringAlignment() {
        Header header = Header.builder()
                .stamp(Time.builder().sec(1).nanosec(2).build())
                .frameId("odom")
                .build();

        byte[] bytes = header.encode();
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        assertEquals(1, buffer.getInt(4));
        assertEquals(2L, Integer.toUnsignedLong(buffer.getInt(8)));
        assertEquals(5L, Integer.toUnsignedLong(buffer.getInt(12)));
        assertEquals('o', bytes[16]);
        assertEquals(0, bytes[20]);
        assertEquals(header, Header.decode(bytes));
    }

    @Test
    void testEmptyRoundTrip() {
        Empty value = Empty.builder().build();
        assertEquals(value, Empty.decode(value.encode()));
    }

    @Test
    void testBoolRoundTrip() {
        Bool value = Bool.builder().data(true).build();
        assertEquals(value, Bool.decode(value.encode()));
    }

    @Test
    void testByteRoundTrip() {
        org.indunet.fastproto.ros2.std_msgs.msg.Byte value =
                org.indunet.fastproto.ros2.std_msgs.msg.Byte.builder().data(-12).build();
        assertEquals(value, org.indunet.fastproto.ros2.std_msgs.msg.Byte.decode(value.encode()));
    }

    @Test
    void testCharRoundTrip() {
        Char value = Char.builder().data(65).build();
        assertEquals(value, Char.decode(value.encode()));
    }

    @Test
    void testInt8RoundTrip() {
        Int8 value = Int8.builder().data(-42).build();
        assertEquals(value, Int8.decode(value.encode()));
    }

    @Test
    void testInt16RoundTrip() {
        Int16 value = Int16.builder().data(-1024).build();
        assertEquals(value, Int16.decode(value.encode()));
    }

    @Test
    void testInt32RoundTrip() {
        Int32 value = Int32.builder().data(-123456).build();
        assertEquals(value, Int32.decode(value.encode()));
    }

    @Test
    void testInt64RoundTrip() {
        Int64 value = Int64.builder().data(-9_876_543_210L).build();
        assertEquals(value, Int64.decode(value.encode()));
    }

    @Test
    void testUInt16RoundTrip() {
        UInt16 value = UInt16.builder().data(65000).build();
        assertEquals(value, UInt16.decode(value.encode()));
    }

    @Test
    void testUInt8RoundTrip() {
        UInt8 value = UInt8.builder().data(250).build();
        assertEquals(value, UInt8.decode(value.encode()));
    }

    @Test
    void testUInt32RoundTrip() {
        UInt32 value = UInt32.builder().data(4_000_000_000L).build();
        assertEquals(value, UInt32.decode(value.encode()));
    }

    @Test
    void testUInt64RoundTrip() {
        UInt64 value = UInt64.builder().data(new BigInteger("18446744073709551615")).build();
        assertEquals(value, UInt64.decode(value.encode()));
    }

    @Test
    void testFloat32RoundTrip() {
        Float32 value = Float32.builder().data(12.5f).build();
        assertEquals(value, Float32.decode(value.encode()));
    }

    @Test
    void testFloat64RoundTrip() {
        Float64 value = Float64.builder().data(-98.7654321).build();
        assertEquals(value, Float64.decode(value.encode()));
    }

    @Test
    void testStringRoundTrip() {
        org.indunet.fastproto.ros2.std_msgs.msg.String value =
                org.indunet.fastproto.ros2.std_msgs.msg.String.builder().data("hello ros2").build();
        assertEquals(value, org.indunet.fastproto.ros2.std_msgs.msg.String.decode(value.encode()));
    }

    @Test
    void testColorRgbARoundTrip() {
        ColorRGBA color = ColorRGBA.builder().r(0.2f).g(0.4f).b(0.6f).a(0.8f).build();
        byte[] bytes = color.encode();

        assertEquals(20, bytes.length);
        assertEquals(color, ColorRGBA.decode(bytes));
    }

    @Test
    void testFloat32MultiArrayRoundTrip() {
        MultiArrayLayout layout = MultiArrayLayout.builder()
                .dim(new MultiArrayDimension[]{
                        MultiArrayDimension.builder().label("rows").size(2).stride(6).build(),
                        MultiArrayDimension.builder().label("cols").size(3).stride(3).build()
                })
                .dataOffset(0)
                .build();
        Float32MultiArray float32MultiArray = Float32MultiArray.builder()
                .layout(layout)
                .data(new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f})
                .build();

        assertEquals(float32MultiArray, Float32MultiArray.decode(float32MultiArray.encode()));
    }

    @Test
    void testFloat64MultiArrayRoundTrip() {
        MultiArrayLayout layout = MultiArrayLayout.builder()
                .dim(new MultiArrayDimension[]{
                        MultiArrayDimension.builder().label("rows").size(2).stride(6).build(),
                        MultiArrayDimension.builder().label("cols").size(3).stride(3).build()
                })
                .dataOffset(0)
                .build();
        Float64MultiArray float64MultiArray = Float64MultiArray.builder()
                .layout(layout)
                .data(new double[]{0.1, 0.2, 0.3, 0.4})
                .build();

        assertEquals(float64MultiArray, Float64MultiArray.decode(float64MultiArray.encode()));
    }

    @Test
    void testInt32MultiArrayRoundTrip() {
        MultiArrayLayout layout = MultiArrayLayout.builder()
                .dim(new MultiArrayDimension[]{
                        MultiArrayDimension.builder().label("rows").size(2).stride(6).build(),
                        MultiArrayDimension.builder().label("cols").size(3).stride(3).build()
                })
                .dataOffset(0)
                .build();
        Int32MultiArray int32MultiArray = Int32MultiArray.builder()
                .layout(layout)
                .data(new int[]{10, 20, 30, 40, 50, 60})
                .build();

        assertEquals(int32MultiArray, Int32MultiArray.decode(int32MultiArray.encode()));
    }

    @Test
    void testInt16MultiArrayRoundTrip() {
        MultiArrayLayout layout = MultiArrayLayout.builder()
                .dim(new MultiArrayDimension[]{
                        MultiArrayDimension.builder().label("rows").size(2).stride(6).build(),
                        MultiArrayDimension.builder().label("cols").size(3).stride(3).build()
                })
                .dataOffset(0)
                .build();
        Int16MultiArray int16MultiArray = Int16MultiArray.builder()
                .layout(layout)
                .data(new int[]{100, 200, 300, 400})
                .build();

        assertEquals(int16MultiArray, Int16MultiArray.decode(int16MultiArray.encode()));
    }

    @Test
    void testInt8MultiArrayRoundTrip() {
        MultiArrayLayout layout = MultiArrayLayout.builder()
                .dim(new MultiArrayDimension[]{
                        MultiArrayDimension.builder().label("rows").size(2).stride(6).build(),
                        MultiArrayDimension.builder().label("cols").size(3).stride(3).build()
                })
                .dataOffset(0)
                .build();
        Int8MultiArray int8MultiArray = Int8MultiArray.builder()
                .layout(layout)
                .data(new byte[]{-3, -2, -1, 0, 1, 2})
                .build();

        assertEquals(int8MultiArray, Int8MultiArray.decode(int8MultiArray.encode()));
    }

    @Test
    void testInt64MultiArrayRoundTrip() {
        MultiArrayLayout layout = MultiArrayLayout.builder()
                .dim(new MultiArrayDimension[]{
                        MultiArrayDimension.builder().label("rows").size(2).stride(6).build(),
                        MultiArrayDimension.builder().label("cols").size(3).stride(3).build()
                })
                .dataOffset(0)
                .build();
        Int64MultiArray int64MultiArray = Int64MultiArray.builder()
                .layout(layout)
                .data(new long[]{1000L, 2000L, 3000L})
                .build();

        assertEquals(int64MultiArray, Int64MultiArray.decode(int64MultiArray.encode()));
    }

    @Test
    void testUInt16MultiArrayRoundTrip() {
        MultiArrayLayout layout = MultiArrayLayout.builder()
                .dim(new MultiArrayDimension[]{
                        MultiArrayDimension.builder().label("rows").size(2).stride(6).build(),
                        MultiArrayDimension.builder().label("cols").size(3).stride(3).build()
                })
                .dataOffset(0)
                .build();
        UInt16MultiArray uint16MultiArray = UInt16MultiArray.builder()
                .layout(layout)
                .data(new int[]{60000, 61000, 62000})
                .build();

        assertEquals(uint16MultiArray, UInt16MultiArray.decode(uint16MultiArray.encode()));
    }

    @Test
    void testUInt32MultiArrayRoundTrip() {
        MultiArrayLayout layout = MultiArrayLayout.builder()
                .dim(new MultiArrayDimension[]{
                        MultiArrayDimension.builder().label("rows").size(2).stride(6).build(),
                        MultiArrayDimension.builder().label("cols").size(3).stride(3).build()
                })
                .dataOffset(0)
                .build();
        UInt32MultiArray uint32MultiArray = UInt32MultiArray.builder()
                .layout(layout)
                .data(new long[]{3_000_000_000L, 3_500_000_000L})
                .build();

        assertEquals(uint32MultiArray, UInt32MultiArray.decode(uint32MultiArray.encode()));
    }

    @Test
    void testUInt64MultiArrayRoundTrip() {
        MultiArrayLayout layout = MultiArrayLayout.builder()
                .dim(new MultiArrayDimension[]{
                        MultiArrayDimension.builder().label("rows").size(2).stride(6).build(),
                        MultiArrayDimension.builder().label("cols").size(3).stride(3).build()
                })
                .dataOffset(0)
                .build();
        UInt64MultiArray uint64MultiArray = UInt64MultiArray.builder()
                .layout(layout)
                .data(new BigInteger[]{
                        new BigInteger("4294967296"),
                        new BigInteger("18446744073709551615")
                })
                .build();

        assertEquals(uint64MultiArray, UInt64MultiArray.decode(uint64MultiArray.encode()));
    }

    @Test
    void testUInt8MultiArrayRoundTrip() {
        MultiArrayLayout layout = MultiArrayLayout.builder()
                .dim(new MultiArrayDimension[]{
                        MultiArrayDimension.builder().label("rows").size(2).stride(6).build(),
                        MultiArrayDimension.builder().label("cols").size(3).stride(3).build()
                })
                .dataOffset(0)
                .build();
        UInt8MultiArray uint8MultiArray = UInt8MultiArray.builder()
                .layout(layout)
                .data(new byte[]{1, 2, 3, 4, 5, 6})
                .build();

        assertEquals(uint8MultiArray, UInt8MultiArray.decode(uint8MultiArray.encode()));
    }
}
