package org.indunet.fastproto;

import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.Decoders;
import org.indunet.fastproto.encoder.EncodeContext;
import org.indunet.fastproto.encoder.Encoders;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class FastProto {
    protected static ConcurrentHashMap<Class<?>, TypeAssist> assists = new ConcurrentHashMap<>();

    /**
     * Convert binary message into object.
     *
     * @param datagram binary message
     * @param clazz deserialized object
     * @return deserialize object instance
     */
    public static <T> T decode(byte[] datagram, Class<T> clazz) {
        Objects.requireNonNull(datagram);
        Objects.requireNonNull(clazz);

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
     * Convert object into binary message.
     *
     * @param object serialized object
     * @param datagram binary message
     * @return void
     */
    public static void encode(Object object, byte[] datagram) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(datagram);

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
}
