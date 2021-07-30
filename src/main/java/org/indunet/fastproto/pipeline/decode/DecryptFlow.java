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


import lombok.val;
import org.indunet.fastproto.crypto.Crypto;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.CodecContext;

/**
 * Decrypt flow.
 *
 * @author Deng Ran
 * @since 2.0.0
 */
public class DecryptFlow extends AbstractFlow<CodecContext> {
    public static final long FLOW_CODE = 0x0010;

    @Override
    public void process(CodecContext context) {
        val assist = context.getTypeAssist();

        if (assist.getEnableCrypto() == null) {
            return;
        }

        val datagram = context.getDatagram();
        val crypto = Crypto.getInstance(assist.getEnableCrypto());
        val key = assist.getKey();

        context.setDatagram(crypto.decrypt(key, datagram));
        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return FLOW_CODE;
    }
}
