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
import org.indunet.fastproto.decoder.UInteger64Decoder;
import org.indunet.fastproto.encoder.UInteger64Encoder;
import org.indunet.fastproto.graph.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.validate.EncodingFormulaValidator;
import org.indunet.fastproto.graph.validate.FieldValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

/**
 * uint64 type, corresponding to Java BigInteger.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.5.0
 */
@TypeFlag
@Decoder(UInteger64Decoder.class)
@Encoder(UInteger64Encoder.class)
@Validator({FieldValidator.class, DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UInt64Type {
    Type[] ALLOWED_JAVA_TYPES = {
            BigInteger.class
    };
    int SIZE = Long.SIZE >> 3;
    BigInteger MAX_VALUE = new BigInteger(String.valueOf(Long.MAX_VALUE))
            .subtract(new BigInteger(String.valueOf(Long.MIN_VALUE)));
    BigInteger MIN_VALUE = new BigInteger("0");

    int value();

    Class<? extends Function<BigDecimal, ?>>[] decodingFormula() default {};

    Class<? extends Function<?, BigDecimal>>[] encodingFormula() default {};

    String description() default "";
}
