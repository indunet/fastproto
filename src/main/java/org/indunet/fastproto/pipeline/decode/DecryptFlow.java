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
import org.indunet.fastproto.annotation.EnableCrypto;
import org.indunet.fastproto.crypto.Crypto;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CryptoException;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.PipelineContext;
import org.indunet.fastproto.pipeline.FlowCode;

import java.util.Optional;

/**
 * Decrypt flow.
 *
 * @author Deng Ran
 * @since 2.0.0
 */
public class DecryptFlow extends Pipeline<PipelineContext> {
    @Override
    public void process(PipelineContext context) {
        val graph = context.getGraph();
        val ref = graph.root();

        if (ref.getEnableCrypto() == null) {
            return;
        }

        val datagram = context.getDatagram();
        val crypto = Crypto.getInstance(ref.getEnableCrypto());
        val enableCrypto = ref.getEnableCrypto();
        byte[] key;

        if (!enableCrypto.key().isEmpty()) {
            key = enableCrypto.key().getBytes();
        } else if (enableCrypto.keySupplier().length != 0) {
            key = Optional.of(enableCrypto)
                    .map(EnableCrypto::keySupplier)
                    .map(a -> {
                        try {
                            val c = a[0];

                            return c.newInstance()
                                    .get();
                        } catch (InstantiationException | IllegalAccessException  e) {
                            throw new CryptoException(CodecError.INVALID_CRYPTO_KEY_SUPPLIER, e);
                        }
                    }).get();
        } else {
            throw new CryptoException(CodecError.NO_CRYPTO_KEY);
        }

        context.setDatagram(crypto.decrypt(key, datagram));
        this.forward(context);
    }

    @Override
    public long getCode() {
        return FlowCode.DECRYPT_FLOW_CODE;
    }
}
