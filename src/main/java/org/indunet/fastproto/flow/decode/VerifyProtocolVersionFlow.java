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

package org.indunet.fastproto.flow.decode;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.VersionAssist;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.ProtocolVersionException;
import org.indunet.fastproto.flow.AbstractFlow;
import org.indunet.fastproto.flow.CodecContext;

/**
 * Verify protocol version flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class VerifyProtocolVersionFlow extends AbstractFlow<CodecContext> {
    public static final int FLOW_CODE = 0x0004;

    @Override
    public void process(@NonNull CodecContext context) {
        val datagram = context.getDatagram();
        val protocolClass = context.getProtocolClass();

        if (!VersionAssist.validate(datagram, protocolClass)) {
            throw new ProtocolVersionException(CodecError.PROTOCOL_VERSION_NOT_MATCH);
        } else {
            this.nextFlow(context);
        }
    }

    @Override
    public int getFlowCode() {
        return FLOW_CODE;
    }
}
