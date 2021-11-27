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

package org.indunet.fastproto.pipeline.decode;

import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.FixedLengthException;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.CodecContext;
import org.indunet.fastproto.pipeline.FlowCode;

import java.text.MessageFormat;

/**
 * Verify length flow.
 *
 * @author Deng Ran
 * @since 2.4.0
 */
public class VerifyFixedLengthFlow extends Pipeline<CodecContext> {
    @Override
    public void process(CodecContext context) {
        int fixedLength = context.getReferenceGraph()
                .root()
                .getEnableFixedLength()
                .value();
        int length = context.getDatagram().length;

        if (fixedLength != length) {
            throw new FixedLengthException(MessageFormat.format(
                    CodecError.FIXED_LENGTH_UNMATCH.getMessage(), fixedLength, length));
        }

        this.forward(context);
    }

    @Override
    public long getCode() {
        return FlowCode.VERIFY_FIXED_LENGTH_FLOW_CODE;
    }
}
