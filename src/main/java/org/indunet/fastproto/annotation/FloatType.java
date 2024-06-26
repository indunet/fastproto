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

import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for Float type. This type occupies 4 bytes and can be used to annotate fields of type Float or float.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
@DataType
@Validator({DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FloatType {
    int SIZE = Float.SIZE >> 3;
    float MAX_VALUE = Float.MAX_VALUE;
    float MIN_VALUE = Float.MIN_VALUE;

    /*
     * The byte offset of the field in the binary data.
     */
    int offset();

    /*
     * The byte order of the field in the binary data, its priority is higher than @DefaultByteOrder.
     */
    ByteOrder[] byteOrder() default {};
}
