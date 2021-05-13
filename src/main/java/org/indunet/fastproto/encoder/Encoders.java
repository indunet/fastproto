package org.indunet.fastproto.encoder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class Encoders {
    protected static ConcurrentHashMap<Class<? extends TypeEncoder>, TypeEncoder> deocders = new ConcurrentHashMap<>();
    protected static ConcurrentHashMap<Class<? extends Function>, Function> formulas = new ConcurrentHashMap<>();

    public static <T> Consumer<EncodeContext<T>> getEncoder(Class<? extends TypeEncoder<T>> clazz) {
        return null;
    }

    public static <T, R> Consumer<EncodeContext<T>> getEncoder(Class<? extends TypeEncoder<T>> encoderClass, Class<? extends Function<T, R>> formulaClass) {
        return null;
    }
}
