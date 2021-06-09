package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.TypeFlag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Auto type, infer by the field type, support all Java primitive type and their wrapper classes.
 *
 * @author Deng Ran
 * @since 1.4.0
 */
@TypeFlag
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoType {
    int value();

    int bitOffset() default 0;

    int length() default -1;

    ProtocolType dataType() default ProtocolType.LONG;

    TimeUnit unit() default TimeUnit.MILLISECONDS;

    Class<? extends Function>[] afterDecode() default {};

    Class<? extends Function>[] beforeEncode() default {};
}
