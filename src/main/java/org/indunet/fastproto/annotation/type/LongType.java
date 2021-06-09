package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.LongDecoder;
import org.indunet.fastproto.encoder.LongEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Long type, corresponding to Java Long/long.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.0.0
 */
@TypeFlag
@Decoder(LongDecoder.class)
@Encoder(LongEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LongType {
    Type[] JAVA_TYPES = {Long.class, Long.TYPE};
    int SIZE = Long.SIZE >> 3;
    long MAX_VALUE = Long.MAX_VALUE;
    long MIN_VALUE = Long.MIN_VALUE;

    int value();

    Class<? extends Function<Long, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Long>>[] beforeEncode() default {};
}
