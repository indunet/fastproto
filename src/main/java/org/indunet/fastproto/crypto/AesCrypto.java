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

import lombok.val;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CryptoException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * AES Crypto which must be 16 bytes key.
 *
 * @author Deng Ran
 * @since 2.0.0
 */
public class AesCrypto implements Crypto {
    protected static final String NAME = "AES";
    protected static final int KEY_LENGTH = 16;
    protected static AesCrypto crypto = new AesCrypto();

    protected AesCrypto() {

    }

    public static AesCrypto getInstance() {
        return crypto;
    }

    public byte[] encrypt(byte[] key, byte[] datagram) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey keySpec = new SecretKeySpec(generateKey(key, KEY_LENGTH), NAME);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(datagram);
        } catch (NoSuchPaddingException | IllegalBlockSizeException
                    | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new CryptoException(CodecError.FAIL_ENCRYPTING, e);
        }
    }

    public byte[] decrypt(byte[] key, byte[] datagram) {
        try {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey keySpec = new SecretKeySpec(generateKey(key, KEY_LENGTH), NAME);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(datagram);
        } catch (NoSuchPaddingException | IllegalBlockSizeException
                | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new CryptoException(CodecError.FAIL_DECRYPTING, e);
        }
    }


}
