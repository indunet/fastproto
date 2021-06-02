package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.decoder.BooleanDecoder;
import org.indunet.fastproto.encoder.BooleanEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Boolean type, corresponding to Java Boolean/boolean.
 *
 * @author Deng Ran
 * @see Type
 * @since 1.0.0
 */
@Type
@Decoder(BooleanDecoder.class)
@Encoder(BooleanEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BooleanType {
    int MAX_BIT_OFFSET = 7;
    int MIN_BIT_OFFSET = 0;

    int byteOffset();

    int bitOffset();
}
