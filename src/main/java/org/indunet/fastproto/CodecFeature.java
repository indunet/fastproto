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
import org.indunet.fastproto.pipeline.decode.DecompressFlow;
import org.indunet.fastproto.pipeline.decode.DecryptFlow;
import org.indunet.fastproto.pipeline.decode.VerifyChecksumFlow;
import org.indunet.fastproto.pipeline.decode.VerifyProtocolVersionFlow;
import org.indunet.fastproto.pipeline.encode.*;

/**
 * Codec Feature.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public final class CodecFeature {
    public static final long DEFAULT = 0;
    public static final long DISABLE_COMPRESS = CompressFlow.FLOW_CODE | DecompressFlow.FLOW_CODE;
    public static final long DISABLE_PROTOCOL_VERSION = VerifyProtocolVersionFlow.FLOW_CODE | WriteProtocolVersionFlow.FLOW_CODE;
    public static final long DISABLE_CHECKSUM = VerifyChecksumFlow.FLOW_CODE | WriteChecksumFlow.FLOW_CODE;
    public static final long NON_INFER_LENGTH = InferLengthFlow.FLOW_CODE;
    public static final long DISABLE_CRYPTO = EncryptFlow.FLOW_CODE | DecryptFlow.FLOW_CODE;

    public static long of(TypeAssist assist) {
        long codecFeature = DEFAULT;

        if (assist.getEnableCrypto() == null) {
            codecFeature |= DISABLE_CRYPTO;
        }

        if (assist.getEnableCompress() == null) {
            codecFeature |= DISABLE_COMPRESS;
        }

        if (assist.getEnableChecksum() == null) {
            codecFeature |= DISABLE_CHECKSUM;
        }

        if (assist.getEnableProtocolVersion() == null) {
            codecFeature |= DISABLE_PROTOCOL_VERSION;
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
