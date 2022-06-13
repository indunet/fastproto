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

package org.indunet.fastproto.pipeline.encode;

import lombok.val;
import org.indunet.fastproto.pipeline.CodecContext;
import org.indunet.fastproto.util.CodecUtils;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.FlowCode;

/**
 * Write protocol version flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class WriteVersionFlow extends Pipeline<CodecContext> {
    @Override
    public void process(CodecContext context) {
        val reference = context.getReferenceGraph().root();
        val versions = reference.getEnableProtocolVersions();
        val bytes = context.getDatagram();
        
        versions.stream()
            .forEach(v -> CodecUtils.uint8Type(bytes, v.offset(), v.version()));
        
        this.forward(context);
    }

    @Override
    public long getCode() {
        return FlowCode.WRITE_PROTOCOL_VERSION_FLOW_CODE;
    }
}
