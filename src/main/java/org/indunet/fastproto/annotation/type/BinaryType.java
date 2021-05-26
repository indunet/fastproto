package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.decoder.BinaryDecoder;
import org.indunet.fastproto.encoder.BinaryEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Type
@Decoder(BinaryDecoder.class)
@Encoder(BinaryEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BinaryType {
    int byteOffset();

    int length();
}
