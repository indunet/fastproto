package org.indunet.fastproto.annotation;

import org.indunet.fastproto.crypto.CryptoPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableCrypto {
    String key() default "";

    String method() default "";

    Class<? extends Supplier<byte[]>>[] keySupplier() default {};

    CryptoPolicy cryptoPolicy() default CryptoPolicy.AES;

    String[] parameters() default {};
}
