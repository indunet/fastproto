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
import org.indunet.fastproto.decoder.BooleanDecoder;
import org.indunet.fastproto.encoder.BooleanEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.function.Function;


/**
 * Boolean type, corresponding to Java Boolean/boolean.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.0.0
 */
@TypeFlag
@Decoder(BooleanDecoder.class)
@Encoder(BooleanEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BooleanType {
    Type[] JAVA_TYPES = {Boolean.class, Boolean.TYPE};
    int SIZE = 1;
    Boolean AUTO_TYPE = true;
    int MAX_BIT_OFFSET = 7;
    int MIN_BIT_OFFSET = 0;

    int value();

    int bitOffset() default 0;

    Class<? extends Function<Boolean, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Boolean>>[] beforeEncode() default {};
}
