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

    public Decoder defaultEndian(ByteOrder endianPolicy) {
        this.byteOrder = endianPolicy;

        return this;
    }

    public Decoder boolType(int byteOffset, int bitOffset) {
        return this.put(CodecUtils.boolType(this.bytes, byteOffset, bitOffset));
    }

    public Decoder boolType(int byteOffset, int bitOffset, String name) {
        return this.put(name, CodecUtils.boolType(this.bytes, byteOffset, bitOffset));
    }

    public Decoder uint8Type(int offset) {
        return this.put(CodecUtils.uint8Type(this.bytes, offset));
    }

    public Decoder uint8Type(int offset, String name) {
        return this.put(name, CodecUtils.uint8Type(this.bytes, offset));
    }

    public Decoder uint16Type(int offset) {
        return this.put(CodecUtils.uint16Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder uint16Type(int offset, String name) {
        return this.put(name, CodecUtils.uint16Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder uint16Type(int offset, ByteOrder endianPolicy) {
        return this.put(CodecUtils.uint16Type(this.bytes, offset, endianPolicy));
    }

    public Decoder uint16Type(int offset, ByteOrder endianPolicy, String name) {
        return this.put(name, CodecUtils.uint16Type(this.bytes, offset, endianPolicy));
    }

    public Decoder uint32Type(int offset) {
        return this.put(CodecUtils.uint32Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder uint32Type(int offset, String name) {
        return this.put(name, CodecUtils.uint32Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder uint32Type(int offset, ByteOrder endianPolicy) {
        return this.put(CodecUtils.uint32Type(this.bytes, offset, endianPolicy));
    }

    public Decoder uint32Type(int offset, ByteOrder endianPolicy, String name) {
        return this.put(name, CodecUtils.uint32Type(this.bytes, offset, endianPolicy));
    }

    public Decoder uint64Type(int offset) {
        return this.put(CodecUtils.uint64Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder uint64Type(int offset, String name) {
        return this.put(name, CodecUtils.uint64Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder uint64Type(int offset, ByteOrder endianPolicy) {
        return this.put(CodecUtils.uint64Type(this.bytes, offset, endianPolicy));
    }

    public Decoder uint64Type(int offset, ByteOrder endianPolicy, String name) {
        return this.put(name, CodecUtils.uint64Type(this.bytes, offset, endianPolicy));
    }

    public Decoder byteType(int offset) {
        return this.put(CodecUtils.byteType(this.bytes, offset));
    }

    public Decoder byteType(int offset, String name) {
        return this.put(name, CodecUtils.byteType(this.bytes, offset));
    }

    public Decoder int8Type() {
        return this.put((int) this.byteBuffer.read());
    }

    public Decoder int8Type(int offset) {
        return this.put(CodecUtils.int8Type(this.bytes, offset));
    }

    public Decoder int8Type(int offset, String name) {
        return this.put(name, CodecUtils.int8Type(this.bytes, offset));
    }

    public Decoder shortType(int offset) {
        return this.put(CodecUtils.shortType(this.bytes, offset, this.byteOrder));
    }

    public Decoder shortType(int offset, String name) {
        return this.put(name, CodecUtils.shortType(this.bytes, offset, this.byteOrder));
    }

    public Decoder shortType(int offset, ByteOrder endianPolicy) {
        return this.put(CodecUtils.shortType(this.bytes, offset, endianPolicy));
    }

    public Decoder shortType(int offset, ByteOrder endianPolicy, String name) {
        return this.put(name, CodecUtils.shortType(this.bytes, offset, endianPolicy));
    }

    public Decoder int16Type(int offset) {
        return this.put(CodecUtils.int16Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder int16Type(int offset, String name) {
        return this.put(name, CodecUtils.int16Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder int16Type(int offset, ByteOrder endianPolicy) {
        return this.put(CodecUtils.int16Type(this.bytes, offset, endianPolicy));
    }

    public Decoder int16Type(int offset, ByteOrder endianPolicy, String name) {
        return this.put(name, CodecUtils.int16Type(this.bytes, offset, endianPolicy));
    }

    public Decoder int32Type(int offset) {
        return this.put(CodecUtils.int32Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder int32Type(int offset, String name) {
        return this.put(name, CodecUtils.int32Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder int32Type(int offset, ByteOrder endianPolicy) {
        return this.put(CodecUtils.int32Type(this.bytes, offset, endianPolicy));
    }

    public Decoder int32Type(int offset, ByteOrder endianPolicy, String name) {
        return this.put(name, CodecUtils.int32Type(this.bytes, offset, endianPolicy));
    }

    public Decoder int64Type(int offset) {
        return this.put(CodecUtils.int64Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder int64Type(int offset, String name) {
        return this.put(name, CodecUtils.int64Type(this.bytes, offset, this.byteOrder));
    }

    public Decoder int64Type(int offset, ByteOrder endianPolicy) {
        return this.put(CodecUtils.int64Type(this.bytes, offset, endianPolicy));
    }

    public Decoder int64Type(int offset, ByteOrder endianPolicy, String name) {
        return this.put(name, CodecUtils.int64Type(this.bytes, offset, endianPolicy));
    }

    public Decoder floatType(int offset) {
        return this.put(CodecUtils.floatType(this.bytes, offset, this.byteOrder));
    }

    public Decoder floatType(int offset, String name) {
        return this.put(name, CodecUtils.floatType(this.bytes, offset, this.byteOrder));
    }

    public Decoder floatType(int offset, ByteOrder endianPolicy) {
        return this.put(CodecUtils.floatType(this.bytes, offset, endianPolicy));
    }

    public Decoder floatType(int offset, ByteOrder endianPolicy, String name) {
        return this.put(name, CodecUtils.floatType(this.bytes, offset, endianPolicy));
    }

    public Decoder doubleType(int offset) {
        return this.put(CodecUtils.doubleType(this.bytes, offset, this.byteOrder));
    }

    public Decoder doubleType(int offset, String name) {
        return this.put(name, CodecUtils.doubleType(this.bytes, offset, this.byteOrder));
    }

    public Decoder doubleType(int offset, ByteOrder endianPolicy) {
        return this.put(CodecUtils.doubleType(this.bytes, offset, endianPolicy));
    }

    public Decoder doubleType(int offset, ByteOrder endianPolicy, String name) {
        return this.put(name, CodecUtils.doubleType(this.bytes, offset, endianPolicy));
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

    @Deprecated
    public Map<String, Object> get() {
        return this.map;
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
