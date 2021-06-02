package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.decoder.DoubleDecoder;
import org.indunet.fastproto.encoder.DoubleEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Double type, corresponding to Java Double/double.
 *
 * @author Deng Ran
 * @see Type
 * @since 1.0.0
 */
@Type
@Decoder(DoubleDecoder.class)
@Encoder(DoubleEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleType {
    int SIZE = Double.SIZE >> 3;
    double MAX_VALUE = Double.MAX_VALUE;
    double MIN_VALUE = Double.MIN_VALUE;

    int value();
}
