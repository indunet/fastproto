package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.BinaryDecoder;
import org.indunet.fastproto.encoder.BinaryEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Binary type, corresponding to Java byte array.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.0.0
 */
@TypeFlag
@Decoder(BinaryDecoder.class)
@Encoder(BinaryEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BinaryType {
    Type[] JAVA_TYPES = {byte[].class};

    int value();

    int length() default -1;

    TimeUnit unit() default TimeUnit.MILLISECONDS;

    Class<? extends Function<byte[], ?>>[] afterDecode() default {};

    Class<? extends Function<?, byte[]>>[] beforeEncode() default {};
}
