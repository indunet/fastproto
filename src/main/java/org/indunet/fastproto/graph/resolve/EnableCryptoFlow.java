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

package org.indunet.fastproto.graph.resolve;

import lombok.val;
import org.indunet.fastproto.annotation.EnableCrypto;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CryptoException;
import org.indunet.fastproto.graph.Reference;

/**
 * Resolve enable crypto flow.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class EnableCryptoFlow extends ResolvePipeline {
    @Override
    public void process(Reference reference) {
        val protocolClass = reference.getProtocolClass();

        if (protocolClass.isAnnotationPresent(EnableCrypto.class)) {
            val enableCrypto = protocolClass.getAnnotation(EnableCrypto.class);
            byte[] key;

            if (!enableCrypto.key().isEmpty()) {
                key = enableCrypto.key()
                        .getBytes();

                reference.setEnableCrypto(enableCrypto);
            } else if (enableCrypto.keySupplier().length != 0) {
                val keySupplier = enableCrypto.keySupplier()[0];

                try {
                    key = keySupplier.newInstance()
                            .get();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new CryptoException(CodecError.INVALID_CRYPTO_KEY_SUPPLIER, e);
                }
            } else {
                throw new CryptoException(CodecError.NO_CRYPTO_KEY);
            }
        }

        this.forward(reference);
    }
}
