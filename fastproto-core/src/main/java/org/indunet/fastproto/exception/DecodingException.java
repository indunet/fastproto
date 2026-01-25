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
 * Exception class for Decoding operations.
 * This class extends CodecException and is used to handle exceptions that occur during the decoding process in the fastproto library.
 * It provides constructors to create an exception with a message, a cause, or both.
 * This exception is thrown when there are issues during the decoding process.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
public class DecodingException extends CodecException {
    public DecodingException() {
    }

    public DecodingException(String message) {
        super(message);
    }

    public DecodingException(String message, Throwable cause) {
        super(message, cause);
    }
}
