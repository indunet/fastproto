package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.TimestampDecoder;
import org.indunet.fastproto.encoder.TimestampEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Timestamp type, corresponding to Java java.sql.Timestamp.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.1.0
 */
@TypeFlag
@Decoder(TimestampDecoder.class)
@Encoder(TimestampEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimestampType {
    Type[] JAVA_TYPES = {Timestamp.class};

    int value();

    ProtocolType protocolType() default ProtocolType.LONG;

    TimeUnit unit() default TimeUnit.MILLISECONDS;

    Class<? extends Function<Timestamp, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Timestamp>>[] beforeEncode() default {};
}
