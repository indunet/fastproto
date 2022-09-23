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
import org.indunet.fastproto.decoder.IntegerDecoder;
import org.indunet.fastproto.encoder.IntegerEncoder;
import org.indunet.fastproto.reference.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.reference.resolve.validate.EncodingFormulaValidator;
import org.indunet.fastproto.reference.resolve.validate.FieldValidator;

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
 * @since 1.0.0
 */
@TypeFlag
@Decoder(IntegerDecoder.class)
@Encoder(IntegerEncoder.class)
@Validator({FieldValidator.class, DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Int32Type {
    Type[] ALLOWED_JAVA_TYPES = {
            int.class,
            Integer.class
    };
    int SIZE = Integer.SIZE >> 3;
    int MAX_VALUE = Integer.MAX_VALUE;
    int MIN_VALUE = Integer.MIN_VALUE;

    int offset();

    Class<? extends Function<Integer, ?>>[] decodingFormula() default {};

    Class<? extends Function<?, Integer>>[] encodingFormula() default {};

    String description() default "";
}
