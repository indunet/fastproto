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

    public DecodeException(DecodeError error) {
        this(error.getMessage());
    }

    public DecodeException(DecodeError error, Throwable cause) {
        this(error.getMessage(), cause);
    }

    public DecodeException(String message) {
        super(message);
    }

    public DecodeException(String message, Throwable cause) {
        super(message);
    }

    @AllArgsConstructor
    @Getter
    public enum DecodeError {
        NO_VALID_DECODER_FOUND("No valid decoder found for {0}."),
        EXCEEDED_DATAGRAM_SIZE("Exceeded datagram size."),
        ILLEGAL_BIT_OFFSET("Illegal bit offset, must be in [0, 7]."),
        ILLEGAL_BYTE_OFFSET("Illegal byte offset, must be larger than or equal to 0."),
        ILLEGAL_PARAMETER("Illegal parameter."),
        FAIL_INITIALIZING_DECODER("Fail initializing decoder {0}."),
        FAIL_GETTING_DECODE_FORMULA("Fail getting decode formula of annotation {0} on field {1}"),
        FAIL_INITIALIZING_DECODE_FORMULA("Fail initializing decode formula {0}."),
        FAIL_INITIALIZING_DECODE_OBJECT("Fail initializing decode object {0}"),
        NOT_FOUND_DECODER("Decoder for data type {0} cannot be found."),
        ILLEGAL_TIMESTAMP_PARAMETERS("Illgeal timestamp parameters."),
        FAIL_ASSIGN_VALUE("Fail assigning value for field {0}"),
        FAIL_DECOMPRESS_DATAGRAM("Fail decompressing datagram with {0}"),
        PROTOCOL_VERSION_NOT_MATCH("Protocol version and datagram version doesn't match."),
        ILLEGAL_PROTOCOL_VERSION_TYPE("Illegal protocol version type");

        String message;
    }
}
