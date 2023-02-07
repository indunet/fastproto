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

package org.indunet.fastproto.annotation;

import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Boolean type, each unit takes 1 bit, can be used to annotate field type of Boolean/boolean.
 *
 * @author Deng Ran
 * @see DataType
 * @since 1.0.0
 */
@DataType
@Validator({DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BoolType {
    int SIZE = 1;

    int BIT_0 = 0;
    int BIT_1 = 1;
    int BIT_2 = 2;
    int BIT_3 = 3;
    int BIT_4 = 4;
    int BIT_5 = 5;
    int BIT_6 = 6;
    int BIT_7 = 7;

    /*
     * The byte offset of the field in the binary data.
     */
    int byteOffset();

    /*
     * The bit offset of the field in the binary data.
     */
    int bitOffset();

    /*
     * The bit order of the field in the binary data, its priority is higher than @DefaultBitOrder.
     */
    BitOrder[] bitOrder() default {};
}
