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

package org.indunet.fastproto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Codec exception.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
public class CodecException extends RuntimeException {
    public CodecException() {
        super();
    }

    public CodecException(CodecError error) {
        this(error.getMessage());
    }

    public CodecException(String message) {
        super(message);
    }

    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }

    @AllArgsConstructor
    @Getter
    public static enum CodecError {
        INVALID_ENDIAN_POLICY("Invalid endian policy."),
        INVALID_COMPRESS_POLICY("Invalid compress policy."),
        ANNOTATION_FIELD_NOT_MATCH("Annotation {0} and field {1} doesn't match."),
        UNSUPPORTED_TYPE("Unsupported type {0}");

        String message;
    }
}
