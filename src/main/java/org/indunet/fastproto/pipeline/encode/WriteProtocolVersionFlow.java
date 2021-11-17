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

import org.indunet.fastproto.ProtocolVersionAssist;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.CodecContext;
import org.indunet.fastproto.pipeline.FlowCode;

/**
 * Write protocol version flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class WriteProtocolVersionFlow extends AbstractFlow<CodecContext> {
    @Override
    public void process(CodecContext context) {
        ProtocolVersionAssist.encode(context.getDatagram(), context.getReferenceGraph().root());

        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return FlowCode.WRITE_PROTOCOL_VERSION_FLOW_CODE;
    }
}
