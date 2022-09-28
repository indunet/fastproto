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

import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.Validator;
import org.indunet.fastproto.reference.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.reference.resolve.validate.EncodingFormulaValidator;
import org.indunet.fastproto.reference.resolve.validate.FieldValidator;

import java.lang.annotation.*;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Timestamp type, corresponding to Java java.sql.Timestamp.
 *
 * @author Deng Ran
 * @see DataType
 * @since 1.1.0
 */
@DataType
@Validator({FieldValidator.class, DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeType {
    int SIZE = Long.SIZE >> 3;

    int offset();

    Class<? extends Annotation> genericType() default Int64Type.class;

    TimeUnit unit() default TimeUnit.MILLISECONDS;

    Class<? extends Function<Timestamp, ?>>[] decodingFormula() default {};

    Class<? extends Function<?, Timestamp>>[] encodingFormula() default {};
}
