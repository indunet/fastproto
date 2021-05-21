package org.indunet.fastproto.encoder;

import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Deng Ran
 * @version 1.0
 */
public class Encoders {
    protected static ConcurrentHashMap<Class<? extends TypeEncoder>, TypeEncoder> encoders = new ConcurrentHashMap<>();
    protected static ConcurrentHashMap<Class<? extends Function>, Function> formulas = new ConcurrentHashMap<>();

    public static Consumer<EncodeContext> getEncoder(Class<? extends TypeEncoder> clazz) {
        return encoders.computeIfAbsent(clazz, c -> {
            try {
                return c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new EncodeException(
                        MessageFormat.format(EncodeError.FAIL_INITIALIZING_ENCODER.getMessage(), c.getName()), e);
            }
        })::encode;
    }

    public static <T, R> Function<? super T, ? extends R> getFormula(Class<? extends Function> clazz) {
        return formulas.computeIfAbsent(clazz, c -> {
            try {
                return c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new EncodeException(
                        MessageFormat.format(EncodeError.FAIL_INITIALIZING_ENCODE_FORMULA.getMessage(), c.getName()), e);
            }
        });
    }
}
