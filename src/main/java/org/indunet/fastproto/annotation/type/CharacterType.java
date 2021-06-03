package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.decoder.ByteDecoder;
import org.indunet.fastproto.decoder.CharacterDecoder;
import org.indunet.fastproto.encoder.ByteEncoder;
import org.indunet.fastproto.encoder.CharacterEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

/**
 * Character type, corresponding to Java Character/char.
 *
 * @author Deng Ran
 * @see Type
 * @since 1.0.0
 */
@Type
@Decoder(CharacterDecoder.class)
@Encoder(CharacterEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CharacterType {
    int SIZE = Character.SIZE >> 3;

    int value();

    Class<? extends Function<Character, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Character>>[] beforeEncode() default {};
}
