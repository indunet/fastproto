package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.decoder.StringDecoder;
import org.indunet.fastproto.encoder.StringEncoder;

import java.lang.annotation.*;

/**
 * @author Deng Ran
 * @version 1.0
 */
@DataType
@Decoder(StringDecoder.class)
@Encoder(StringEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StringType {
    int byteOffset();

    int length();

    Class<? extends Annotation> dataType() default BinaryType.class;

    String charsetName() default "utf-8";
}
