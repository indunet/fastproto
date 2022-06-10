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

package org.indunet.fastproto.iot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.EnableCompress;
import org.indunet.fastproto.annotation.EnableCrypto;
import org.indunet.fastproto.annotation.EnableVersion;
import org.indunet.fastproto.annotation.type.*;
import org.indunet.fastproto.compress.CompressPolicy;
import org.indunet.fastproto.crypto.CryptoPolicy;
import org.indunet.fastproto.iot.formula.DecodeSpeedFormula;
import org.indunet.fastproto.iot.formula.EncodeSpeedFormula;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * @author Deng Ran
 * @since 1.2.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EnableCrypto(value = CryptoPolicy.AES_ECB_PKCS5PADDING, key = "330926")
@EnableCompress(CompressPolicy.GZIP)
@EnableVersion(value = 78, version = 17)
public class Everything {
    @BoolType(value = 0, bitOffset = 1)
    Boolean aBoolean;

    @ByteType(1)
    Byte aByte;

    @ShortType(2)
    Short aShort;

    @Int32Type(4)
    Integer aInteger;

    @Int64Type(8)
    Long aLong;

    @FloatType(16)
    Float aFloat;

    @DoubleType(20)
    Double aDouble;

    @Int8Type(28)
    Integer aInteger8;

    @Int16Type(30)
    Integer aInteger16;

    @UInt8Type(32)
    Integer aUInteger8;

    @UInt16Type(34)
    Integer aUInteger16;

    @UInt32Type(36)
    Long aUInteger32;

    @BinaryType(value = 40, length = 10)
    byte[] aByteArray;

    @StringType(value = 50, length = 6)
    String aString;

    @TimeType(56)
    Timestamp aTimestamp;

    @CharType(64)
    Character aCharacter;

    @UInt8Type(value = 66, decodingFormula = DecodeSpeedFormula.class, encodingFormula = EncodeSpeedFormula.class)
    float speed;

    @UInt64Type(70)
    BigInteger aUInteger64;
}

