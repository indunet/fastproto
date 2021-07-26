/*
 * Copyright 2019-2021 indunet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.UInteger32Decoder;
import org.indunet.fastproto.encoder.UInteger32Encoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Long type, corresponding to Java Long/long.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.2.0
 */
@TypeFlag
@Decoder(UInteger32Decoder.class)
@Encoder(UInteger32Encoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UInteger32Type {
    Type[] JAVA_TYPES = {Long.class, Long.TYPE};
    int SIZE = Integer.SIZE >> 3;
    boolean AUTO_TYPE = false;
    long MAX_VALUE = (long) Integer.MAX_VALUE - Integer.MIN_VALUE;
    int MIN_VALUE = 0;

    int value();

    Class<? extends Function<Long, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Long>>[] beforeEncode() default {};
}
