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

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.annotation.Validator;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.FieldValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

/**
 * Float type, corresponding to Java Float/float.
 *
 * @author Deng Ran
 * @see DataType
 * @since 1.0.0
 */
@DataType
@Validator({FieldValidator.class, DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FloatType {
    int SIZE = Float.SIZE >> 3;
    float MAX_VALUE = Float.MAX_VALUE;
    float MIN_VALUE = Float.MIN_VALUE;

    int offset();

    EndianPolicy endian() default EndianPolicy.LITTLE;
}
