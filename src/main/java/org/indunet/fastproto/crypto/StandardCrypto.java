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

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Standard crypto, aes & des
 *
 * @author Deng Ran
 * @since 2.0.0
 */
public class StandardCrypto implements Crypto {
    protected static ConcurrentMap<String, StandardCrypto> ciphers = new ConcurrentHashMap<>();
    protected String transformation;
    protected String algorithm;
    protected int keyLength;

    protected StandardCrypto(String transformation, int keyLength) {
        this.transformation = transformation;
        this.keyLength = keyLength;
        this.algorithm = transformation.split("/")[0];
    }

    public static StandardCrypto getInstance(@NonNull CryptoPolicy policy) {
        return ciphers.computeIfAbsent(policy.getTransformation(), __ ->
                new StandardCrypto(policy.getTransformation(), policy.getKeyLength()));
    }

    @Override
    public byte[] encrypt(byte[] key, byte[] datagram) {
        try {
            Cipher cipher = Cipher.getInstance(this.transformation);
            SecretKey keySpec = new SecretKeySpec(generateKey(key, keyLength), this.algorithm);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(datagram);
        } catch (NoSuchPaddingException | IllegalBlockSizeException
                    | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new CryptoException(CodecError.FAIL_ENCRYPTING, e);
        }
    }

    @Override
    public byte[] decrypt(byte[] key, byte[] datagram) {
        try {
            val cipher = Cipher.getInstance(this.transformation);
            SecretKey keySpec = new SecretKeySpec(generateKey(key, keyLength), this.algorithm);

            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(datagram);
        } catch (NoSuchPaddingException | IllegalBlockSizeException
                | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new CryptoException(CodecError.FAIL_DECRYPTING, e);
        }
    }

    public byte[] generateKey(@NonNull byte[] userKey, int length) {
        if (userKey.length == 0) {
            throw new CryptoException(CodecError.INVALID_CRYPTO_KEY);
        }

        val key = new byte[length];

        System.arraycopy(userKey, 0, key, 0, Math.min(userKey.length, length));

        return key;
    }
}
