package org.indunet.fastproto;

import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.Decoders;
import org.indunet.fastproto.tuple.Tuple;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class FastProto {
    protected static ConcurrentHashMap<Class<?>, TypeAssist> assists = new ConcurrentHashMap<>();

    // TODO, decode method.
    public static <T> T decode(byte[] datagram, Class<T> clazz) {
        Objects.requireNonNull(datagram);
        Objects.requireNonNull(clazz);

        TypeAssist assist = assists.computeIfAbsent(clazz, c -> TypeAssist.create(c));
        List<DecodeContext> contexts = assist.toDecodeContexts(datagram);
        T object = contexts.stream()
                .map(DecodeContext::getObject)
                .map(clazz::cast)
                .findFirst()
                .get();

        contexts.stream()
                .map(c -> Tuple.get(
                        Decoders.getDecoder(
                                c.getTypeAssist().getDecoderClass(),
                                c.getTypeAssist().getDecodeFormula()),
                        c))
                .map(t -> Tuple.get(
                        t.getC1().apply(t.getC2()),
                        t.getC2()))
                .map(t -> t.append(t.getC2().getTypeAssist().getDecodeFormula()))
                .forEach(t -> {
                    Field f = t.getC2().getTypeAssist().getField();
                    Object o = t.getC2().getObject();

                    try {
                        f.set(o, t.getC1());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        return object;
    }

    // TODO, encode method.
    public static void encode(Object object, byte[] datagram) {
        Objects.requireNonNull(object);
        Objects.requireNonNull(datagram);
    }
}
