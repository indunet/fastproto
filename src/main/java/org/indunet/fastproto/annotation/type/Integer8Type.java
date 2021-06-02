package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.decoder.Integer8Decoder;
import org.indunet.fastproto.encoder.Integer8Encoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Integer8 type, corresponding to Java Integer/int.
 *
 * @author Deng Ran
 * @since 1.2.0
 * @see Type
 */
@Type
@Decoder(Integer8Decoder.class)
@Encoder(Integer8Encoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Integer8Type {
    int SIZE = Byte.SIZE >> 3;
    int MAX_VALUE = Byte.MAX_VALUE;
    int MIN_VALUE = Byte.MIN_VALUE;

    int value();
}
