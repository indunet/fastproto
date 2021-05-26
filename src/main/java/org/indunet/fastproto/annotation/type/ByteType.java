package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.decoder.ByteDecoder;
import org.indunet.fastproto.encoder.ByteEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Type
@Decoder(ByteDecoder.class)
@Encoder(ByteEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ByteType {
    int SIZE = Byte.SIZE >> 3;
    int MAX = Byte.MAX_VALUE;
    int MIN = Byte.MIN_VALUE;

    int value();
}
