package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.Type;
import org.indunet.fastproto.annotation.Type.DataType;
import org.indunet.fastproto.decoder.TimestampDecoder;
import org.indunet.fastproto.encoder.TimestampEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Timestamp type, corresponding to Java java.sql.Timestamp.
 *
 * @author Deng Ran
 * @see Type
 * @since 1.1.0
 */
@Type
@Decoder(TimestampDecoder.class)
@Encoder(TimestampEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimestampType {
    int value();

    DataType dataType() default DataType.LONG_TYPE;

    TimeUnit unit() default TimeUnit.MILLISECONDS;

    Class<? extends Function<Timestamp, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Timestamp>>[] beforeEncode() default {};
}
