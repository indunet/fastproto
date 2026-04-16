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

        byte[] bytes = Ros2FastProto.encode(header, Ros2Codecs.HEADER);

        assertArrayEquals(new byte[]{
                0x00, 0x01, 0x00, 0x00,
                0x2a, 0x00, 0x00, 0x00,
                0x7b, 0x00, 0x00, 0x00,
                0x04, 0x00, 0x00, 0x00,
                0x6d, 0x61, 0x70, 0x00
        }, bytes);
        assertEquals(header, Ros2FastProto.decode(bytes, Ros2Codecs.HEADER));
    }

    @Test
    void testHeaderStringAlignment() {
        Header header = Header.builder()
                .stamp(Time.builder().sec(1).nanosec(2).build())
                .frameId("odom")
                .build();

        byte[] bytes = Ros2FastProto.encode(header, Ros2Codecs.HEADER);
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        assertEquals(1, buffer.getInt(4));
        assertEquals(2L, Integer.toUnsignedLong(buffer.getInt(8)));
        assertEquals(5L, Integer.toUnsignedLong(buffer.getInt(12)));
        assertEquals('o', bytes[16]);
        assertEquals(0, bytes[20]);
        assertEquals(header, Ros2FastProto.decode(bytes, Ros2Codecs.HEADER));
    }

    @Test
    void testStdScalarMessagesRoundTrip() {
        Empty emptyValue = Empty.builder().build();
        Bool boolValue = Bool.builder().data(true).build();
        org.indunet.fastproto.ros2.std_msgs.msg.Byte byteValue =
                org.indunet.fastproto.ros2.std_msgs.msg.Byte.builder().data(-12).build();
        Char charValue = Char.builder().data(65).build();
        Int8 int8Value = Int8.builder().data(-42).build();
        Int16 int16Value = Int16.builder().data(-1024).build();
        Int32 int32Value = Int32.builder().data(-123456).build();
        Int64 int64Value = Int64.builder().data(-9_876_543_210L).build();
        UInt16 uint16Value = UInt16.builder().data(65000).build();
        UInt8 uint8Value = UInt8.builder().data(250).build();
        UInt32 uint32Value = UInt32.builder().data(4_000_000_000L).build();
        UInt64 uint64Value = UInt64.builder().data(new BigInteger("18446744073709551615")).build();
        Float32 float32Value = Float32.builder().data(12.5f).build();
        Float64 float64Value = Float64.builder().data(-98.7654321).build();
        org.indunet.fastproto.ros2.std_msgs.msg.String stringValue =
                org.indunet.fastproto.ros2.std_msgs.msg.String.builder().data("hello ros2").build();

        assertEquals(emptyValue, Ros2FastProto.decode(Ros2FastProto.encode(emptyValue, Ros2Codecs.EMPTY), Ros2Codecs.EMPTY));
        assertEquals(boolValue, Ros2FastProto.decode(Ros2FastProto.encode(boolValue, Ros2Codecs.BOOL), Ros2Codecs.BOOL));
        assertEquals(byteValue, Ros2FastProto.decode(Ros2FastProto.encode(byteValue, Ros2Codecs.BYTE), Ros2Codecs.BYTE));
        assertEquals(charValue, Ros2FastProto.decode(Ros2FastProto.encode(charValue, Ros2Codecs.CHAR), Ros2Codecs.CHAR));
        assertEquals(int8Value, Ros2FastProto.decode(Ros2FastProto.encode(int8Value, Ros2Codecs.INT8), Ros2Codecs.INT8));
        assertEquals(int16Value, Ros2FastProto.decode(Ros2FastProto.encode(int16Value, Ros2Codecs.INT16), Ros2Codecs.INT16));
        assertEquals(int32Value, Ros2FastProto.decode(Ros2FastProto.encode(int32Value, Ros2Codecs.INT32), Ros2Codecs.INT32));
        assertEquals(int64Value, Ros2FastProto.decode(Ros2FastProto.encode(int64Value, Ros2Codecs.INT64), Ros2Codecs.INT64));
        assertEquals(uint16Value, Ros2FastProto.decode(Ros2FastProto.encode(uint16Value, Ros2Codecs.UINT16), Ros2Codecs.UINT16));
        assertEquals(uint8Value, Ros2FastProto.decode(Ros2FastProto.encode(uint8Value, Ros2Codecs.UINT8), Ros2Codecs.UINT8));
        assertEquals(uint32Value, Ros2FastProto.decode(Ros2FastProto.encode(uint32Value, Ros2Codecs.UINT32), Ros2Codecs.UINT32));
        assertEquals(uint64Value, Ros2FastProto.decode(Ros2FastProto.encode(uint64Value, Ros2Codecs.UINT64), Ros2Codecs.UINT64));
        assertEquals(float32Value, Ros2FastProto.decode(Ros2FastProto.encode(float32Value, Ros2Codecs.FLOAT32), Ros2Codecs.FLOAT32));
        assertEquals(float64Value, Ros2FastProto.decode(Ros2FastProto.encode(float64Value, Ros2Codecs.FLOAT64), Ros2Codecs.FLOAT64));
        assertEquals(stringValue, Ros2FastProto.decode(Ros2FastProto.encode(stringValue, Ros2Codecs.STD_STRING), Ros2Codecs.STD_STRING));
    }

    @Test
    void testColorRgbARoundTrip() {
        ColorRGBA color = ColorRGBA.builder().r(0.2f).g(0.4f).b(0.6f).a(0.8f).build();
        byte[] bytes = Ros2FastProto.encode(color, Ros2Codecs.COLOR_RGBA);

        assertEquals(20, bytes.length);
        assertEquals(color, Ros2FastProto.decode(bytes, Ros2Codecs.COLOR_RGBA));
    }

    @Test
    void testMultiArrayRoundTrip() {
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
        Float64MultiArray float64MultiArray = Float64MultiArray.builder()
                .layout(layout)
                .data(new double[]{0.1, 0.2, 0.3, 0.4})
                .build();
        Int32MultiArray int32MultiArray = Int32MultiArray.builder()
                .layout(layout)
                .data(new int[]{10, 20, 30, 40, 50, 60})
                .build();
        Int16MultiArray int16MultiArray = Int16MultiArray.builder()
                .layout(layout)
                .data(new int[]{100, 200, 300, 400})
                .build();
        Int8MultiArray int8MultiArray = Int8MultiArray.builder()
                .layout(layout)
                .data(new byte[]{-3, -2, -1, 0, 1, 2})
                .build();
        Int64MultiArray int64MultiArray = Int64MultiArray.builder()
                .layout(layout)
                .data(new long[]{1000L, 2000L, 3000L})
                .build();
        UInt16MultiArray uint16MultiArray = UInt16MultiArray.builder()
                .layout(layout)
                .data(new int[]{60000, 61000, 62000})
                .build();
        UInt32MultiArray uint32MultiArray = UInt32MultiArray.builder()
                .layout(layout)
                .data(new long[]{3_000_000_000L, 3_500_000_000L})
                .build();
        UInt64MultiArray uint64MultiArray = UInt64MultiArray.builder()
                .layout(layout)
                .data(new BigInteger[]{
                        new BigInteger("4294967296"),
                        new BigInteger("18446744073709551615")
                })
                .build();
        UInt8MultiArray uint8MultiArray = UInt8MultiArray.builder()
                .layout(layout)
                .data(new byte[]{1, 2, 3, 4, 5, 6})
                .build();

        assertEquals(float32MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(float32MultiArray, Ros2Codecs.FLOAT32_MULTI_ARRAY), Ros2Codecs.FLOAT32_MULTI_ARRAY));
        assertEquals(float64MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(float64MultiArray, Ros2Codecs.FLOAT64_MULTI_ARRAY), Ros2Codecs.FLOAT64_MULTI_ARRAY));
        assertEquals(int8MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(int8MultiArray, Ros2Codecs.INT8_MULTI_ARRAY), Ros2Codecs.INT8_MULTI_ARRAY));
        assertEquals(int16MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(int16MultiArray, Ros2Codecs.INT16_MULTI_ARRAY), Ros2Codecs.INT16_MULTI_ARRAY));
        assertEquals(int32MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(int32MultiArray, Ros2Codecs.INT32_MULTI_ARRAY), Ros2Codecs.INT32_MULTI_ARRAY));
        assertEquals(int64MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(int64MultiArray, Ros2Codecs.INT64_MULTI_ARRAY), Ros2Codecs.INT64_MULTI_ARRAY));
        assertEquals(uint16MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(uint16MultiArray, Ros2Codecs.UINT16_MULTI_ARRAY), Ros2Codecs.UINT16_MULTI_ARRAY));
        assertEquals(uint32MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(uint32MultiArray, Ros2Codecs.UINT32_MULTI_ARRAY), Ros2Codecs.UINT32_MULTI_ARRAY));
        assertEquals(uint64MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(uint64MultiArray, Ros2Codecs.UINT64_MULTI_ARRAY), Ros2Codecs.UINT64_MULTI_ARRAY));
        assertEquals(uint8MultiArray, Ros2FastProto.decode(Ros2FastProto.encode(uint8MultiArray, Ros2Codecs.UINT8_MULTI_ARRAY), Ros2Codecs.UINT8_MULTI_ARRAY));
    }
}
