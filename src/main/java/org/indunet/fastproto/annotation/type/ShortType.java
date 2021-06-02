package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.decoder.ShortDecoder;
import org.indunet.fastproto.encoder.ShortEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Short type, corresponding to Java Short/short.
 *
 * @author Deng Ran
 * @see Type
 * @since 1.0.0
 */
@Type
@Decoder(ShortDecoder.class)
@Encoder(ShortEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ShortType {
    int SIZE = Short.SIZE >> 3;
    int MAX_VALUE = Short.MAX_VALUE;
    int MIN_VALUE = Short.MIN_VALUE;

    int value();

}
