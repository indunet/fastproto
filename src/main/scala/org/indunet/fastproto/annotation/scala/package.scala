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
import org.indunet.fastproto.annotation.`type`.{BinaryType => JavaBinaryType}
import org.indunet.fastproto.annotation.`type`.{BoolType => JavaBooleanType}
import org.indunet.fastproto.annotation.`type`.{ByteType => JavaByteType}
import org.indunet.fastproto.annotation.`type`.{CharType => JavaCharacterType}
import org.indunet.fastproto.annotation.`type`.{DoubleType => JavaDoubleType}
import org.indunet.fastproto.annotation.`type`.{EnumType => JavaEnumType}
import org.indunet.fastproto.annotation.`type`.{FloatType => JavaFloatType}
import org.indunet.fastproto.annotation.`type`.{Int8Type => JavaInteger8Type}
import org.indunet.fastproto.annotation.`type`.{Int16Type => JavaInteger16Type}
import org.indunet.fastproto.annotation.`type`.{Int32Type => JavaIntegerType}
import org.indunet.fastproto.annotation.`type`.{ListType => JavaListType}
import org.indunet.fastproto.annotation.`type`.{Int64Type => JavaLongType}
import org.indunet.fastproto.annotation.`type`.{ShortType => JavaShortType}
import org.indunet.fastproto.annotation.`type`.{StringType => JavaStringType}
import org.indunet.fastproto.annotation.`type`.{TimeType => JavaTimestampType}
import org.indunet.fastproto.annotation.`type`.{UInt8Type => JavaUInteger8Type}
import org.indunet.fastproto.annotation.`type`.{UInt16Type => JavaUInteger16Type}
import org.indunet.fastproto.annotation.`type`.{UInt32Type => JavaUInteger32Type}
import org.indunet.fastproto.annotation.`type`.{UInt64Type => JavaUInteger64Type}

import org.indunet.fastproto.annotation.{Endian => JavaEndian}
import org.indunet.fastproto.annotation.{EncodingIgnore => JavaEncodingIgnore}
import org.indunet.fastproto.annotation.{DecodingIgnore => JavaDecodingIgnore}

import org.indunet.fastproto.annotation.{EnableChecksum => JavaEnableChecksum}
import org.indunet.fastproto.annotation.{EnableCompress => JavaEnableCompress}
import org.indunet.fastproto.annotation.{EnableCrypto => JavaEnableCrypto}
import org.indunet.fastproto.annotation.{EnableProtocolVersion => JavaEnableProtocolVersion}
import org.indunet.fastproto.annotation.{EnableProtocolVersions => JavaEnableProtocolVersions}
import org.indunet.fastproto.annotation.{EnableFixedLength => JavaEnableFixedLength}

import _root_.scala.annotation.meta.field

/**
 * Scala annotations.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
package object scala {
  type ArrayType = JavaArrayType @field
  type BinaryType = JavaBinaryType @field
  type BooleanType = JavaBooleanType @field
  type ByteType = JavaByteType @field
  type CharacterType = JavaCharacterType @field
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
  type EnableProtocolVersions = JavaEnableProtocolVersions
  type EnableFixedLength = JavaEnableFixedLength
}
