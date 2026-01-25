package org.indunet.fastproto.codec;

import lombok.val;
import org.indunet.fastproto.annotation.BoolArrayType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Codec for Boolean array type.
 * This codec is responsible for encoding and decoding Boolean array types.
 * It provides support for both primitive boolean array and wrapper Boolean array, as well as collections of Boolean.
 * It is used in conjunction with the BoolArrayType annotation.
 *
 * @author Deng Ran
 * @since 3.8.3
 */
public class BoolArrayCodec implements Codec<boolean[]> {
    @Override
    public boolean[] decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            val type = context.getDataTypeAnnotation(BoolArrayType.class);
            val order = context.getBitOrder(type::bitOrder);

            boolean[] bools = new boolean[type.length()];
            int byteIndex = type.byteOffset() + type.bitOffset() / 8;
            int bitIndex = type.bitOffset() % 8;

            for (int i = 0; i < type.length(); i++) {
                bools[i] = inputStream.readBool(byteIndex, bitIndex, order);
                bitIndex++;

                if (bitIndex == 8) {
                    bitIndex = 0;
                    byteIndex++;
                }
            }

            return bools;
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding boolean array type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, boolean[] bools) {
        try {
            val type = context.getDataTypeAnnotation(BoolArrayType.class);
            val order = context.getBitOrder(type::bitOrder);

            int byteIndex = type.byteOffset() + type.bitOffset() / 8;
            int bitIndex = type.bitOffset() % 8;

            for (int i = 0; i < Math.min(type.length(), bools.length); i++) {
                outputStream.writeBool(byteIndex, bitIndex, order, bools[i]);
                bitIndex ++;

                if (bitIndex == 8) {
                    bitIndex = 0;
                    byteIndex++;
                }
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding boolean array type.", e);
        }
    }

    public class WrapperCodec implements Codec<Boolean[]> {
        @Override
        public Boolean[] decode(CodecContext context, ByteBufferInputStream inputStream) {
            boolean[] bools = BoolArrayCodec.this.decode(context, inputStream);
            Boolean[] results = new Boolean[bools.length];

            IntStream.range(0, bools.length)
                    .forEach(i -> results[i] = bools[i]);

            return results;
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Boolean[] values) {
            boolean[] bools = new boolean[values.length];

            IntStream.range(0, values.length)
                    .forEach(i -> bools[i] = values[i]);

            BoolArrayCodec.this.encode(context, outputStream, bools);
        }
    }

    public class CollectionCodec implements Codec<Collection<Boolean>> {
        @Override
        public Collection<Boolean> decode(CodecContext context, ByteBufferInputStream inputStream) {
            try {
                val type = (Class<? extends Collection>) context.getFieldType();
                Collection<Boolean> collection = CollectionUtils.newInstance(type);

                for (val value : BoolArrayCodec.this.decode(context, inputStream)) {
                    collection.add(value);
                }

                return collection;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding collection type of %s", context.getFieldType().toString()), e);
            }
        }

        @Override
        public void encode(CodecContext context, ByteBufferOutputStream outputStream, Collection<Boolean> collection) {
            val bools = new boolean[collection.size()];
            val values = collection.stream()
                    .toArray(Boolean[]::new);

            IntStream.range(0, values.length)
                    .forEach(i -> bools[i] = values[i]);

            BoolArrayCodec.this.encode(context, outputStream, bools);
        }
    }
}
