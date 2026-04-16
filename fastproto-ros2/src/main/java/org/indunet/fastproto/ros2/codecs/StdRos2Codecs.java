package org.indunet.fastproto.ros2.codecs;

import org.indunet.fastproto.ros2.Ros2CdrReader;
import org.indunet.fastproto.ros2.Ros2CdrWriter;
import org.indunet.fastproto.ros2.Ros2Codec;
import org.indunet.fastproto.ros2.internal.FixedSizeRos2Codec;
import org.indunet.fastproto.ros2.std_msgs.msg.Bool;
import org.indunet.fastproto.ros2.std_msgs.msg.Byte;
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
import org.indunet.fastproto.ros2.std_msgs.msg.Int64;
import org.indunet.fastproto.ros2.std_msgs.msg.Int64MultiArray;
import org.indunet.fastproto.ros2.std_msgs.msg.Int32;
import org.indunet.fastproto.ros2.std_msgs.msg.Int32MultiArray;
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

import java.math.BigInteger;

public final class StdRos2Codecs {
    public static final Ros2Codec<ColorRGBA> COLOR_RGBA = new FixedSizeRos2Codec<>(ColorRGBA.class, 4, ColorRGBA.SIZE);

    public static final Ros2Codec<Header> HEADER = new Ros2Codec<Header>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Header value) {
            BuiltinRos2Codecs.TIME.serialize(writer, value.getStamp());
            writer.writeString(value.getFrameId());
        }

        @Override
        public Header deserialize(Ros2CdrReader reader) {
            return Header.builder()
                    .stamp(BuiltinRos2Codecs.TIME.deserialize(reader))
                    .frameId(reader.readString())
                    .build();
        }
    };

    public static final Ros2Codec<Bool> BOOL = scalarCodec(
            (writer, value) -> writer.writeBool(value.isData()),
            reader -> Bool.builder().data(reader.readBool()).build()
    );

    public static final Ros2Codec<Empty> EMPTY = scalarCodec(
            (writer, value) -> {
            },
            reader -> Empty.builder().build()
    );

    public static final Ros2Codec<Byte> BYTE = scalarCodec(
            (writer, value) -> writer.writeInt8(value.getData()),
            reader -> Byte.builder().data(reader.readInt8()).build()
    );

    public static final Ros2Codec<Char> CHAR = scalarCodec(
            (writer, value) -> writer.writeUInt8(value.getData()),
            reader -> Char.builder().data(reader.readUInt8()).build()
    );

    public static final Ros2Codec<Int8> INT8 = scalarCodec(
            (writer, value) -> writer.writeInt8(value.getData()),
            reader -> Int8.builder().data(reader.readInt8()).build()
    );

    public static final Ros2Codec<Int16> INT16 = scalarCodec(
            (writer, value) -> writer.writeInt16(value.getData()),
            reader -> Int16.builder().data(reader.readInt16()).build()
    );

    public static final Ros2Codec<Int32> INT32 = scalarCodec(
            (writer, value) -> writer.writeInt32(value.getData()),
            reader -> Int32.builder().data(reader.readInt32()).build()
    );

    public static final Ros2Codec<Int64> INT64 = scalarCodec(
            (writer, value) -> writer.writeInt64(value.getData()),
            reader -> Int64.builder().data(reader.readInt64()).build()
    );

    public static final Ros2Codec<UInt16> UINT16 = scalarCodec(
            (writer, value) -> writer.writeUInt16(value.getData()),
            reader -> UInt16.builder().data(reader.readUInt16()).build()
    );

    public static final Ros2Codec<UInt64> UINT64 = scalarCodec(
            (writer, value) -> writer.writeUInt64(value.getData() == null ? BigInteger.ZERO : value.getData()),
            reader -> UInt64.builder().data(reader.readUInt64()).build()
    );

    public static final Ros2Codec<UInt32> UINT32 = scalarCodec(
            (writer, value) -> writer.writeUInt32(value.getData()),
            reader -> UInt32.builder().data(reader.readUInt32()).build()
    );

    public static final Ros2Codec<UInt8> UINT8 = scalarCodec(
            (writer, value) -> writer.writeUInt8(value.getData()),
            reader -> UInt8.builder().data(reader.readUInt8()).build()
    );

    public static final Ros2Codec<Float32> FLOAT32 = scalarCodec(
            (writer, value) -> writer.writeFloat(value.getData()),
            reader -> Float32.builder().data(reader.readFloat()).build()
    );

    public static final Ros2Codec<Float64> FLOAT64 = scalarCodec(
            (writer, value) -> writer.writeDouble(value.getData()),
            reader -> Float64.builder().data(reader.readDouble()).build()
    );

    public static final Ros2Codec<org.indunet.fastproto.ros2.std_msgs.msg.String> STD_STRING = scalarCodec(
            (writer, value) -> writer.writeString(value.getData() == null ? "" : value.getData()),
            reader -> org.indunet.fastproto.ros2.std_msgs.msg.String.builder().data(reader.readString()).build()
    );

    public static final Ros2Codec<MultiArrayDimension> MULTI_ARRAY_DIMENSION = new Ros2Codec<MultiArrayDimension>() {
        @Override
        public void serialize(Ros2CdrWriter writer, MultiArrayDimension value) {
            writer.writeString(value.getLabel());
            writer.writeUInt32(value.getSize());
            writer.writeUInt32(value.getStride());
        }

        @Override
        public MultiArrayDimension deserialize(Ros2CdrReader reader) {
            return MultiArrayDimension.builder()
                    .label(reader.readString())
                    .size(reader.readUInt32())
                    .stride(reader.readUInt32())
                    .build();
        }
    };

    public static final Ros2Codec<MultiArrayLayout> MULTI_ARRAY_LAYOUT = new Ros2Codec<MultiArrayLayout>() {
        @Override
        public void serialize(Ros2CdrWriter writer, MultiArrayLayout value) {
            Ros2CodecSupport.writeMultiArrayDimensionArray(writer, value.getDim());
            writer.writeUInt32(value.getDataOffset());
        }

        @Override
        public MultiArrayLayout deserialize(Ros2CdrReader reader) {
            return MultiArrayLayout.builder()
                    .dim(Ros2CodecSupport.readMultiArrayDimensionArray(reader))
                    .dataOffset(reader.readUInt32())
                    .build();
        }
    };

    public static final Ros2Codec<Float32MultiArray> FLOAT32_MULTI_ARRAY = new Ros2Codec<Float32MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Float32MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeFloatSequence(Ros2CodecSupport.safeFloatArray(value.getData()));
        }

        @Override
        public Float32MultiArray deserialize(Ros2CdrReader reader) {
            return Float32MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readFloatSequence())
                    .build();
        }
    };

    public static final Ros2Codec<Float64MultiArray> FLOAT64_MULTI_ARRAY = new Ros2Codec<Float64MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Float64MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeDoubleSequence(Ros2CodecSupport.safeDoubleArray(value.getData()));
        }

        @Override
        public Float64MultiArray deserialize(Ros2CdrReader reader) {
            return Float64MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readDoubleSequence())
                    .build();
        }
    };

    public static final Ros2Codec<Int32MultiArray> INT32_MULTI_ARRAY = new Ros2Codec<Int32MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Int32MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeInt32Sequence(Ros2CodecSupport.safeIntArray(value.getData()));
        }

        @Override
        public Int32MultiArray deserialize(Ros2CdrReader reader) {
            return Int32MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readInt32Sequence())
                    .build();
        }
    };

    public static final Ros2Codec<Int16MultiArray> INT16_MULTI_ARRAY = new Ros2Codec<Int16MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Int16MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeInt16Sequence(Ros2CodecSupport.safeIntArray(value.getData()));
        }

        @Override
        public Int16MultiArray deserialize(Ros2CdrReader reader) {
            return Int16MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readInt16Sequence())
                    .build();
        }
    };

    public static final Ros2Codec<Int8MultiArray> INT8_MULTI_ARRAY = new Ros2Codec<Int8MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Int8MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeByteSequence(value.getData() == null ? new byte[0] : value.getData());
        }

        @Override
        public Int8MultiArray deserialize(Ros2CdrReader reader) {
            return Int8MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readByteSequence())
                    .build();
        }
    };

    public static final Ros2Codec<Int64MultiArray> INT64_MULTI_ARRAY = new Ros2Codec<Int64MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, Int64MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeInt64Sequence(value.getData() == null ? new long[0] : value.getData());
        }

        @Override
        public Int64MultiArray deserialize(Ros2CdrReader reader) {
            return Int64MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readInt64Sequence())
                    .build();
        }
    };

    public static final Ros2Codec<UInt16MultiArray> UINT16_MULTI_ARRAY = new Ros2Codec<UInt16MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, UInt16MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeUInt16Sequence(Ros2CodecSupport.safeIntArray(value.getData()));
        }

        @Override
        public UInt16MultiArray deserialize(Ros2CdrReader reader) {
            return UInt16MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readUInt16Sequence())
                    .build();
        }
    };

    public static final Ros2Codec<UInt32MultiArray> UINT32_MULTI_ARRAY = new Ros2Codec<UInt32MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, UInt32MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeUInt32Sequence(value.getData() == null ? new long[0] : value.getData());
        }

        @Override
        public UInt32MultiArray deserialize(Ros2CdrReader reader) {
            return UInt32MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readUInt32Sequence())
                    .build();
        }
    };

    public static final Ros2Codec<UInt64MultiArray> UINT64_MULTI_ARRAY = new Ros2Codec<UInt64MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, UInt64MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeUInt64Sequence(Ros2CodecSupport.safeBigIntegerArray(value.getData()));
        }

        @Override
        public UInt64MultiArray deserialize(Ros2CdrReader reader) {
            return UInt64MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readUInt64Sequence())
                    .build();
        }
    };

    public static final Ros2Codec<UInt8MultiArray> UINT8_MULTI_ARRAY = new Ros2Codec<UInt8MultiArray>() {
        @Override
        public void serialize(Ros2CdrWriter writer, UInt8MultiArray value) {
            MULTI_ARRAY_LAYOUT.serialize(writer, value.getLayout());
            writer.writeByteSequence(value.getData() == null ? new byte[0] : value.getData());
        }

        @Override
        public UInt8MultiArray deserialize(Ros2CdrReader reader) {
            return UInt8MultiArray.builder()
                    .layout(MULTI_ARRAY_LAYOUT.deserialize(reader))
                    .data(reader.readByteSequence())
                    .build();
        }
    };

    private StdRos2Codecs() {
    }

    private interface Serializer<T> {
        void write(Ros2CdrWriter writer, T value);
    }

    private interface Deserializer<T> {
        T read(Ros2CdrReader reader);
    }

    private static <T> Ros2Codec<T> scalarCodec(Serializer<T> serializer, Deserializer<T> deserializer) {
        return new Ros2Codec<T>() {
            @Override
            public void serialize(Ros2CdrWriter writer, T value) {
                serializer.write(writer, value);
            }

            @Override
            public T deserialize(Ros2CdrReader reader) {
                return deserializer.read(reader);
            }
        };
    }
}
