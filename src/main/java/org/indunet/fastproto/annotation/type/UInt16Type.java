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
import org.indunet.fastproto.annotation.Validator;
import org.indunet.fastproto.decoder.UInteger16Decoder;
import org.indunet.fastproto.encoder.UInteger16Encoder;
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
 * @see TypeFlag
 * @since 1.2.0
 */
@TypeFlag
@Decoder(UInteger16Decoder.class)
@Encoder(UInteger16Encoder.class)
@Validator({FieldValidator.class, DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UInt16Type {
    Type[] ALLOWED_JAVA_TYPES = {
            int.class,
            Integer.class
    };
    int SIZE = Short.SIZE >> 3;
    int MAX_VALUE = Short.MAX_VALUE - Short.MIN_VALUE;
    int MIN_VALUE = 0;

    int value();

    Class<? extends Function<Integer, ?>>[] decodingFormula() default {};

    Class<? extends Function<?, Integer>>[] encodingFormula() default {};

    String description() default "";
}
