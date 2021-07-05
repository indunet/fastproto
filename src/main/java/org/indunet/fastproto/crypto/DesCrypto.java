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

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * DES crypto.
 *
 * @author Deng Ran
 * @since 2.0.0
 */
public class DesCrypto implements Crypto {
    protected static final String NAME = "DES";
    protected static final int KEY_LENGTH = 8;
    protected static DesCrypto crypto = new DesCrypto();

    public static DesCrypto getInstance() {
        return crypto;
    }

    @Override
    public byte[] encrypt(byte[] key, byte[] datagram) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        SecretKey keySpec = new SecretKeySpec(generateKey(key, KEY_LENGTH), NAME);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            return cipher.doFinal(datagram);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public byte[] decrypt(byte[] key, byte[] datagram) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        SecretKey keySpec = new SecretKeySpec(generateKey(key, KEY_LENGTH), NAME);
        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            return cipher.doFinal(datagram);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
