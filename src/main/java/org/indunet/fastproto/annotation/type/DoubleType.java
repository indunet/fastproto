package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.DoubleDecoder;
import org.indunet.fastproto.encoder.DoubleEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Double type, corresponding to Java Double/double.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.0.0
 */
@TypeFlag
@Decoder(DoubleDecoder.class)
@Encoder(DoubleEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleType {
    Type[] JAVA_TYPES = {Double.class, Double.TYPE};
    int SIZE = Double.SIZE >> 3;
    double MAX_VALUE = Double.MAX_VALUE;
    double MIN_VALUE = Double.MIN_VALUE;

    int value();

    Class<? extends Function<Double, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Double>>[] beforeEncode() default {};
}
