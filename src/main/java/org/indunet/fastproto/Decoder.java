package org.indunet.fastproto;

import lombok.val;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Binary decoder.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
final class Decoder {
    static ConcurrentHashMap<Class, Constructor> constructorMap = new ConcurrentHashMap<>();
    byte[] bytes;
    ByteBuffer byteBuffer;
    Map<String, Object> map = new LinkedHashMap<>();
    ByteOrder byteOrder = ByteOrder.LITTLE;
    BitOrder bitOrder = BitOrder.LSB_0;
    int ordinal = 0;
    Object last;

    Decoder(byte[] bytes) {
        this(new ByteBuffer(bytes));

        this.bytes = bytes;
    }

    Decoder(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    protected Decoder put(String key, Object value) {
        this.last = value;
        this.map.put(key, value);

        return this;
    }

    protected Decoder put(Object value) {
        this.last = value;
        this.map.put(String.valueOf(this.ordinal++), value);

        return this;
    }

    public Decoder defaultByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;

        return this;
    }

    public Decoder defaultBitOrder(BitOrder bitOrder) {
        this.bitOrder = bitOrder;

        return this;
    }

    public Decoder boolType(int byteOffset, int bitOffset) {
        return this.put(CodecUtils.boolType(this.bytes, byteOffset, bitOffset));
    }

    public Decoder boolType(int byteOffset, int bitOffset, String name) {
        return this.put(name, CodecUtils.boolType(this.bytes, byteOffset, bitOffset));
    }

    public Decoder readUInt8(int offset) {
        return this.put(CodecUtils.uint8Type(this.bytes, offset));
    }

    public Decoder readUInt8(int offset, String name) {
        return this.put(name, CodecUtils.uint8Type(this.bytes, offset));
    }

    public Decoder readUInt8() {
        return this.put(CodecUtils.uint8Type(this.byteBuffer, this.byteBuffer.getReadIndex()));
    }

    public Decoder readUInt8(String name) {
        return this.put(name, CodecUtils.uint8Type(this.byteBuffer, this.byteBuffer.getReadIndex()));
    }

