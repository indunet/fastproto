/*
 * Copyright 2019-2021 indunet.org
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

package org.indunet.fastproto.pipeline;

/**
 * Codec Context.
 *
 * @author Deng Ran
 * @since 2.4.0
 */
public final class FlowCode {
    public static final long DECODE_FLOW_CODE = 0x0001;
    public static final long DECRYPT_FLOW_CODE = 0x0002;
    public static final long UNCOMPRESS_FLOW_CODE = 0x0004;
    public static final long VERIFY_CHECKSUM_FLOW_CODE = 0x0008;
    public static final long VERIFY_PROTOCOL_VERSION_FLOW_CODE = 0x0010;
    public static final long VERIFY_FIXED_LENGTH_FLOW_CODE = 0x0020;

    public static final long COMPRESS_FLOW_CODE = 0x0100;
    public static final long ENCODE_FLOW_CODE = 0x0200;
    public static final long ENCRYPT_FLOW_CODE = 0x0400;
    public static final long FIXED_LENGTH_FLOW_CODE = 0x0800;
    public static final long INFER_LENGTH_FLOW_CODE = 0x1000;
    public static final long WRITE_CHECKSUM_FLOW_CODE = 0x2000;
    public static final long WRITE_PROTOCOL_VERSION_FLOW_CODE = 0x4000;

    public static final long ARRAY_FLOW_CODE = 0x01;
    public static final long DECODE_FORMULA_FLOW_CODE = 0x02;
    public static final long ENCODE_FORMULA_FLOW_CODE = 0x03;
    public static final long FIELD_FLOW_CODE = 0x04;
    public static final long LIST_FLOW_CODE = 0x05;
    public static final long TIMESTAMP_FLOW_CODE = 0x06;
}
