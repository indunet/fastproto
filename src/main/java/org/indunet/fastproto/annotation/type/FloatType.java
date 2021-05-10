package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.decoder.FloatDecoder;
import org.indunet.fastproto.encoder.FloatEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @version 1.0
 */
@DataType
@Decoder(FloatDecoder.class)
@Encoder(FloatEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FloatType {
    int SIZE = Float.SIZE >> 3;
    float MAX = Float.MAX_VALUE;
    float MIN = Float.MIN_VALUE;

    int byteOffset();
}
