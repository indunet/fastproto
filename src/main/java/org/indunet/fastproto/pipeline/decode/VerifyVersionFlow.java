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

package org.indunet.fastproto.pipeline.decode;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.pipeline.CodecContext;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.ProtocolVersionException;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.FlowCode;

/**
 * Verify protocol version flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class VerifyVersionFlow extends Pipeline<CodecContext> {
    @Override
    public void process(@NonNull CodecContext context) {
        val reference = context.getGraph().root();
        val versions = reference.getEnableProtocolVersions();
        val bytes = context.getDatagram();
        
        val flag = versions.stream()
                .anyMatch(v -> v.version() != CodecUtils.uint8Type(bytes, v.offset()));
        
        if (flag) {
            throw new ProtocolVersionException(CodecError.PROTOCOL_VERSION_NOT_MATCH);
        } else {
            this.forward(context);
        }
    }

    @Override
    public long getCode() {
        return FlowCode.VERIFY_PROTOCOL_VERSION_FLOW_CODE;
    }
}
