package org.indunet.fastproto;

import lombok.val;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.util.CodecUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Binary decoder.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
class Decoder {
    protected byte[] bytes;
    protected Map<String, Object> map = new HashMap<>();
    protected EndianPolicy endianPolicy = EndianPolicy.LITTLE;
    protected int ordinal = 0;
    protected Object last;

    Decoder(byte[] bytes) {
        this.bytes = bytes;
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

    public Decoder defaultEndian(EndianPolicy endianPolicy) {
        this.endianPolicy = endianPolicy;

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
        return this.put(CodecUtils.uint16Type(this.bytes, offset, this.endianPolicy));
    }

    public Decoder uint16Type(int offset, String name) {
        return this.put(name, CodecUtils.uint16Type(this.bytes, offset, this.endianPolicy));
    }

    public Decoder uint16Type(int offset, EndianPolicy endianPolicy) {
        return this.put(CodecUtils.uint16Type(this.bytes, offset, endianPolicy));
    }

    public Decoder uint16Type(int offset, EndianPolicy endianPolicy, String name) {
        return this.put(name, CodecUtils.uint16Type(this.bytes, offset, endianPolicy));
    }

    public Decoder uint32Type(int offset) {
        return this.put(CodecUtils.uint32Type(this.bytes, offset, this.endianPolicy));
    }

    public Decoder uint32Type(int offset, String name) {
        return this.put(name, CodecUtils.uint32Type(this.bytes, offset, this.endianPolicy));
    }

    public Decoder uint32Type(int offset, EndianPolicy endianPolicy) {
        return this.put(CodecUtils.uint32Type(this.bytes, offset, endianPolicy));
    }

    public Decoder uint32Type(int offset, EndianPolicy endianPolicy, String name) {
        return this.put(name, CodecUtils.uint32Type(this.bytes, offset, endianPolicy));
    }

    public Decoder uint64Type(int offset) {
        return this.put(CodecUtils.uint64Type(this.bytes, offset, this.endianPolicy));
    }

    public Decoder uint64Type(int offset, String name) {
        return this.put(name, CodecUtils.uint64Type(this.bytes, offset, this.endianPolicy));
    }

    public Decoder uint64Type(int offset, EndianPolicy endianPolicy) {
        return this.put(CodecUtils.uint64Type(this.bytes, offset, endianPolicy));
    }

    public Decoder uint64Type(int offset, EndianPolicy endianPolicy, String name) {
        return this.put(name, CodecUtils.uint64Type(this.bytes, offset, endianPolicy));
    }

    public Decoder byteType(int offset) {
        return this.put(CodecUtils.byteType(this.bytes, offset));
    }

    public Decoder byteType(int offset, String name) {
        return this.put(name, CodecUtils.byteType(this.bytes, offset));
    }

    public Decoder int8Type(int offset) {
        return this.put(CodecUtils.int8Type(this.bytes, offset));
    }

    public Decoder int8Type(int offset, String name) {
        return this.put(name, CodecUtils.int8Type(this.bytes, offset));
    }

    public Decoder shortType(int offset) {
        return this.put(CodecUtils.shortType(this.bytes, offset, this.endianPolicy));
    }

    public Decoder shortType(int offset, String name) {
        return this.put(name, CodecUtils.shortType(this.bytes, offset, this.endianPolicy));
    }

    public Decoder shortType(int offset, EndianPolicy endianPolicy) {
        return this.put(CodecUtils.shortType(this.bytes, offset, endianPolicy));
    }

    public Decoder shortType(int offset, EndianPolicy endianPolicy, String name) {
        return this.put(name, CodecUtils.shortType(this.bytes, offset, endianPolicy));
    }

    public Decoder int16Type(int offset) {
        return this.put(CodecUtils.int16Type(this.bytes, offset, this.endianPolicy));
    }

    public Decoder int16Type(int offset, String name) {
        return this.put(name, CodecUtils.int16Type(this.bytes, offset, this.endianPolicy));
    }

    public Decoder int16Type(int offset, EndianPolicy endianPolicy) {
        return this.put(CodecUtils.int16Type(this.bytes, offset, endianPolicy));
    }

    public Decoder int16Type(int offset, EndianPolicy endianPolicy, String name) {
        return this.put(name, CodecUtils.int16Type(this.bytes, offset, endianPolicy));
    }

    public Decoder int32Type(int offset) {
        return this.put(CodecUtils.int32Type(this.bytes, offset, this.endianPolicy));
    }

    public Decoder int32Type(int offset, String name) {
        return this.put(name, CodecUtils.int32Type(this.bytes, offset, this.endianPolicy));
    }

    public Decoder int32Type(int offset, EndianPolicy endianPolicy) {
        return this.put(CodecUtils.int32Type(this.bytes, offset, endianPolicy));
    }

    public Decoder int32Type(int offset, EndianPolicy endianPolicy, String name) {
        return this.put(name, CodecUtils.int32Type(this.bytes, offset, endianPolicy));
    }

    public Decoder int64Type(int offset) {
        return this.put(CodecUtils.int64Type(this.bytes, offset, this.endianPolicy));
    }

    public Decoder int64Type(int offset, String name) {
        return this.put(name, CodecUtils.int64Type(this.bytes, offset, this.endianPolicy));
    }

    public Decoder int64Type(int offset, EndianPolicy endianPolicy) {
        return this.put(CodecUtils.int64Type(this.bytes, offset, endianPolicy));
    }

    public Decoder int64Type(int offset, EndianPolicy endianPolicy, String name) {
        return this.put(name, CodecUtils.int64Type(this.bytes, offset, endianPolicy));
    }

    public Decoder floatType(int offset) {
        return this.put(CodecUtils.floatType(this.bytes, offset, this.endianPolicy));
    }

    public Decoder floatType(int offset, String name) {
        return this.put(name, CodecUtils.floatType(this.bytes, offset, this.endianPolicy));
    }

    public Decoder floatType(int offset, EndianPolicy endianPolicy) {
        return this.put(CodecUtils.floatType(this.bytes, offset, endianPolicy));
    }

    public Decoder floatType(int offset, EndianPolicy endianPolicy, String name) {
        return this.put(name, CodecUtils.floatType(this.bytes, offset, endianPolicy));
    }

    public Decoder doubleType(int offset) {
        return this.put(CodecUtils.doubleType(this.bytes, offset, this.endianPolicy));
    }

    public Decoder doubleType(int offset, String name) {
        return this.put(name, CodecUtils.doubleType(this.bytes, offset, this.endianPolicy));
    }

    public Decoder doubleType(int offset, EndianPolicy endianPolicy) {
        return this.put(CodecUtils.doubleType(this.bytes, offset, endianPolicy));
    }

    public Decoder doubleType(int offset, EndianPolicy endianPolicy, String name) {
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

    @Deprecated
    public Map<String, Object> get() {
        return this.map;
    }

    public Map<String, Object> getAsMap() {
        return this.map;
    }

    public <T> T mapTo(Class<T> clazz) {
        try {
            T obj = clazz.newInstance();

            val fields = Arrays.stream(clazz.getDeclaredFields())
                    .peek(f -> f.setAccessible(true))
                    .collect(Collectors.toList());

            for (val f : fields) {
                if (this.map.containsKey(f.getName())) {
                    f.set(obj, this.map.get(f.getName()));
                }
            }

            return obj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DecodingException(String.format("Fail mapping to Object %s", clazz.getName()), e);
        }
    }
}
