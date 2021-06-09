package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.Integer16Decoder;
import org.indunet.fastproto.encoder.Integer16Encoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Integer16 type, corresponding to Java Integer/int.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.2.0
 */
@TypeFlag
@Decoder(Integer16Decoder.class)
@Encoder(Integer16Encoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Integer16Type {
    Type[] JAVA_TYPES = {Integer.class, Integer.TYPE};
    int SIZE = Short.SIZE >> 3;
    int MAX_VALUE = Short.MAX_VALUE;
    int MIN_VALUE = Short.MIN_VALUE;

    int value();

    Class<? extends Function<Integer, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Integer>>[] beforeEncode() default {};
}
