package org.indunet.fastproto.annotation.type;

/**
 * @author Deng Ran
 * @version 1.1.0
 */

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.decoder.ByteDecoder;
import org.indunet.fastproto.encoder.ByteEncoder;

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
@Decoder(ByteDecoder.class)
@Encoder(ByteEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CharacterType {
    int size = Character.SIZE >> 3;

    int value();
}
