package org.indunet.fastproto.annotation;

import org.indunet.fastproto.formula.Formula;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecodeFormula {
    Class<? extends Formula> value();
}
