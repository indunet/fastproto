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

package org.indunet.fastproto.crypto;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CryptoException;

/**
 * Crypto.
 *
 * @author Deng Ran
 * @since 2.0.0
 */
public interface Crypto {
    byte[] encrypt(byte[] key, byte[] datagram);
    byte[] decrypt(byte[] key, byte[] datagram);

    default byte[] generateKey(@NonNull byte[] userKey, int length) {
        if (userKey.length == 0) {
            throw new CryptoException(CodecError.INVALID_CRYPTO_KEY);
        }

        val key = new byte[length];

        System.arraycopy(userKey, 0, key, 0, Math.min(userKey.length, length));

        return key;
    }
}
