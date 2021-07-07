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
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.CodecContext;

/**
 * verify checksum flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class VerifyChecksumFlow extends AbstractFlow<CodecContext> {
    public static final long FLOW_CODE = 0x0002;

    @Override
    public void process(CodecContext context) {
        val protocolClass = context.getProtocolClass();
        val datagram = context.getDatagram();

        if (protocolClass.isAnnotationPresent(EnableChecksum.class)) {
            Checker checker = Checker
                    .getInstance(protocolClass.getAnnotation(EnableChecksum.class));

            if (!checker.validate(datagram, protocolClass)) {
                throw new CheckSumException(CodecError.ILLEGAL_CHECK_SUM);
            }
        }

        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return FLOW_CODE;
    }
}
