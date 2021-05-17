package org.indunet.fastproto.decoder;

import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.TypeDecoder;
import org.indunet.fastproto.encoder.EncodeContext;
import org.indunet.fastproto.encoder.TypeEncoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class Decoders {
    protected static ConcurrentHashMap<Class<? extends TypeDecoder>, TypeDecoder> deocders = new ConcurrentHashMap<>();
    protected static ConcurrentHashMap<Class<? extends Function>, Function> formulas = new ConcurrentHashMap<>();

    public static <T> TypeDecoder<T> getDecoder(Class<? extends TypeDecoder> clazz) {
        return deocders.computeIfAbsent(clazz, (c) -> {
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

    public static <T> TypeDecoder<T> getDecoder(Class<? extends TypeDecoder> clazz, Class<T> type) {
        return deocders.computeIfAbsent(clazz, (c) -> {
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

    public static <T, R> Function<DecodeContext, R> getDecoder(Class<? extends TypeDecoder> clazz, Function<T, R> formula) {
        TypeDecoder<T> decoder = getDecoder(clazz);
        return (DecodeContext c) -> formula.apply((T) getDecoder(clazz).decode(c));
    }

    public static <T> TypeEncoder<T> getEncoder(Class<? extends TypeEncoder> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
