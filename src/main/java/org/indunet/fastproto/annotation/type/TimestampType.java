package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.TypeDecoder;
import org.indunet.fastproto.annotation.TypeEncoder;
import org.indunet.fastproto.decoder.TimestampDecoder;
import org.indunet.fastproto.encoder.TimestampEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author Deng Ran
 * @version 1.0
 */
@DataType
@TypeDecoder(TimestampDecoder.class)
@TypeEncoder(TimestampEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimestampType {
    int byteOffset();

    int length() default 8;

    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
