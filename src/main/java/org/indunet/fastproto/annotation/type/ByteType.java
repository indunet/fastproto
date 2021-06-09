package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.ByteDecoder;
import org.indunet.fastproto.encoder.ByteEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Byte type, corresponding to Java Byte/byte.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.0.0
 */
@TypeFlag
@Decoder(ByteDecoder.class)
@Encoder(ByteEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ByteType {
    Type[] JAVA_TYPES = {Byte.class, Byte.TYPE};
    int SIZE = Byte.SIZE >> 3;
    int MAX_VALUE = Byte.MAX_VALUE;
    int MIN_VALUE = Byte.MIN_VALUE;

    int value();

    Class<? extends Function<Byte, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Byte>>[] beforeEncode() default {};
}
