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
 * @author Deng Ran
 * @since 1.7.0
 */
public class EncodeFormulaException extends EncodingException {
    public EncodeFormulaException() {

    }

    public EncodeFormulaException(CodecError error) {
        this(error.getMessage());
    }

    public EncodeFormulaException(String message) {
        super(message);
    }

    public EncodeFormulaException(CodecError error, Throwable cause) {
        this(error.getMessage(), cause);
    }

    public EncodeFormulaException(String message, Throwable cause) {
        super(message, cause);
    }
}
