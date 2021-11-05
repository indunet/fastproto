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

package org.indunet.fastproto.annotation

import org.indunet.fastproto.annotation.`type`.{ArrayType => JavaArrayType}
import org.indunet.fastproto.annotation.`type`.{AutoType => JavaAutoType}
import org.indunet.fastproto.annotation.`type`.{BinaryType => JavaBinaryType}
import org.indunet.fastproto.annotation.`type`.{BooleanType => JavaBooleanType}
import org.indunet.fastproto.annotation.`type`.{ByteType => JavaByteType}
import org.indunet.fastproto.annotation.`type`.{CharacterType => JavaCharacterType}
import org.indunet.fastproto.annotation.`type`.{DateType => JavaDateType}
import org.indunet.fastproto.annotation.`type`.{DoubleType => JavaDoubleType}
import org.indunet.fastproto.annotation.`type`.{EnumType => JavaEnumType}
import org.indunet.fastproto.annotation.`type`.{FloatType => JavaFloatType}
import org.indunet.fastproto.annotation.`type`.{Integer8Type => JavaInteger8Type}
import org.indunet.fastproto.annotation.`type`.{Integer16Type => JavaInteger16Type}
import org.indunet.fastproto.annotation.`type`.{IntegerType => JavaIntegerType}
import org.indunet.fastproto.annotation.`type`.{ListType => JavaListType}
import org.indunet.fastproto.annotation.`type`.{LongType => JavaLongType}
import org.indunet.fastproto.annotation.`type`.{ShortType => JavaShortType}
import org.indunet.fastproto.annotation.`type`.{StringType => JavaStringType}
import org.indunet.fastproto.annotation.`type`.{TimestampType => JavaTimestampType}
import org.indunet.fastproto.annotation.`type`.{UInteger8Type => JavaUInteger8Type}
import org.indunet.fastproto.annotation.`type`.{UInteger16Type => JavaUInteger16Type}
import org.indunet.fastproto.annotation.`type`.{UInteger32Type => JavaUInteger32Type}
import org.indunet.fastproto.annotation.`type`.{UInteger64Type => JavaUInteger64Type}

import org.indunet.fastproto.annotation.{Endian => JavaEndian}
import org.indunet.fastproto.annotation.{EncodeIgnore => JavaEncodingIgnore}
import org.indunet.fastproto.annotation.{DecodeIgnore => JavaDecodingIgnore}

import org.indunet.fastproto.annotation.{EnableChecksum => JavaEnableChecksum}
import org.indunet.fastproto.annotation.{EnableCompress => JavaEnableCompress}
import org.indunet.fastproto.annotation.{EnableCrypto => JavaEnableCrypto}
import org.indunet.fastproto.annotation.{EnableProtocolVersion => JavaEnableProtocolVersion}

import _root_.scala.annotation.meta.field

/**
 * Scala annotations.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
package object scala {
  type ArrayType = JavaArrayType @field
  type AutoType = JavaAutoType @field
  type BinaryType = JavaBinaryType @field
  type BooleanType = JavaBooleanType @field
  type ByteType = JavaByteType @field
  type CharacterType = JavaCharacterType @field
  type DateType = JavaDateType @field
  type DoubleType = JavaDoubleType @field
  type EnumType = JavaEnumType @field
  type FloatType = JavaFloatType @field
  type Int8Type = JavaInteger8Type @field
  type Int16Type = JavaInteger16Type @field
  type IntType = JavaIntegerType @field
  type ListType = JavaListType @field
  type LongType = JavaLongType @field
  type ShortType = JavaShortType @field
  type StringType = JavaStringType @field
  type TimestampType = JavaTimestampType @field
  type UInt8Type = JavaUInteger8Type @field
  type UInt16Type = JavaUInteger16Type @field
  type UInt32Type = JavaUInteger32Type @field
  type UInt64Type = JavaUInteger64Type @field

  type Endian = JavaEndian @field
  type DecodingIgnore = JavaDecodingIgnore @field
  type EncodingIgnore = JavaEncodingIgnore @field

  type EnableChecksum = JavaEnableChecksum
  type EnableCompress = JavaEnableCompress
  type EnableCrypto = JavaEnableCrypto
  type EnableProtocolVersion = JavaEnableProtocolVersion
}
