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

package org.indunet.fastproto.codec;

import lombok.Builder;
import lombok.Data;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Codec context.
 *
 * @author Deng Ran
 * @since 3.2.2
 */
@Data
@Builder
public class CodecContext {
    ByteOrder defaultByteOrder;
    BitOrder defaultBitOrder;
    Class<?> fieldType;
    Field field;
    Annotation dataTypeAnnotation;

    public <T> T getDataTypeAnnotation(Class<T> clazz) {
        return (T) this.dataTypeAnnotation;
    }
}
