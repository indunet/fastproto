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
 * Encode exception.
 *
 * @author Deng Ran
 * @see CodecException
 * @since 1.0.0
 */
public class EncodeException extends CodecException {
    public EncodeException(EncodeError error) {
        this(error.getMessage());
    }

    public EncodeException(EncodeError error, Throwable cause) {
        this(error.getMessage(), cause);
    }

    public EncodeException(String message) {
        super(message);
    }

    public EncodeException(String message, Throwable cause) {
        super(message, cause);
    }

    @AllArgsConstructor
    @Getter
    public enum EncodeError {
        NO_VALID_ENCODER_FOUND("No valid encoder found for {0}"),
        EXCEEDED_DATAGRAM_SIZE("Exceeded datagram size."),
        EXCEEDED_TYPE_SIZE_LIMIT("Exceeded limitation of {0} size."),
        ILLEGAL_BIT_OFFSET("Illegal bit offset, must be in [0, 7]."),
        ILLEGAL_BYTE_OFFSET("Illegal byte offset, must be larger than or equal to 0."),
        ILLEGAL_PARAMETER("Illegal parameter."),
        FAIL_INITIALIZING_ENCODER("Fail initializing encoder of {0}."),
        FAIL_INITIALIZING_ENCODE_FORMULA("Fail initilizing encode formula of {0}."),
        FAIL_GETTING_FIELD_VALUE("Unable to get the value of filed {0}."),
        ILLEGAL_TIMESTAMP_PARAMETERS("Illgeal timestamp parameters."),
        FAIL_COMPRESS_DATAGRAM("Fail compressing datagram with {0}"),
        ILLEGAL_PROTOCOL_VERSION_TYPE("Illegal protocol version type"),
        UNABLE_INFER_LENGTH("Unable to infer the datagram length in reverse addressing mode.");

        String message;
    }
}
