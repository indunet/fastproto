package org.indunet.fastproto;

import lombok.val;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.lang.reflect.Field;
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

    Decoder(byte[] bytes) {
        this.bytes = bytes;
    }

    public Decoder defaultEndian(EndianPolicy endianPolicy) {
        this.endianPolicy = endianPolicy;

        return this;
    }

    public Decoder boolType(String name, int byteOffset, int bitOffset) {
        this.map.put(name, CodecUtils.boolType(this.bytes, byteOffset, bitOffset));

        return this;
    }

    public Decoder uint8Type(String name, int offset) {
        this.map.put(name, CodecUtils.uint8Type(this.bytes, offset));

        return this;
    }

    public Decoder uint16Type(String name, int offset) {
        this.map.put(name, CodecUtils.uint16Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder uint16Type(String name, int offset, EndianPolicy endianPolicy) {
        this.map.put(name, CodecUtils.uint16Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder uint32Type(String name, int offset) {
        this.map.put(name, CodecUtils.uint32Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder uint32Type(String name, int offset, EndianPolicy endianPolicy) {
        this.map.put(name, CodecUtils.uint32Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder uint64Type(String name, int offset) {
        this.map.put(name, CodecUtils.uint64Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder uint64Type(String name, int offset, EndianPolicy endianPolicy) {
        this.map.put(name, CodecUtils.uint64Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder byteType(String name, int offset) {
        this.map.put(name, CodecUtils.byteType(this.bytes, offset));

        return this;
    }

    public Decoder int8Type(String name, int offset) {
        this.map.put(name, CodecUtils.int8Type(this.bytes, offset));

        return this;
    }

    public Decoder shortType(String name, int offset) {
        this.map.put(name, CodecUtils.shortType(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder shortType(String name, int offset, EndianPolicy endianPolicy) {
        this.map.put(name, CodecUtils.shortType(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder int16Type(String name, int offset) {
        this.map.put(name, CodecUtils.int16Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder int16Type(String name, int offset, EndianPolicy endianPolicy) {
        this.map.put(name, CodecUtils.int16Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder int32Type(String name, int offset) {
        this.map.put(name, CodecUtils.int32Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder int32Type(String name, int offset, EndianPolicy endianPolicy) {
        this.map.put(name, CodecUtils.int32Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder int64Type(String name, int offset) {
        this.map.put(name, CodecUtils.int64Type(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder int64Type(String name, int offset, EndianPolicy endianPolicy) {
        this.map.put(name, CodecUtils.int64Type(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder floatType(String name, int offset) {
        this.map.put(name, CodecUtils.floatType(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder floatType(String name, int offset, EndianPolicy endianPolicy) {
        this.map.put(name, CodecUtils.floatType(this.bytes, offset, endianPolicy));

        return this;
    }

    public Decoder doubleType(String name, int offset) {
        this.map.put(name, CodecUtils.doubleType(this.bytes, offset, this.endianPolicy));

        return this;
    }

    public Decoder doubleType(String name, int offset, EndianPolicy endianPolicy) {
        this.map.put(name, CodecUtils.doubleType(this.bytes, offset, endianPolicy));

        return this;
    }

    public Map<String, Object> get() {
        return this.map;
    }

    public <T> T mapTo(Class<T> clazz) {
        try {
            T obj = clazz.newInstance();

            val fieldMap = Arrays.stream(clazz.getDeclaredFields())
                    .peek(f -> f.setAccessible(true))
                    .collect(Collectors.toMap(Field::getName, Function.identity()));

            for (val entry : this.map.entrySet()) {
                String name = entry.getKey();
                Object value = entry.getValue();

                if (fieldMap.containsKey(name)) {
                    fieldMap.get(name).set(obj, value);
                    continue;
                }
            }

            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DecodingException(String.format("Fail mapping to %s", clazz.getName()), e);
        }
    }
}
