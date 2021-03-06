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
 * Decode exception.
 *
 * @author Deng Ran
 * @see CodecException
 * @since 1.0.0
 */
public class DecodeException extends CodecException {
    public DecodeException() {

    }

    public DecodeException(CodecError error) {
        this(error.getMessage());
    }

    public DecodeException(String message) {
        super(message);
    }

    public DecodeException(CodecError error, Throwable cause) {
        this(error.getMessage(), cause);
    }

    public DecodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
