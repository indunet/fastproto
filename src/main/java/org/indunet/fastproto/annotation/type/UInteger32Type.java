package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.UInteger32Decoder;
import org.indunet.fastproto.encoder.UInteger32Encoder;

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
 * @since 1.2.0
 */
@TypeFlag
@Decoder(UInteger32Decoder.class)
@Encoder(UInteger32Encoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UInteger32Type {
    Type[] JAVA_TYPES = {Long.class, Long.TYPE};
    int SIZE = Integer.SIZE >> 3;
    long MAX_VALUE = (long) Integer.MAX_VALUE - Integer.MIN_VALUE;
    int MIN_VALUE = 0;

    int value();

    Class<? extends Function<Long, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Long>>[] beforeEncode() default {};
}
