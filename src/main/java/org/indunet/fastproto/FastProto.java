package org.indunet.fastproto;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.annotation.EnableCompress;
import org.indunet.fastproto.compress.CompressorFactory;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.Decoders;
import org.indunet.fastproto.encoder.EncodeContext;
import org.indunet.fastproto.encoder.Encoders;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * FastProto API.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
public class FastProto {
    protected static ConcurrentHashMap<Class<?>, TypeAssist> assists = new ConcurrentHashMap<>();

    /**
     * Convert binary message into object.
     *
     * @param datagram binary message
     * @param clazz    deserialized object
     * @return deserialize object instance
     */
    public static <T> T parseFrom(@NonNull byte[] datagram, @NonNull Class<T> clazz) {
        return parseFrom(datagram, clazz, true);
    }

    /**
     * Convert binary datagram into object.
     *
     * @param datagram       binary message
     * @param clazz          deserialized object
     * @param enableCompress enable compress or not
     * @return object.
     */
    public static <T> T parseFrom(@NonNull byte[] datagram, @NonNull Class<T> clazz, boolean enableCompress) {
        if (enableCompress && clazz.isAnnotationPresent(EnableCompress.class)) {
            val compress = clazz.getAnnotation(EnableCompress.class);
            val compressor = CompressorFactory.create(compress);

            datagram = compressor.decompress(datagram);
        }

        TypeAssist assist = assists.computeIfAbsent(clazz, c -> TypeAssist.of(c));
        List<DecodeContext> contexts = assist.toDecodeContexts(datagram);

        contexts.parallelStream()
                .forEach(c -> {
                    TypeAssist a = c.getTypeAssist();
                    Function<DecodeContext, ?> func = Decoders.getDecoder(
                            a.getDecoderClass(),
                            a.getDecodeFormula());
                    Object value = func.apply(c);
                    Object o = c.getObject();
                    a.setValue(o, value);
                });

        return assist.getObject(clazz);
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object serialized object
     * @param length the length of the datagram.
     * @return binary datagram.
     */
    public static byte[] toByteArray(@NonNull Object object, int length) {
        return toByteArray(object, length, true);
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object         serialized object
     * @param length         the length of the datagram.
     * @param enableCompress enable compress or not
     * @return binary datagram.
     */
    public static byte[] toByteArray(@NonNull Object object, int length, boolean enableCompress) {
        byte[] datagram = new byte[length];

        TypeAssist assist = assists.computeIfAbsent(object.getClass(), c -> TypeAssist.of(c));
        List<EncodeContext> contexts = assist.toEncodeContexts(object, datagram);

        contexts.stream()
                .forEach(c -> {
                    if (c.getTypeAssist().getEncodeFormula() != null) {
                        Object o = Encoders.getFormula(c.getTypeAssist().getEncodeFormula())
                                .apply(c.getValue());
                        c.setValue(o);
                    }

                    Consumer<EncodeContext> consumer = Encoders.getEncoder(c.getTypeAssist().getEncoderClass());
                    consumer.accept(c);
                });

        if (enableCompress && object.getClass().isAnnotationPresent(EnableCompress.class)) {
            val annotation = object.getClass().getAnnotation(EnableCompress.class);
            val compressor = CompressorFactory.create(annotation);

            return compressor.compress(datagram);
        } else {
            return datagram;
        }
    }
}
