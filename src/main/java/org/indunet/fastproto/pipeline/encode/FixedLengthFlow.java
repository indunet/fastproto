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

package org.indunet.fastproto.pipeline.encode;

import lombok.val;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.CodecContext;
import org.indunet.fastproto.pipeline.FlowCode;

/**
 * Codec Context.
 *
 * @author Deng Ran
 * @since 2.4.0
 */
public class FixedLengthFlow extends AbstractFlow<CodecContext> {
    @Override
    public void process(CodecContext context) {
        val assist = context.getTypeAssist();

        context.setDatagram(new byte[assist.getFixedLength()]);
        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return FlowCode.FIXED_LENGTH_FLOW_CODE;
    }
}