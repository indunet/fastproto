package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.UInteger64Decoder;
import org.indunet.fastproto.encoder.UInteger64Encoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

/**
 * uint64 type, corresponding to Java BigInteger.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.5.0
 */
@TypeFlag
@Decoder(UInteger64Decoder.class)
@Encoder(UInteger64Encoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UInteger64Type {
    Type[] JAVA_TYPES = {BigInteger.class};
    int SIZE = Long.SIZE >> 3;
    BigInteger MAX_VALUE = new BigInteger(String.valueOf(Long.MAX_VALUE))
            .subtract(new BigInteger(String.valueOf(Long.MIN_VALUE)));
    BigInteger MIN_VALUE = new BigInteger("0");

    int value();

    Class<? extends Function<BigDecimal, ?>>[] afterDecode() default {};

    Class<? extends Function<?, BigDecimal>>[] beforeEncode() default {};
}
