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

package org.indunet.fastproto.scala.inverter.iot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.indunet.fastproto.annotation.EnableCompress;
import org.indunet.fastproto.annotation.EnableCrypto;
import org.indunet.fastproto.annotation.EnableProtocolVersion;
import org.indunet.fastproto.annotation.type.*;
import org.indunet.fastproto.compress.CompressPolicy;
import org.indunet.fastproto.crypto.CryptoPolicy;
import org.indunet.fastproto.scala.inverter.iot.formula.DecodeSpeedFormula;
import org.indunet.fastproto.scala.inverter.iot.formula.EncodeSpeedFormula;

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
@EnableProtocolVersion(value = 78, version = 17)
public class Everything {
    @BooleanType(value = 0, bitOffset = 1)
    Boolean aBoolean;

    @ByteType(1)
    Byte aByte;

    @ShortType(2)
    Short aShort;

    @IntegerType(4)
    Integer aInteger;

    @LongType(8)
    Long aLong;

    @FloatType(16)
    Float aFloat;

    @DoubleType(20)
    Double aDouble;

    @Integer8Type(28)
    Integer aInteger8;

    @Integer16Type(30)
    Integer aInteger16;

    @UInteger8Type(32)
    Integer aUInteger8;

    @UInteger16Type(34)
    Integer aUInteger16;

    @UInteger32Type(36)
    Long aUInteger32;

    @BinaryType(value = 40, length = 10)
    byte[] aByteArray;

    @StringType(value = 50, length = 6)
    String aString;

    @TimestampType(56)
    Timestamp aTimestamp;

    @CharacterType(64)
    Character aCharacter;

    @UInteger8Type(value = 66, decodingFormula = DecodeSpeedFormula.class, encodingFormula = EncodeSpeedFormula.class)
    float speed;

    @AutoType(70)
    BigInteger aUInteger64;
}

