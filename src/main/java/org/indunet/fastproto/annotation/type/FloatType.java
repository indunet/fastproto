package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.decoder.FloatDecoder;
import org.indunet.fastproto.encoder.FloatEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

/**
 * Float type, corresponding to Java Float/float.
 *
 * @author Deng Ran
 * @see Type
 * @since 1.0.0
 */
@Type
@Decoder(FloatDecoder.class)
@Encoder(FloatEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FloatType {
    int SIZE = Float.SIZE >> 3;
    float MAX_VALUE = Float.MAX_VALUE;
    float MIN_VALUE = Float.MIN_VALUE;

    int value();

    Class<? extends Function<Float, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Float>>[] beforeEncode() default {};
}
