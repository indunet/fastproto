package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.StringDecoder;
import org.indunet.fastproto.encoder.StringEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * String type, corresponding to Java String.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.1.0
 */
@TypeFlag
@Decoder(StringDecoder.class)
@Encoder(StringEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StringType {
    Type[] JAVA_TYPES = {String.class};

    int value();

    int length() default -1;

    String charsetName() default "UTF-8";

    Class<? extends Function<String, ?>>[] afterDecode() default {};

    Class<? extends Function<?, String>>[] beforeEncode() default {};
}
