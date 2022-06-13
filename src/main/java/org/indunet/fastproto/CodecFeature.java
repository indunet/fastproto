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

package org.indunet.fastproto;

import lombok.val;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.pipeline.FlowCode;

/**
 * Codec Feature.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public final class CodecFeature {
    public static final long DEFAULT = 0;
    public static final long DISABLE_COMPRESS = FlowCode.COMPRESS_FLOW_CODE | FlowCode.UNCOMPRESS_FLOW_CODE;
    public static final long DISABLE_PROTOCOL_VERSION = FlowCode.VERIFY_PROTOCOL_VERSION_FLOW_CODE | FlowCode.WRITE_PROTOCOL_VERSION_FLOW_CODE;
    public static final long DISABLE_CHECKSUM = FlowCode.VERIFY_CHECKSUM_FLOW_CODE | FlowCode.WRITE_CHECKSUM_FLOW_CODE;
    public static final long NON_INFER_LENGTH = FlowCode.INFER_LENGTH_FLOW_CODE;
    public static final long DISABLE_CRYPTO = FlowCode.DECRYPT_FLOW_CODE | FlowCode.ENCRYPT_FLOW_CODE;
    public static final long DISABLE_FIXED_LENGTH = FlowCode.VERIFY_FIXED_LENGTH_FLOW_CODE | FlowCode.FIXED_LENGTH_FLOW_CODE;

    public static long of(Reference reference) {
        long codecFeature = DEFAULT;

        if (reference.getEnableCrypto() == null) {
            codecFeature |= DISABLE_CRYPTO;
        }

        if (reference.getEnableCompress() == null) {
            codecFeature |= DISABLE_COMPRESS;
        }

        if (reference.getEnableChecksum() == null) {
            codecFeature |= DISABLE_CHECKSUM;
        }

        if (reference.getEnableProtocolVersions().isEmpty()) {
            codecFeature |= DISABLE_PROTOCOL_VERSION;
        }

        if (reference.getEnableFixedLength() == null) {
            codecFeature |= DISABLE_FIXED_LENGTH;
        }

        return codecFeature;
    }

    public static long of(long... codecFeatures) {
        long codecFeature = DEFAULT;

        for (val feature: codecFeatures) {
            codecFeature |= feature;
        }

        return codecFeature;
    }
}
