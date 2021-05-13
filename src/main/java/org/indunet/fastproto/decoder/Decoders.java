package org.indunet.fastproto.decoder;

import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.TypeDecoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class Decoders {
    protected static ConcurrentHashMap<Class<? extends TypeDecoder>, TypeDecoder> deocders = new ConcurrentHashMap<>();
    protected static ConcurrentHashMap<Class<? extends Function>, Function> formulas = new ConcurrentHashMap<>();

    public static <T> TypeDecoder<T> getDecoder(Class<? extends TypeDecoder<T>> clazz) {
        return deocders.computeIfPresent(clazz, (c, __) -> {
            try {
                return c.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public static <T, R> Function<DecodeContext, R> getDecoder(Class<? extends TypeDecoder<T>> clazz, Function<T, R> formula) {
        return (DecodeContext c) -> formula.apply(getDecoder(clazz).decode(c));
    }
}
