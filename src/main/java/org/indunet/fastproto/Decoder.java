package org.indunet.fastproto;

import lombok.val;
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.codec.Codec;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.util.CodecUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Binary decoder.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public class Decoder {
    protected byte[] bytes;
    protected Map<String, Object> map = new HashMap<>();
    protected EndianPolicy endianPolicy = EndianPolicy.LITTLE;
    protected int ordinal = 0;

    Decoder(byte[] bytes) {
        this.bytes = bytes;
    }

    public Decoder defaultEndian(EndianPolicy endianPolicy) {
        this.endianPolicy = endianPolicy;

        return this;
    }

    public Decoder boolType(int byteOffset, int bitOffset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.boolType(this.bytes, byteOffset, bitOffset));

        return this;
    }

    public Decoder boolType(int byteOffset, int bitOffset, String name) {
        this.map.put(name, CodecUtils.boolType(this.bytes, byteOffset, bitOffset));

        return this;
    }

    public Decoder uint8Type(int offset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.uint8Type(this.bytes, offset));

        return this;
    }

    public Decoder uint8Type(int offset, String name) {
        this.map.put(name, CodecUtils.uint8Type(this.bytes, offset));

        return this;
    }

    public Decoder uint16Type(int offset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.uint16Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder uint16Type(int offset, EndianPolicy endianPolicy) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.uint16Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder uint16Type(int offset, String name) {
        this.map.put(name, CodecUtils.uint16Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder uint16Type(int offset, EndianPolicy endianPolicy, String name) {
        this.map.put(name, CodecUtils.uint16Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder uint32Type(int offset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.uint32Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder uint32Type(int offset, EndianPolicy endianPolicy) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.uint32Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder uint32Type(int offset, String name) {
        this.map.put(name, CodecUtils.uint32Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder uint32Type(int offset, EndianPolicy endianPolicy, String name) {
        this.map.put(name, CodecUtils.uint32Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder uint64Type(int offset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.uint64Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder uint64Type(int offset, EndianPolicy endianPolicy) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.uint64Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder uint64Type(int offset, String name) {
        this.map.put(name, CodecUtils.uint64Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder uint64Type(int offset, EndianPolicy endianPolicy, String name) {
        this.map.put(name, CodecUtils.uint64Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder byteType(int offset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.byteType(this.bytes, offset));

        return this;
    }

    public Decoder int8Type(int offset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.int8Type(this.bytes, offset));

        return this;
    }

    public Decoder int8Type(int offset, String name) {
        this.map.put(name, CodecUtils.int8Type(this.bytes, offset));

        return this;
    }

    public Decoder shortType(int offset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.shortType(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder shortType(int offset, EndianPolicy endianPolicy) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.shortType(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder shortType(int offset, String name) {
        this.map.put(name, CodecUtils.shortType(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder shortType(int offset, EndianPolicy endianPolicy, String name) {
        this.map.put(name, CodecUtils.shortType(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder int16Type(int offset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.int16Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder int16Type(int offset, EndianPolicy endianPolicy) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.int16Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder int16Type(int offset, String name) {
        this.map.put(name, CodecUtils.int16Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder int16Type(int offset, EndianPolicy endianPolicy, String name) {
        this.map.put(name, CodecUtils.int16Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder int32Type(int offset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.int32Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder int32Type(int offset, EndianPolicy endianPolicy) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.int32Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder int32Type(int offset, String name) {
        this.map.put(name, CodecUtils.int32Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder int32Type(int offset, EndianPolicy endianPolicy, String name) {
        this.map.put(name, CodecUtils.int32Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder int64Type(int offset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.int64Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder int64Type(int offset, EndianPolicy endianPolicy) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.int64Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder int64Type(int offset, String name) {
        this.map.put(name, CodecUtils.int64Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder int64Type(int offset, EndianPolicy endianPolicy, String name) {
        this.map.put(name, CodecUtils.int64Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder floatType(int offset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.floatType(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder floatType(int offset, EndianPolicy endianPolicy) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.floatType(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder floatType(int offset, String name) {
        this.map.put(name, CodecUtils.floatType(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder floatType(int offset, EndianPolicy endianPolicy, String name) {
        this.map.put(name, CodecUtils.floatType(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder doubleType(int offset) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.doubleType(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder doubleType(int offset, EndianPolicy endianPolicy) {
        this.map.put(String.valueOf(this.ordinal ++), CodecUtils.doubleType(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder doubleType(int offset, String name) {
        this.map.put(name, CodecUtils.doubleType(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder doubleType(int offset, EndianPolicy endianPolicy, String name) {
        this.map.put(name, CodecUtils.doubleType(this.bytes, offset, endianPolicy));

        return this;
    }

    public Map<String, Object> get() {
        return this.map;
    }

    public <T> T mapTo(Class<T> clazz) {
        try {
            T obj = clazz.newInstance();

            val fields = clazz.getDeclaredFields();
            val fieldMap = Arrays.stream(fields)
                    .peek(f -> f.setAccessible(true))
                    .collect(Collectors.toMap(Field::getName,Function.identity()));

            for (val entry : this.map.entrySet()) {
                String fieldName = entry.getKey();
                Object value = entry.getValue();

                // match by name
                if (fieldMap.containsKey(fieldName)) {
                    try {
                        fieldMap.get(fieldName).set(obj, value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                    continue;
                }

                // match by ordinal
                if (!fieldName.chars()
                        .allMatch(c -> c >= '0' && c <= '9')) {
                    continue;
                }

                val ordinal = Integer.parseInt(fieldName);

                if (ordinal < fields.length) {
                    fields[ordinal].set(obj, value);
                }
            }

            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CodecException(String.format("Fail mapping to %s", clazz.getName()), e);
        }
    }
}
