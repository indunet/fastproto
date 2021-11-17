/*
 * Copyright 2019-2021 indunet.org
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

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.EnumDecoder;
import org.indunet.fastproto.encoder.EnumEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Float type, corresponding to Java Float/float.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 2.1.0
 */
@TypeFlag
@Decoder(EnumDecoder.class)
@Encoder(EnumEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumType {
    Type[] JAVA_TYPES = {Enum.class};

    int value();

    String fieldName() default "";

    ProtocolType protocolType() default ProtocolType.UINTEGER8;

    EndianPolicy[] endianPolicy() default {};

    Class<? extends Function<? extends Enum, ?>>[] decodingFormula() default {};

    Class<? extends Function<?, ? super Enum>>[] encodingFormula() default {};
}
