package org.indunet.fastproto.codec;

import lombok.val;
import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.StructArrayType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.graph.Graph;
import org.indunet.fastproto.graph.Resolver;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Codec for struct arrays/collections with fixed-size element.
 * Assumption: element size can be inferred from its annotated fields (max(offset+size)).
 */
public class StructArrayCodec<T> implements Codec<Object> {
    @Override
    public Object decode(CodecContext context, ByteBufferInputStream inputStream) {
        try {
            StructArrayType type = context.getDataTypeAnnotation(StructArrayType.class);
            Class<?> elemClass = type.element();
            Graph elemGraph = Resolver.resolve(elemClass);
            int elemSize = inferFixedSize(elemGraph);

            int count = type.length();
            int base = inputStream.toByteBuffer().reverse(type.offset());
            List<Object> list = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                byte[] slice = inputStream.toByteBuffer().toBytes();
                int start = base + i * elemSize;
                byte[] elemBytes = java.util.Arrays.copyOfRange(slice, start, start + elemSize);
                Object elem = FastProto.decode(elemBytes, (Class) elemClass);
                list.add(elem);
            }

            Class<?> ft = context.getField().getType();
            if (ft.isArray()) {
                Object arr = Array.newInstance(elemClass, count);
                for (int i = 0; i < count; i++) {
                    Array.set(arr, i, list.get(i));
                }
                return arr;
            } else if (Collection.class.isAssignableFrom(ft)) {
                Collection coll = new java.util.ArrayList(count);
                coll.addAll(list);
                return coll;
            } else {
                throw new DecodingException("StructArrayType only supports arrays or collections");
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new DecodingException("Fail decoding struct array type.", e);
        }
    }

    @Override
    public void encode(CodecContext context, ByteBufferOutputStream outputStream, Object value) {
        try {
            StructArrayType type = context.getDataTypeAnnotation(StructArrayType.class);
            Class<?> elemClass = type.element();
            Graph elemGraph = Resolver.resolve(elemClass);
            int elemSize = inferFixedSize(elemGraph);

            int count = type.length();
            int base = outputStream.toByteBuffer().reverse(type.offset());

            List<Object> list = new ArrayList<>();
            if (value.getClass().isArray()) {
                int n = Array.getLength(value);
                for (int i = 0; i < Math.min(count, n); i++) {
                    list.add(Array.get(value, i));
                }
            } else if (value instanceof Collection) {
                int i = 0;
                for (Object o : (Collection) value) {
                    if (i++ >= count) break;
                    list.add(o);
                }
            } else {
                throw new EncodingException("StructArrayType only supports arrays or collections");
            }

            for (int i = 0; i < list.size(); i++) {
                Object elem = list.get(i);
                byte[] bytes = FastProto.encode(elem, elemSize);
                for (int b = 0; b < bytes.length; b++) {
                    outputStream.writeByte(base + i * elemSize + b, bytes[b]);
                }
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new EncodingException("Fail encoding struct array type.", e);
        }
    }

    private static int inferFixedSize(Graph graph) {
        // naive: take max of (offset + size/length) across valid field refs
        int max = 0;
        for (val r : graph.getValidReferences()) {
            int off = r.getProtocolType().offset();
            int span = 0;
            if (r.getProtocolType().size() > 0) {
                span = r.getProtocolType().size();
            } else if (r.getProtocolType().length() > 0) {
                span = r.getProtocolType().length();
            }
            if (off + span > max) max = off + span;
        }
        return max;
    }
} 