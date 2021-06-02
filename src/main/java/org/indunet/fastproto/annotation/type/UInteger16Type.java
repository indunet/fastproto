package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.decoder.UInteger16Decoder;
import org.indunet.fastproto.encoder.UInteger16Encoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Integer type, corresponding to Java Integer/int.
 *
 * @author Deng Ran
 * @since 1.2.0
 * @see Type
 */
@Type
@Decoder(UInteger16Decoder.class)
@Encoder(UInteger16Encoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UInteger16Type {
    int SIZE = Short.SIZE >> 3;
    int MAX_VALUE = Short.MAX_VALUE - Short.MIN_VALUE;
    int MIN_VALUE = 0;

    int value();
}
