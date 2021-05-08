package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.TypeDecoder;
import org.indunet.fastproto.annotation.TypeEncoder;
import org.indunet.fastproto.decoder.BooleanDecoder;
import org.indunet.fastproto.encoder.BooleanEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author Deng Ran
 * @version 1.0
 */
@DataType
@TypeDecoder(BooleanDecoder.class)
@TypeEncoder(BooleanEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BooleanType {
    int MAX_BIT_OFFSET = 7;
    int MIN_BIT_OFFSET = 0;

    int byteOffset();

    int bitOffset();
}
