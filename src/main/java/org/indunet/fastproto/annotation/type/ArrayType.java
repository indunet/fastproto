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
import org.indunet.fastproto.annotation.Validator;
import org.indunet.fastproto.decoder.ArrayDecoder;
import org.indunet.fastproto.encoder.ArrayEncoder;
import org.indunet.fastproto.graph.validate.ArrayValidator;
import org.indunet.fastproto.graph.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.validate.EncodingFormulaValidator;
import org.indunet.fastproto.graph.validate.FieldValidator;

import java.lang.annotation.*;
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
@Validator({FieldValidator.class, DecodingFormulaValidator.class, EncodingFormulaValidator.class, ArrayValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayType {
    Type[] ALLOWED_JAVA_TYPES = {
            char[].class,
            Character[].class,
            byte[].class,
            Byte[].class,
            short[].class,
            Short[].class,
            int[].class,
            Integer[].class,
            long[].class,
            Long[].class,
            float[].class,
            Float[].class,
            double[].class,
            Double[].class
    };

    Class<?>[] ALLOWED_GENERIC_TYPES = {
            ProtocolType.CHAR,
            ProtocolType.BYTE,
            ProtocolType.SHORT,
            ProtocolType.INT32,
            ProtocolType.INT64,
            ProtocolType.FLOAT,
            ProtocolType.DOUBLE,
            ProtocolType.INT8,
            ProtocolType.INT16,
            ProtocolType.UINT8,
            ProtocolType.UINT16,
            ProtocolType.UINT32,
    };

    int value();

    Class<? extends Annotation> genericType();

    int length();

    Class<? extends Function<?, ?>>[] decodingFormula() default {};

    Class<? extends Function<?, ?>>[] encodingFormula() default {};

    String description() default "";
}
