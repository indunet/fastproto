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

package org.indunet.fastproto.annotation;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Long type, corresponding to Java Long/long.
 *
 * @author Deng Ran
 * @see DataType
 * @since 1.2.0
 */
@DataType
@Validator({DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UInt32Type {
    Class DEFAULT_JAVA_TYPE = long[].class;
    int SIZE = Integer.SIZE >> 3;
    long MAX_VALUE = (long) Integer.MAX_VALUE - Integer.MIN_VALUE;
    int MIN_VALUE = 0;

    int offset();

    EndianPolicy[] endian() default {};
}