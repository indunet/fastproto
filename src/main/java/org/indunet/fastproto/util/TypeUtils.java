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

package org.indunet.fastproto.util;

import lombok.NonNull;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CodecException;

import java.lang.reflect.Type;
import java.text.MessageFormat;

/**
 * Type utils.
 *
 * @author Deng Ran
 * @since 2.0.0
 */
public class TypeUtils {
    public static Type getWrapperClass(@NonNull String name) {
        switch (name) {
            case "boolean":
                return Boolean.class;
            case "byte":
                return Byte.class;
            case "char":
                return Character.class;
            case "short":
                return Short.class;
            case "int":
                return Integer.class;
            case "long":
                return Long.class;
            case "float":
                return Float.class;
            case "double":
                return Double.class;
            default:
                throw new CodecException(
                        MessageFormat.format(CodecError.UNSUPPORTED_TYPE.getMessage(), name));
        }
    }
}