    public Decoder readUInt16(int offset) {
        return this.put(CodecUtils.uint16Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder readUInt16(int offset, String name) {
        return this.put(name, CodecUtils.uint16Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder readUInt16(int offset, ByteOrder byteOrder) {
        return this.put(CodecUtils.uint16Type(this.bytes, offset, byteOrder));
    }

    public Decoder readUInt16(int offset, ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.uint16Type(this.bytes, offset, byteOrder));
    }

    public Decoder readUInt16() {
        return this.put(CodecUtils.uint16Type(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readUInt16(String name) {
        return this.put(name, CodecUtils.uint16Type(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readUInt16(ByteOrder byteOrder) {
        return this.put(CodecUtils.uint16Type(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readUInt16(ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.uint16Type(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readUInt32(int offset) {
        return this.put(CodecUtils.uint32Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder readUInt32(int offset, String name) {
        return this.put(name, CodecUtils.uint32Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder readUInt32(int offset, ByteOrder byteOrder) {
        return this.put(CodecUtils.uint32Type(this.bytes, offset, byteOrder));
    }

    public Decoder readUInt32(int offset, ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.uint32Type(this.bytes, offset, byteOrder));
    }

    public Decoder readUInt32() {
        return this.put(CodecUtils.uint32Type(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readUInt32(String name) {
        return this.put(name, CodecUtils.uint32Type(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readUInt32(ByteOrder byteOrder) {
        return this.put(CodecUtils.uint32Type(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readUInt32(ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.uint32Type(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readUInt64(int offset) {
        return this.put(CodecUtils.uint64Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder readUInt64(int offset, String name) {
        return this.put(name, CodecUtils.uint64Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder readUInt64(int offset, ByteOrder byteOrder) {
        return this.put(CodecUtils.uint64Type(this.bytes, offset, byteOrder));
    }

    public Decoder readUInt64(int offset, ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.uint64Type(this.bytes, offset, byteOrder));
    }

    public Decoder readUInt64() {
        return this.put(CodecUtils.uint64Type(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readUInt64(String name) {
        return this.put(name, CodecUtils.uint64Type(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readUInt64(ByteOrder byteOrder) {
        return this.put(CodecUtils.uint64Type(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readUInt64(ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.uint64Type(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readByte(int offset) {
        return this.put(CodecUtils.byteType(this.bytes, offset));
    }

    public Decoder readByte(int offset, String name) {
        return this.put(name, CodecUtils.byteType(this.bytes, offset));
    }

    public Decoder readByte() {
        return this.put(this.byteBuffer.get(this.byteBuffer.getReadIndex()));
    }

    public Decoder readByte(String name) {
        return this.put(name, this.byteBuffer.get(this.byteBuffer.getReadIndex()));
    }

    public Decoder readInt8(int offset) {
        return this.put(CodecUtils.int8Type(this.bytes, offset));
    }

    public Decoder readInt8(int offset, String name) {
        return this.put(name, CodecUtils.int8Type(this.bytes, offset));
    }

    public Decoder readInt8() {
        return this.put((int) this.byteBuffer.get(this.byteBuffer.getReadIndex()));
    }

    public Decoder readInt8(String name) {
        return this.put(name, (int) this.byteBuffer.get(this.byteBuffer.getReadIndex()));
    }

    public Decoder readShort(int offset) {
        return this.put(CodecUtils.shortType(this.bytes, offset, this.byteOrder));
    }

    public Decoder readShort(int offset, String name) {
        return this.put(name, CodecUtils.shortType(this.bytes, offset, this.byteOrder));
    }

    public Decoder readShort(int offset, ByteOrder byteOrder) {
        return this.put(CodecUtils.shortType(this.bytes, offset, byteOrder));
    }

    public Decoder readShort(int offset, ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.shortType(this.bytes, offset, byteOrder));
    }

    public Decoder readShort() {
        return this.put(CodecUtils.shortType(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readShort(String name) {
        return this.put(name, CodecUtils.shortType(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readShort(ByteOrder byteOrder) {
        return this.put(CodecUtils.shortType(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readShort(ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.shortType(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readInt16(int offset) {
        return this.put(CodecUtils.int16Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder readInt16(int offset, String name) {
        return this.put(name, CodecUtils.int16Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder readInt16(int offset, ByteOrder byteOrder) {
        return this.put(CodecUtils.int16Type(this.bytes, offset, byteOrder));
    }

    public Decoder readInt16(int offset, ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.int16Type(this.bytes, offset, byteOrder));
    }

    public Decoder readInt16() {
        return this.put(CodecUtils.int16Type(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readInt16(String name) {
        return this.put(name, CodecUtils.int16Type(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readInt16(ByteOrder byteOrder) {
        return this.put(CodecUtils.int16Type(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readInt16(ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.int16Type(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readInt32(int offset) {
        return this.put(CodecUtils.int32Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder readInt32(int offset, String name) {
        return this.put(name, CodecUtils.int32Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder readInt32(int offset, ByteOrder byteOrder) {
        return this.put(CodecUtils.int32Type(this.bytes, offset, byteOrder));
    }

    public Decoder readInt32(int offset, ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.int32Type(this.bytes, offset, byteOrder));
    }

    public Decoder readInt32() {
        return this.put(CodecUtils.int32Type(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readInt32(String name) {
        return this.put(name, CodecUtils.int32Type(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readInt32(ByteOrder byteOrder) {
        return this.put(CodecUtils.int32Type(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readInt32(ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.int32Type(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readInt64(int offset) {
        return this.put(CodecUtils.int64Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder readInt64(int offset, String name) {
        return this.put(name, CodecUtils.int64Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder readInt64(int offset, ByteOrder byteOrder) {
        return this.put(CodecUtils.int64Type(this.bytes, offset, byteOrder));
    }

    public Decoder readInt64(int offset, ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.int64Type(this.bytes, offset, byteOrder));
    }

    public Decoder readInt64() {
        return this.put(CodecUtils.int64Type(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readInt64(String name) {
        return this.put(name, CodecUtils.int64Type(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readInt64(ByteOrder byteOrder) {
        return this.put(CodecUtils.int64Type(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readInt64(ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.int64Type(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readFloat(int offset) {
        return this.put(CodecUtils.floatType(this.bytes, offset, this.byteOrder));
    }

    public Decoder readFloat(int offset, String name) {
        return this.put(name, CodecUtils.floatType(this.bytes, offset, this.byteOrder));
    }

    public Decoder readFloat(int offset, ByteOrder byteOrder) {
        return this.put(CodecUtils.floatType(this.bytes, offset, byteOrder));
    }

    public Decoder readFloat(int offset, ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.floatType(this.bytes, offset, byteOrder));
    }

    public Decoder readFloat() {
        return this.put(CodecUtils.floatType(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readFloat(String name) {
        return this.put(name, CodecUtils.floatType(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readFloat(ByteOrder byteOrder) {
        return this.put(CodecUtils.floatType(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readFloat(ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.floatType(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readDouble(int offset) {
        return this.put(CodecUtils.doubleType(this.bytes, offset, this.byteOrder));
    }

    public Decoder readDouble(int offset, String name) {
        return this.put(name, CodecUtils.doubleType(this.bytes, offset, this.byteOrder));
    }

    public Decoder readDouble(int offset, ByteOrder byteOrder) {
        return this.put(CodecUtils.doubleType(this.bytes, offset, byteOrder));
    }

    public Decoder readDouble(int offset, ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.doubleType(this.bytes, offset, byteOrder));
    }

    public Decoder readDouble() {
        return this.put(CodecUtils.doubleType(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readDouble(String name) {
        return this.put(name, CodecUtils.doubleType(this.byteBuffer, this.byteBuffer.getReadIndex(), this.byteOrder));
    }

    public Decoder readDouble(ByteOrder byteOrder) {
        return this.put(CodecUtils.doubleType(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public Decoder readDouble(ByteOrder byteOrder, String name) {
        return this.put(name, CodecUtils.doubleType(this.byteBuffer, this.byteBuffer.getReadIndex(), byteOrder));
    }

    public boolean getAsBoolean() {
        return (Boolean) this.last;
    }

    public short getAsShort() {
        return (Short) this.last;
    }

    public int getAsInt() {
        return (Integer) this.last;
    }

    public long getAsLong() {
        return (Long) this.last;
    }

    public BigInteger getAsBigInteger() {
        return (BigInteger) this.last;
    }

    public float getAsFloat() {
        return (Float) this.last;
    }

    public double getAsDouble() {
        return (Double) this.last;
    }

    public Decoder align(int alignment) {
        if (alignment <= 0 || (alignment & 0x01) != 0) {
            throw new IllegalArgumentException("alignment must be a positive even number");
        }

        int index = this.byteBuffer.getReadIndex();
        int after = ((index + (alignment - 1)) & ~(alignment - 1));

        if (after > 0) {
            this.byteBuffer.get(after - 1);
        } else {
            this.byteBuffer.resetReadIndex();
        }

        return this;
    }

    public Map<String, Object> getAsMap() {
        return this.map;
    }

    public <T> T mapTo(Class<T> clazz) {
        Constructor<T> constructor = constructorMap.computeIfAbsent(clazz, __ -> {
            val constructors = clazz.getConstructors();

            // only one constructor
            if (constructors.length == 1) {
                return constructors[0];
            } else if (Arrays.stream(constructors)
                    .anyMatch(c -> c.getParameterCount() == 0)) {
                return Arrays.stream(constructors)
                        .filter(c -> c.getParameterCount() == 0)
                        .findFirst()
                        .get();
            } else if (Arrays.stream(constructors)
                    .anyMatch(c -> c.getParameterCount() == this.map.size())) {
                return Arrays.stream(constructors)
                        .filter(c -> c.getParameterCount() == this.map.size())
                        .findFirst()
                        .get();
            } else {
                throw new CodecException("Could not find a valid constructor");
            }
        });

        if (constructor.getParameterCount() == 0) {
            return this.mapToNoArg(constructor);
        } else {
            return this.mapToArg(constructor);
        }
    }

    protected <T> T mapToNoArg(Constructor<T> constructor) {
        try {
            T obj = constructor.newInstance();

            val fields = Arrays.stream(constructor.getDeclaringClass().getDeclaredFields())
                    .peek(f -> f.setAccessible(true))
                    .collect(Collectors.toList());

            for (val f : fields) {
                if (this.map.containsKey(f.getName())) {
                    f.set(obj, this.map.get(f.getName()));
                }
            }

            return obj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DecodingException(String.format("Fail mapping to Object", constructor.getName()), e);
        }
    }

    protected <T> T mapToArg(Constructor<T> constructor) {
        try {
            Object[] args = this.map.entrySet().stream()
                    .map(Map.Entry::getValue)
                    .toArray();

            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DecodingException(String.format("Fail mapping to Object %s", constructor.getName()), e);
        }
    }
}
