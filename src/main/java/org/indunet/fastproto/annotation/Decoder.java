package org.indunet.fastproto.annotation;

import org.indunet.fastproto.decoder.TypeDecoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify type decoder.
 *
 * @author Deng Ran
 * @see Encoder
 * @since 1.0.0
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Decoder {
    Class<? extends TypeDecoder> value();
}
