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

import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.ArrayDecoder;
import org.indunet.fastproto.encoder.ArrayEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Array type.
 *
 * @author Deng Ran
 * @since 2.2.0
 */
@TypeFlag
@Decoder(ArrayDecoder.class)
@Encoder(ArrayEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayType {
    Type[] JAVA_TYPES = {
            char[].class, Character[].class,
            byte[].class, Byte[].class,
            short[].class, Short[].class,
            int[].class, Integer[].class,
            long[].class, Long[].class,
            float[].class, Float[].class,
            double[].class, Double[].class
    };
    ProtocolType[] PROTOCOL_TYPES = {
      ProtocolType.CHARACTER,
      ProtocolType.BOOLEAN,
      ProtocolType.BYTE,
      ProtocolType.SHORT,
      ProtocolType.INTEGER,
      ProtocolType.LONG,
      ProtocolType.FLOAT,
      ProtocolType.DOUBLE,
      ProtocolType.INTEGER8,
      ProtocolType.INTEGER16,
      ProtocolType.UINTEGER8,
      ProtocolType.UINTEGER16,
      ProtocolType.UINTEGER32,
    };
    Boolean AUTO_TYPE = true;

    int value();

    int bitOffset() default 0;

    ProtocolType protocolType();

    int length();

    Class<? extends Function<?, ?>>[] afterDecode() default {};

    Class<? extends Function<?, ?>>[] beforeEncode() default {};
}
