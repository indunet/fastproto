package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.decoder.LongDecoder;
import org.indunet.fastproto.encoder.LongEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @version 1.0
 */
@DataType
@Decoder(LongDecoder.class)
@Encoder(LongEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LongType {
    int SIZE = Long.SIZE >> 3;
    long MAX = Long.MAX_VALUE;
    long MIN = Long.MIN_VALUE;

    int byteOffset();
}
