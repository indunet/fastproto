/*
 * Copyright 2019-2025 indunet.org
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
 * Bit field type, supports unsigned value extraction from 1..31 bits.
 *
 * @author Deng Ran
 * @since 3.13.0
 */
@DataType
@Validator({DecodingFormulaValidator.class, EncodingFormulaValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BitFieldType {
    /*
     * The byte offset of the field in the binary data.
     */
    int offset() default Integer.MIN_VALUE;

    /*
     * Reference to an offset field name in the same class. Accepts optional leading '$'.
     */
    String offsetRef() default "";

    /*
     * The bit offset (logical index 0..7) within the starting byte.
     */
    int bitOffset();

    /*
     * The bit width of the field (1..31). If 0, a valid lengthRef must be provided.
     */
    int length() default 0;

    /*
     * Reference to a length field name in the same class. Accepts optional leading '$'.
     */
    String lengthRef() default "";

    /*
     * The byte order for multi-byte bit ranges, priority higher than @DefaultByteOrder.
     */
    ByteOrder[] byteOrder() default {};

    /*
     * The bit order within each byte, priority higher than @DefaultBitOrder.
     */
    BitOrder[] bitOrder() default {};
}

