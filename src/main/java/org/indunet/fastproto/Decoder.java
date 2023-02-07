package org.indunet.fastproto;

import lombok.val;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Binary decoder.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public final class Decoder {
    static ConcurrentHashMap<Class, Constructor> constructorMap = new ConcurrentHashMap<>();
    ByteBufferInputStream inputStream;
    Map<String, Object> fieldMap = new LinkedHashMap<>();
    ByteOrder defaultByteOrder = ByteOrder.LITTLE;
    BitOrder defaultBitOrder = BitOrder.LSB_0;

    Decoder(byte[] bytes) {
        this.inputStream = new ByteBufferInputStream(bytes);
    }

    /**
     * Modify default byte order, FastProto uses little endian by default if not specified.
     *
     * @param order Byte order
     * @return this
     */
    public Decoder defaultByteOrder(ByteOrder order) {
        this.defaultByteOrder = order;

        return this;
    }

    /**
     * Modify default bit order, FastProto uses lsb_0 by default if not specified.
     *
     * @param order Bit order
     * @return this
     */
    public Decoder defaultBitOrder(BitOrder order) {
        this.defaultBitOrder = order;

        return this;
    }

    public Decoder readBool(String name) {
        return this.readBool(name, defaultBitOrder, Function.identity());
    }

    public Decoder readBool(String name, Function<Boolean, ?> formula) {
        return this.readBool(name, defaultBitOrder, formula);
    }

    public Decoder readBool(String name, BitOrder order) {
        return this.readBool(name, order, Function.identity());
    }

    public Decoder readBool(String name, BitOrder order, Function<Boolean, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readBool(order)));

        return this;
    }

    public Decoder readBool(String name, int byteOffset, int bitOffset) {
        return this.readBool(name, byteOffset, bitOffset, defaultBitOrder, Function.identity());
    }

    public Decoder readBool(String name, int byteOffset, int bitOffset, Function<Boolean, ?> formula) {
        return this.readBool(name, byteOffset, bitOffset, defaultBitOrder, formula);
    }

    public Decoder readBool(String name, int byteOffset, int bitOffset, BitOrder order) {
        return this.readBool(name, byteOffset, bitOffset, order, Function.identity());
    }

    public Decoder readBool(String name, int byteOffset, int bitOffset, BitOrder order, Function<Boolean, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readBool(byteOffset, bitOffset, order)));

        return this;
    }

    public Decoder readByte(String name) {
        return this.readByte(name, Function.identity());
    }

    public Decoder readByte(String name, Function<Byte, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readByte()));

        return this;
    }

    public Decoder readByte(String name, int offset) {
        return this.readByte(name, offset, Function.identity());
    }

    public Decoder readByte(String name, int offset, Function<Byte, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readByte(offset)));

        return this;
    }

    public Decoder readShort(String name) {
        return this.readShort(name, defaultByteOrder, Function.identity());
    }

    public Decoder readShort(String name, ByteOrder order) {
        return this.readShort(name, order, Function.identity());
    }

    public Decoder readShort(String name, Function<Short, ?> formula) {
        return this.readShort(name, defaultByteOrder, formula);
    }

    public Decoder readShort(String name, ByteOrder order, Function<Short, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readShort(order)));

        return this;
    }

    public Decoder readShort(String name, int offset) {
        return this.readShort(name, offset, defaultByteOrder, Function.identity());
    }

    public Decoder readShort(String name, int offset, Function<Short, ?> formula) {
        return this.readShort(name, offset, defaultByteOrder, formula);
    }

    public Decoder readShort(String name, int offset, ByteOrder order) {
        return this.readShort(name, offset, order, Function.identity());
    }

    public Decoder readShort(String name, int offset, ByteOrder order, Function<Short, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readShort(offset, order)));

        return this;
    }

    public Decoder readInt8(String name) {
        return this.readInt8(name, Function.identity());
    }

    public Decoder readInt8(String name, Function<Integer, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readInt8()));

        return this;
    }

    public Decoder readInt8(String name, int offset) {
        return this.readInt8(name, offset, Function.identity());
    }

    public Decoder readInt8(String name, int offset, Function<Integer, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readInt8(offset)));

        return this;
    }

    public Decoder readInt16(String name) {
        return this.readInt16(name, defaultByteOrder, Function.identity());
    }

    public Decoder readInt16(String name, ByteOrder order) {
        return this.readInt16(name, order, Function.identity());
    }

    public Decoder readInt16(String name, Function<Integer, ?> formula) {
        return this.readInt16(name, defaultByteOrder, formula);
    }

    public Decoder readInt16(String name, ByteOrder order, Function<Integer, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readInt16(order)));

        return this;
    }

    public Decoder readInt16(String name, int offset) {
        return this.readInt16(name, offset, defaultByteOrder, Function.identity());
    }

    public Decoder readInt16(String name, int offset, Function<Integer, ?> formula) {
        return this.readInt16(name, offset, defaultByteOrder, formula);
    }

    public Decoder readInt16(String name, int offset, ByteOrder order) {
        return this.readInt16(name, offset, order, Function.identity());
    }

    public Decoder readInt16(String name, int offset, ByteOrder order, Function<Integer, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readInt16(offset, order)));

        return this;
    }

    public Decoder readInt32(String name) {
        return this.readInt32(name, defaultByteOrder, Function.identity());
    }

    public Decoder readInt32(String name, ByteOrder order) {
        return this.readInt32(name, order, Function.identity());
    }

    public Decoder readInt32(String name, Function<Integer, ?> formula) {
        return this.readInt32(name, defaultByteOrder, formula);
    }

    public Decoder readInt32(String name, ByteOrder order, Function<Integer, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readInt32(order)));

        return this;
    }

    public Decoder readInt32(String name, int offset) {
        return this.readInt32(name, offset, defaultByteOrder, Function.identity());
    }

    public Decoder readInt32(String name, int offset, Function<Integer, ?> formula) {
        return this.readInt32(name, offset, defaultByteOrder, formula);
    }

    public Decoder readInt32(String name, int offset, ByteOrder order) {
        return this.readInt32(name, offset, order, Function.identity());
    }

    public Decoder readInt32(String name, int offset, ByteOrder order, Function<Integer, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readInt32(offset, order)));

        return this;
    }

    public Decoder readInt64(String name) {
        return this.readInt64(name, defaultByteOrder, Function.identity());
    }

    public Decoder readInt64(String name, ByteOrder order) {
        return this.readInt64(name, order, Function.identity());
    }

    public Decoder readInt64(String name, Function<Long, ?> formula) {
        return this.readInt64(name, defaultByteOrder, formula);
    }

    public Decoder readInt64(String name, ByteOrder order, Function<Long, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readInt64(order)));

        return this;
    }

    public Decoder readInt64(String name, int offset) {
        return this.readInt64(name, offset, defaultByteOrder, Function.identity());
    }

    public Decoder readInt64(String name, int offset, Function<Long, ?> formula) {
        return this.readInt64(name, offset, defaultByteOrder, formula);
    }

    public Decoder readInt64(String name, int offset, ByteOrder order) {
        return this.readInt64(name, offset, order, Function.identity());
    }

    public Decoder readInt64(String name, int offset, ByteOrder order, Function<Long, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readInt64(offset, order)));

        return this;
    }

    public Decoder readUInt8(String name) {
        return this.readUInt8(name, Function.identity());
    }

    public Decoder readUInt8(String name, Function<Integer, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readUInt8()));

        return this;
    }

    public Decoder readUInt8(String name, int offset) {
        return this.readUInt8(name, offset, Function.identity());
    }

    public Decoder readUInt8(String name, int offset, Function<Integer, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readUInt8(offset)));

        return this;
    }

    public Decoder readUInt16(String name) {
        return this.readUInt16(name, defaultByteOrder, Function.identity());
    }

    public Decoder readUInt16(String name, ByteOrder order) {
        return this.readUInt16(name, order, Function.identity());
    }

    public Decoder readUInt16(String name, Function<Integer, ?> formula) {
        return this.readUInt16(name, defaultByteOrder, formula);
    }

    public Decoder readUInt16(String name, ByteOrder order, Function<Integer, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readUInt16(order)));

        return this;
    }

    public Decoder readUInt16(String name, int offset) {
        return this.readUInt16(name, offset, defaultByteOrder, Function.identity());
    }

    public Decoder readUInt16(String name, int offset, Function<Integer, ?> formula) {
        return this.readUInt16(name, offset, defaultByteOrder, formula);
    }

    public Decoder readUInt16(String name, int offset, ByteOrder order) {
        return this.readUInt16(name, offset, order, Function.identity());
    }

    public Decoder readUInt16(String name, int offset, ByteOrder order, Function<Integer, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readUInt16(offset, order)));

        return this;
    }

    public Decoder readUInt32(String name) {
        return this.readUInt32(name, defaultByteOrder, Function.identity());
    }

    public Decoder readUInt32(String name, ByteOrder order) {
        return this.readUInt32(name, order, Function.identity());
    }

    public Decoder readUInt32(String name, Function<Long, ?> formula) {
        return this.readUInt32(name, defaultByteOrder, formula);
    }

    public Decoder readUInt32(String name, ByteOrder order, Function<Long, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readUInt32(order)));

        return this;
    }

    public Decoder readUInt32(String name, int offset) {
        return this.readUInt32(name, offset, defaultByteOrder, Function.identity());
    }

    public Decoder readUInt32(String name, int offset, Function<Long, ?> formula) {
        return this.readUInt32(name, offset, defaultByteOrder, formula);
    }

    public Decoder readUInt32(String name, int offset, ByteOrder order) {
        return this.readUInt32(name, offset, order, Function.identity());
    }

    public Decoder readUInt32(String name, int offset, ByteOrder order, Function<Long, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readUInt32(offset, order)));

        return this;
    }

    public Decoder readUInt64(String name) {
        return this.readUInt64(name, defaultByteOrder, Function.identity());
    }

    public Decoder readUInt64(String name, ByteOrder order) {
        return this.readUInt64(name, order, Function.identity());
    }

    public Decoder readUInt64(String name, Function<BigInteger, ?> formula) {
        return this.readUInt64(name, defaultByteOrder, formula);
    }

    public Decoder readUInt64(String name, ByteOrder order, Function<BigInteger, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readUInt64(order)));

        return this;
    }

    public Decoder readUInt64(String name, int offset) {
        return this.readUInt64(name, offset, defaultByteOrder, Function.identity());
    }

    public Decoder readUInt64(String name, int offset, Function<BigInteger, ?> formula) {
        return this.readUInt64(name, offset, defaultByteOrder, formula);
    }

    public Decoder readUInt64(String name, int offset, ByteOrder order) {
        return this.readUInt64(name, offset, order, Function.identity());
    }

    public Decoder readUInt64(String name, int offset, ByteOrder order, Function<BigInteger, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readUInt64(offset, order)));

        return this;
    }

    public Decoder readFloat(String name) {
        return this.readFloat(name, defaultByteOrder, Function.identity());
    }

    public Decoder readFloat(String name, ByteOrder order) {
        return this.readFloat(name, order, Function.identity());
    }

    public Decoder readFloat(String name, Function<Float, ?> formula) {
        return this.readFloat(name, defaultByteOrder, formula);
    }

    public Decoder readFloat(String name, ByteOrder order, Function<Float, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readFloat(order)));

        return this;
    }

    public Decoder readFloat(String name, int offset) {
        return this.readFloat(name, offset, defaultByteOrder, Function.identity());
    }

    public Decoder readFloat(String name, int offset, Function<Float, ?> formula) {
        return this.readFloat(name, offset, defaultByteOrder, formula);
    }

    public Decoder readFloat(String name, int offset, ByteOrder order) {
        return this.readFloat(name, offset, order, Function.identity());
    }

    public Decoder readFloat(String name, int offset, ByteOrder order, Function<Float, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readFloat(offset, order)));

        return this;
    }

    public Decoder readDouble(String name) {
        return this.readDouble(name, defaultByteOrder, Function.identity());
    }

    public Decoder readDouble(String name, ByteOrder order) {
        return this.readDouble(name, order, Function.identity());
    }

    public Decoder readDouble(String name, Function<Double, ?> formula) {
        return this.readDouble(name, defaultByteOrder, formula);
    }

    public Decoder readDouble(String name, ByteOrder order, Function<Double, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readDouble(order)));

        return this;
    }

    public Decoder readDouble(String name, int offset) {
        return this.readDouble(name, offset, defaultByteOrder, Function.identity());
    }

    public Decoder readDouble(String name, int offset, Function<Double, ?> formula) {
        return this.readDouble(name, offset, defaultByteOrder, formula);
    }

    public Decoder readDouble(String name, int offset, ByteOrder order) {
        return this.readDouble(name, offset, order, Function.identity());
    }

    public Decoder readDouble(String name, int offset, ByteOrder order, Function<Double, ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readDouble(offset, order)));

        return this;
    }

    public Decoder readBytes(String name, int length) {
        return this.readBytes(name, length, Function.identity());
    }

    public Decoder readBytes(String name, int length, Function<byte[], ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readBytes(length)));

        return this;
    }

    public Decoder readBytes(String name, int offset, int length) {
        return this.readBytes(name, offset, length, Function.identity());
    }

    public Decoder readBytes(String name, int offset, int length, Function<byte[], ?> formula) {
        this.fieldMap.put(name, formula.apply(inputStream.readBytes(offset, length)));

        return this;
    }

    public Decoder align(int alignment) {
        this.inputStream.align(alignment);

        return this;
    }

    public Decoder skip() {
        this.inputStream.skip();

        return this;
    }

    public Decoder skip(int num) {
        this.inputStream.skip(num);

        return this;
    }

    public Map<String, Object> getMap() {
        return this.fieldMap;
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
                    .anyMatch(c -> c.getParameterCount() == this.fieldMap.size())) {
                return Arrays.stream(constructors)
                        .filter(c -> c.getParameterCount() == this.fieldMap.size())
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
                if (this.fieldMap.containsKey(f.getName())) {
                    f.set(obj, this.fieldMap.get(f.getName()));
                }
            }

            return obj;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DecodingException(String.format("Fail mapping to Object", constructor.getName()), e);
        }
    }

    protected <T> T mapToArg(Constructor<T> constructor) {
        try {
            Object[] args = this.fieldMap.values()
                    .stream()
                    .toArray();

            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DecodingException(String.format("Fail mapping to Object %s", constructor.getName()), e);
        }
    }
}
