package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.decoder.ShortDecoder;
import org.indunet.fastproto.encoder.ShortEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @version 1.0
 */
@DataType
@Decoder(ShortDecoder.class)
@Encoder(ShortEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ShortType {
    int SIZE = Short.SIZE >> 3;
    int MAX = Short.MAX_VALUE;
    int MIN = Short.MIN_VALUE;

    int byteOffset();

}
