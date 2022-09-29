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

package org.indunet.fastproto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.EnableCompress;
import org.indunet.fastproto.annotation.EnableCrypto;
import org.indunet.fastproto.annotation.type.*;
import org.indunet.fastproto.compress.CompressPolicy;
import org.indunet.fastproto.crypto.CryptoPolicy;
import org.indunet.fastproto.domain.formula.DecodeSpeedFormula;
import org.indunet.fastproto.domain.formula.EncodeSpeedFormula;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

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
public class Everything {
    @BoolType(byteOffset = 0, bitOffset = 1)
    Boolean aBoolean;

    @Int8Type(offset = 1)
    Byte aByte;

    @Int16Type(offset = 2)
    Short aShort;

    @Int32Type(offset = 4)
    Integer aInteger;

    @Int64Type(offset = 8)
    long aLong;

    @FloatType(offset = 16)
    Float aFloat;

    @DoubleType(offset = 20)
    Double aDouble;

    @Int8Type(offset = 28)
    Integer aInteger8;

    @Int16Type(offset = 30)
    Integer aInteger16;

    @UInt8Type(offset = 32)
    Integer aUInteger8;

    @UInt16Type(offset = 34)
    Integer aUInteger16;

    @UInt32Type(offset = 36)
    Long aUInteger32;

    @BinaryType(offset = 40, length = 10)
    byte[] aByteArray;

    @StringType(offset = 50, length = 6)
    String aString;

    @TimeType(offset = 56)
    Timestamp aTimestamp;

    @CharType(offset = 64)
    Character aCharacter;

    @UInt8Type(offset = 66, decodingFormula = DecodeSpeedFormula.class, encodingFormula = EncodeSpeedFormula.class)
    float speed;

    @UInt64Type(offset = 70)
    BigInteger aUInteger64;

    @TimeType(offset = 78)
    Date date;

    @TimeType(offset = 86)
    Calendar calendar;

    @TimeType(offset = 94)
    Instant instant;
}

