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
import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.Validator;
import org.indunet.fastproto.decoder.UInteger8Decoder;
import org.indunet.fastproto.encoder.UInteger8Encoder;
import org.indunet.fastproto.graph.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.validate.EncodingFormulaValidator;
import org.indunet.fastproto.graph.validate.FieldValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * Integer type, corresponding to Java Integer/int.
 *
 * @author Deng Ran
 * @see DataType
 * @since 1.2.0
 */
@Deprecated
@DataType
@Decoder(UInteger8Decoder.class)
@Encoder(UInteger8Encoder.class)
@Validator({FieldValidator.class, DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UInteger8Type {
    Type[] ALLOWED_JAVA_TYPES = {
            Integer.class,
            Integer.TYPE
    };
    int SIZE = Byte.SIZE >> 3;
    int MAX_VALUE = Byte.MAX_VALUE - Byte.MIN_VALUE;
    int MIN_VALUE = 0;

    int value();

    Class<? extends Function<Integer, ?>>[] decodingFormula() default {};

    Class<? extends Function<?, Integer>>[] encodingFormula() default {};

    String description() default "";
}
