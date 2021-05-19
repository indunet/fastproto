package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.decoder.TimestampDecoder;
import org.indunet.fastproto.encoder.TimestampEncoder;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Deng Ran
 * @version 1.0
 */
@DataType
@Decoder(TimestampDecoder.class)
@Encoder(TimestampEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimestampType {
    int byteOffset();

    Class<? extends Annotation> dataType() default LongType.class;

    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
