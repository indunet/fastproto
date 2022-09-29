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
import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.Validator;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.FieldValidator;

import java.lang.annotation.*;
import java.util.function.Function;

/**
 * Float type, corresponding to Java Float/float.
 *
 * @author Deng Ran
 * @see DataType
 * @since 2.1.0
 */
@DataType
@Validator({FieldValidator.class, DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumType {
    int SIZE = Byte.SIZE >> 3;
    int MAX_VALUE = Byte.MAX_VALUE - Byte.MIN_VALUE;
    int MIN_VALUE = 0;

    int offset();

    String name() default "";

    EndianPolicy[] endianPolicy() default {};

    Class<? extends Function<? extends Enum, ?>>[] decodingFormula() default {};

    Class<? extends Function<?, ? super Enum>>[] encodingFormula() default {};
}
