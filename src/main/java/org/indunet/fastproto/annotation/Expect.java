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

import org.indunet.fastproto.ByteOrder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Expect annotation declares a constant assertion at a fixed offset in the packet.
 * On decode, the value at offset must match the configured constant; otherwise DecodingException is thrown.
 * On encode, the configured constant is written to the buffer at the given offset.
 *
 * Either 'bytes' or ('value' + 'size') must be provided.
 *
 * @author Deng Ran
 * @since 3.12.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Expects.class)
public @interface Expect {
    /**
     * Byte offset for assertion/read/write.
     */
    int offset();

    /**
     * Raw bytes to assert/write. If non-empty, it takes precedence over value/size.
     */
    byte[] bytes() default {};

    /**
     * Unsigned integer value to assert/write when bytes[] is empty.
     */
    long value() default Long.MIN_VALUE;

    /**
     * Size in bytes of integer value. Valid values: 1, 2, 4, 8.
     */
    int size() default 1;

    /**
     * Byte order for multi-byte integer assertion/write.
     */
    ByteOrder byteOrder() default ByteOrder.LITTLE;
}


