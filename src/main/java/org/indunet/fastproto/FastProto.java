package org.indunet.fastproto;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.annotation.Compress;
import org.indunet.fastproto.compress.Compressors;
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
    public static <T> T decode(@NonNull byte[] datagram, @NonNull Class<T> clazz) {
        return decode(datagram, clazz, true);
    }

    /**
     * Convert binary datagram into object.
     *
     * @param datagram binary message
     * @param clazz    deserialized object
     * @param enableCompress enable compress or not
     * @return object.
     */
    public static <T> T decode(@NonNull byte[] datagram, @NonNull Class<T> clazz, boolean enableCompress) {
        if (enableCompress && clazz.isAnnotationPresent(Compress.class)) {
            val compress = clazz.getAnnotation(Compress.class);
            val compressor = Compressors.get(compress);

            datagram = compressor.decompress(datagram);
        }

        TypeAssist assist = assists.computeIfAbsent(clazz, c -> TypeAssist.of(c));
        List<DecodeContext> contexts = assist.toDecodeContexts(datagram);
        T object = contexts.stream()
                .map(DecodeContext::getObject)
                .map(clazz::cast)
                .findFirst()
                .get();

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

        return object;
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object   serialized object
     * @param datagram binary message
     */
    @Deprecated
    public static void encode(@NonNull Object object, @NonNull byte[] datagram) {
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
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object   serialized object
     * @param length the length of the datagram.
     * @return binary datagram.
     */
    public static byte[] encode(@NonNull Object object, int length) {
        return encode(object, length, true);
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object   serialized object
     * @param length the length of the datagram.
     * @param enableCompress enable compress or not
     * @return binary datagram.
     */
    public static byte[] encode(@NonNull Object object, int length, boolean enableCompress) {
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

        if (enableCompress && object.getClass().isAnnotationPresent(Compress.class)) {
            val compress = object.getClass().getAnnotation(Compress.class);
            val compressor = Compressors.get(compress);

            return compressor.compress(datagram);
        } else {
            return datagram;
        }
    }
}
