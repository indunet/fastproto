package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.decoder.IntegerDecoder;
import org.indunet.fastproto.encoder.IntegerEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @see Type
 * @since 1.0.0
 */
@Type
@Decoder(IntegerDecoder.class)
@Encoder(IntegerEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerType {
    int SIZE = Integer.SIZE >> 3;
    int MAX_VALUE = Integer.MAX_VALUE;
    int MIN_VALUE = Integer.MIN_VALUE;

    int value();
}
