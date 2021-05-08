package org.indunet.fastproto.annotation;

import org.indunet.fastproto.encoder.Encoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Deng Ran
 * @version 1.0
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeEncoder {
    Class<? extends Encoder<?>> value();
}
