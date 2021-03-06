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
 * @author Deng Ran
 * @since 1.7.0
 */
@AllArgsConstructor
@Getter
public enum CodecError {
    INVALID_ENDIAN_POLICY("Invalid endian policy."),
    INVALID_COMPRESS_POLICY("Invalid compress policy."),
    ANNOTATION_FIELD_NOT_MATCH("Annotation {0} and field {1} doesn't match."),
    UNSUPPORTED_TYPE("Unsupported type {0}"),
    UNABLE_RESOLVE_PROTOCOL_CLASS("Unable to resolve the protocol class"),
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
    ILLEGAL_PROTOCOL_VERSION_TYPE("Illegal protocol version type"),
    ILLEGAL_CHECK_SUM("Illegal check sum"),
    NO_VALID_ENCODER_FOUND("No valid encoder found for {0}"),
    EXCEEDED_TYPE_SIZE_LIMIT("Exceeded limitation of {0} size."),
    FAIL_INITIALIZING_ENCODER("Fail initializing encoder of {0}."),
    FAIL_INITIALIZING_ENCODE_FORMULA("Fail initilizing encode formula of {0}."),
    FAIL_GETTING_FIELD_VALUE("Unable to get the value of filed {0}."),
    FAIL_COMPRESS_DATAGRAM("Fail compressing datagram with {0}"),
    UNABLE_INFER_LENGTH("Unable to infer the datagram length in reverse addressing mode."),
    FAIL_DECODING_FIELD("Fail decoding field of {0}"),
    FAIL_ENCODING_FIELD("Fail encoding file of {0}"),
    FAIL_CREATING_DECODE_FLOW("Fail creating decode flow."),
    FAIL_CREATING_ENCODE_FLOW("Fail creating encode flow."),
    INVALID_CRYPTO_KEY("Invalid crypto key."),
    INVALID_CRYPTO_KEY_SUPPLIER("Invalid crypto key supplier."),
    NO_CRYPTO_KEY("No crypto key specified."),
    FAIL_ENCRYPTING("Fail encrypting."),
    FAIL_DECRYPTING("Fail decrypting."),
    NO_KEY_SUPPLIED("No key supplied."),
    INVALID_ENUM_PROTOCOL_TYPE("Invalid protocol type for enum, just uint8, uint16 and int can be applied."),
    ENUM_NOT_FOUND("No enum of code {0} can be found."),
    ILLEGAL_ENUM_CODE_FIELD("Illegal filed name of enum code {0}"),
    NOT_SUPPORT_ARRAY_TYPE("Don't support array type of {0}");

    String message;
}
