package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.IntegerDecoder;
import org.indunet.fastproto.encoder.IntegerEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Integer type, corresponding to Java Integer/int.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.0.0
 */
@TypeFlag
@Decoder(IntegerDecoder.class)
@Encoder(IntegerEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerType {
    Type[] JAVA_TYPES = {Integer.class, Integer.TYPE};
    int SIZE = Integer.SIZE >> 3;
    int MAX_VALUE = Integer.MAX_VALUE;
    int MIN_VALUE = Integer.MIN_VALUE;

    int value();

    Class<? extends Function<Integer, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Integer>>[] beforeEncode() default {};
}
