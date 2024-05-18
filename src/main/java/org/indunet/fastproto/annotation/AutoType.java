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

import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.graph.resolve.validate.DecodingFormulaValidator;
import org.indunet.fastproto.graph.resolve.validate.EncodingFormulaValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an AutoType annotation. FastProto can infer the annotation type based on the field type when using @AutoType.
 * This annotation is used to automatically determine the type of the field in the binary data.
 *
 * @author Deng Ran
 * @since 3.7.1
 */
@DataType
@Validator({DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoType {
    /*
     * The byte offset of the field in the binary data.
     */
    int[] offset() default {};

    /*
     * The byte offset of the field in the binary data, only valid on boolean type.
     */
    int[] byteOffset() default {};

    /*
     * The bit offset of the field in the binary data, only valid on boolean type.
     */
    int[] bitOffset() default {};

    /*
     * The length of the array or string, only valid on array or string type.
     */
    int[] length() default {};

    /*
     * The byte order of the field in the binary data, its priority is higher than @DefaultByteOrder.
     */
    ByteOrder[] byteOrder() default {};

    /*
     * The bit order of the field in the binary data, its priority is higher than @DefaultBitOrder.
     */
    BitOrder[] bitOrder() default {};

    /*
     * The charset of the field in the binary data, only valid on string type.
     */
    String charset() default "UTF-8";

    /*
     * The field name of enum type, only valid on enum type.
     */
    String name() default "";
}
