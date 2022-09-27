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

import lombok.val;
import org.indunet.fastproto.annotation.EnableChecksum;
import org.indunet.fastproto.checksum.Checker;
import org.indunet.fastproto.exception.CheckSumException;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.PipelineContext;
import org.indunet.fastproto.pipeline.FlowCode;

/**
 * verify checksum flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class VerifyChecksumFlow extends Pipeline<PipelineContext> {
    @Override
    public void process(PipelineContext context) {
        val protocolClass = context.getProtocolClass();
        val datagram = context.getDatagram();

        if (protocolClass.isAnnotationPresent(EnableChecksum.class)) {
            Checker checker = Checker
                    .getInstance(protocolClass.getAnnotation(EnableChecksum.class));

            if (!checker.validate(datagram, protocolClass)) {
                throw new CheckSumException(CodecError.ILLEGAL_CHECK_SUM);
            }
        }

        this.forward(context);
    }

    @Override
    public long getCode() {
        return FlowCode.VERIFY_CHECKSUM_FLOW_CODE;
    }
}
