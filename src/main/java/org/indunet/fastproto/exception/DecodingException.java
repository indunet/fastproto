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

/**
 * Decode exception.
 *
 * @author Deng Ran
 * @see CodecException
 * @since 1.0.0
 */
public class DecodingException extends CodecException {
    public DecodingException() {

    }

    public DecodingException(CodecError error) {
        this(error.getMessage());
    }

    public DecodingException(String message) {
        super(message);
    }

    public DecodingException(CodecError error, Throwable cause) {
        this(error.getMessage(), cause);
    }

    public DecodingException(String message, Throwable cause) {
        super(message, cause);
    }
}