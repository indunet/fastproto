/*
 * Copyright 2019-2022 indunet.org
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

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for a Double array type. Each element in the array occupies 8 bytes.
 * This annotation can be used to mark fields of type Double[], double[], List<Double>, or Set<Double>.
 *
 * @author Deng Ran
 * @since 3.6.0
 */
@DataType
@Validator({DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleArrayType {
    /**
     * Specifies the offset where the Double array starts in the byte array.
     * The offset is measured in bytes.
     *
     * @return the offset in bytes.
     */
    int offset();

    /**
     * Specifies the length of the Double array.
     * The length is measured in number of elements, not bytes.
     *
     * @return the length of the Double array.
     */
    int length();

    /**
     * Specifies the byte order used when decoding the Double array.
     * If not specified, the default byte order is used.
     *
     * @return the byte order.
     */
    ByteOrder[] byteOrder() default {};
}
