package org.indunet.fastproto.codec;

import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteBuffer;
import org.indunet.fastproto.annotation.BoolArrayType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Boolean array type codec.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public class BoolArrayCodec implements Codec<boolean[]> {
    @Override
    public boolean[] decode(CodecContext context, byte[] bytes) {
        try {
            val type = context.getDataTypeAnnotation(BoolArrayType.class);
            val bitOrder = context.getBitOrder(type::bitOrder);

            return this.decode(bytes, type.byteOffset(), type.bitOffset(), type.length(), bitOrder);
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding boolean array type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBuffer buffer, boolean[] values) {
        try {
            val type = context.getDataTypeAnnotation(BoolArrayType.class);
            val bitOrder = context.getBitOrder(type::bitOrder);

            int byteIndex = type.byteOffset() + type.bitOffset() / 8;
            int bitIndex = type.bitOffset() % 8;

            for (boolean value : values) {
                if (bitOrder == BitOrder.MSB_0) {
                    if (value) {
                        buffer.orEq(byteIndex, (byte) (0x80 >>> bitIndex));
                    } else {
                        buffer.andEq(byteIndex, (byte) ~(0x80 >>> bitIndex));
                    }
                } else if (bitOrder == BitOrder.LSB_0) {
                    if (value) {
                        buffer.orEq(byteIndex, (byte) (0x01 << bitIndex));
                    } else {
                        buffer.andEq(byteIndex, (byte) ~(0x01 << bitIndex));
                    }
                } else {
                    throw new IllegalArgumentException("mode must be MSB_0 or LSB_0");
                }

                bitIndex++;

                if (bitIndex == 8) {
                    bitIndex = 0;
                    byteIndex++;
                }
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding boolean array type.", e);
        }
    }

    public boolean[] decode(byte[] bytes, int byteOffset, int bitOffset, int length, BitOrder bitOrder) {
        boolean[] result = new boolean[length];
        int byteIndex = byteOffset + bitOffset / 8;
        int bitIndex = bitOffset % 8;

        for (int i = 0; i < length; i++) {
            int value = 0;

            if (bitOrder == BitOrder.MSB_0) {
                value = bytes[byteIndex] & (0x80 >>> bitIndex);
            } else if (bitOrder == BitOrder.LSB_0) {
                value = bytes[byteIndex] & (0x01 << bitIndex);
            } else {
                throw new IllegalArgumentException("mode must be MSB_0 or LSB_0");
            }

            result[i] = value != 0;
            bitIndex++;

            if (bitIndex == 8) {
                bitIndex = 0;
                byteIndex++;
            }
        }
        return result;
    }

    public void encode(byte[] bytes, int byteOffset, int bitOffset, BitOrder bitOrder, boolean[] values) {
        int byteIndex = byteOffset + bitOffset / 8;
        int bitIndex = bitOffset % 8;

        for (boolean value : values) {
            if (bitOrder == BitOrder.MSB_0) {
                if (value) {
                    bytes[byteIndex] |= 0x80 >>> bitIndex;
                } else {
                    bytes[byteIndex] &= ~(0x80 >>> bitIndex);
                }
            } else if (bitOrder == BitOrder.LSB_0) {
                if (value) {
                    bytes[byteIndex] |= 0x01 << bitIndex;
                } else {
                    bytes[byteIndex] &= ~(0x01 << bitIndex);
                }
            } else {
                throw new IllegalArgumentException("mode must be MSB_0 or LSB_0");
            }

            bitIndex++;

            if (bitIndex == 8) {
                bitIndex = 0;
                byteIndex++;
            }
        }
    }

    public class WrapperCodec implements Codec<Boolean[]> {
        @Override
        public Boolean[] decode(CodecContext context, byte[] bytes) {
            boolean[] bools = BoolArrayCodec.this.decode(context, bytes);
            Boolean[] results = new Boolean[bools.length];

            IntStream.range(0, bools.length)
                    .forEach(i -> results[i] = bools[i]);

            return results;
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Boolean[] values) {
            boolean[] bools = new boolean[values.length];

            IntStream.range(0, values.length)
                    .forEach(i -> bools[i] = values[i]);

            BoolArrayCodec.this.encode(context, buffer, bools);
        }
    }

    public class CollectionCodec implements Codec<Collection<Boolean>> {
        @Override
        public Collection<Boolean> decode(CodecContext context, byte[] bytes) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Boolean> collection = CollectionUtils.newInstance(type);

                for (val value : BoolArrayCodec.this.decode(context, bytes)) {
                    collection.add(value);
                }

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBuffer buffer, Collection<Boolean> collection) {
            val bools = new boolean[collection.size()];
            val values = collection.stream()
                    .toArray(Boolean[]::new);

            IntStream.range(0, values.length)
                    .forEach(i -> bools[i] = values[i]);

            BoolArrayCodec.this.encode(context, buffer, bools);
        }
    }
}
