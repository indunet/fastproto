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

package org.indunet.fastproto;

import org.indunet.fastproto.flow.decode.DecompressFlow;
import org.indunet.fastproto.flow.decode.VerifyChecksumFlow;
import org.indunet.fastproto.flow.decode.VerifyProtocolVersionFlow;
import org.indunet.fastproto.flow.encode.CompressFlow;
import org.indunet.fastproto.flow.encode.InferLengthFlow;
import org.indunet.fastproto.flow.encode.WriteChecksumFlow;
import org.indunet.fastproto.flow.encode.WriteProtocolVersionFlow;

/**
 * Codec Feature.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public final class CodecFeature {
    public static final int DEFAULT = 0;
    public static final int IGNORE_ENABLE_COMPRESS = CompressFlow.FLOW_CODE | DecompressFlow.FLOW_CODE;
    public static final int IGNORE_PROTOCOL_VERSION = VerifyProtocolVersionFlow.FLOW_CODE | WriteProtocolVersionFlow.FLOW_CODE;
    public static final int IGNORE_CHECKSUM = VerifyChecksumFlow.FLOW_CODE | WriteChecksumFlow.FLOW_CODE;
    public static final int NON_INFER_LENGTH = InferLengthFlow.FLOW_CODE;

    public static int valueOf(TypeAssist assist) {
        int codecFeature = 0;

        if (!assist.getOpEnableCompress().isPresent()) {
            codecFeature |= IGNORE_ENABLE_COMPRESS;
        }

        if (!assist.getOpChecksum().isPresent()) {
            codecFeature |= IGNORE_CHECKSUM;
        }

        if (!assist.getOpProtocolVersion().isPresent()) {
            codecFeature |= IGNORE_PROTOCOL_VERSION;
        }

        return codecFeature;
    }
}
