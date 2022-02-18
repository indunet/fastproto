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

import org.indunet.fastproto.annotation.`type`.{CharType => JavaCharType, ArrayType => JavaArrayType, BinaryType => JavaBinaryType, BoolType => JavaBoolType, BooleanType => JavaBooleanType, ByteType => JavaByteType, DoubleType => JavaDoubleType, EnumType => JavaEnumType, FloatType => JavaFloatType, Int16Type => JavaInt16Type, Int32Type => JavaInt32Type, Int64Type => JavaInt64Type, Int8Type => JavaInt8Type, ListType => JavaListType, ShortType => JavaShortType, StringType => JavaStringType, TimeType => JavaTimeType, UInt16Type => JavaUInt16Type, UInt32Type => JavaUInt32Type, UInt64Type => JavaUInt64Type, UInt8Type => JavaUInt8Type}
import org.indunet.fastproto.annotation.{Endian => JavaEndian}
import org.indunet.fastproto.annotation.{EncodingIgnore => JavaEncodingIgnore}
import org.indunet.fastproto.annotation.{DecodingIgnore => JavaDecodingIgnore}
import org.indunet.fastproto.annotation.{EnableChecksum => JavaEnableChecksum}
import org.indunet.fastproto.annotation.{EnableCompress => JavaEnableCompress}
import org.indunet.fastproto.annotation.{EnableCrypto => JavaEnableCrypto}
import org.indunet.fastproto.annotation.{EnableVersion => JavaEnableProtocolVersion}
import org.indunet.fastproto.annotation.{EnableFixedLength => JavaEnableFixedLength}

import _root_.scala.annotation.meta.field

/**
 * Scala type annotations.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
package object scala {
  type ArrayType = JavaArrayType @field
  type BinaryType = JavaBinaryType @field
  type BoolType = JavaBoolType @field
  type BooleanType =  JavaBooleanType @field
  type ByteType = JavaByteType @field
  type CharType = JavaCharType @field
  type DoubleType = JavaDoubleType @field
  type EnumType = JavaEnumType @field
  type FloatType = JavaFloatType @field
  type Int8Type = JavaInt8Type @field
  type Int16Type = JavaInt16Type @field
  type Int32Type = JavaInt32Type @field
  type ListType = JavaListType @field
  type Int64Type = JavaInt64Type @field
  type ShortType = JavaShortType @field
  type StringType = JavaStringType @field
  type TimeType = JavaTimeType @field
  type UInt8Type = JavaUInt8Type @field
  type UInt16Type = JavaUInt16Type @field
  type UInt32Type = JavaUInt32Type @field
  type UInt64Type = JavaUInt64Type @field

  type Endian = JavaEndian @field
  type DecodingIgnore = JavaDecodingIgnore @field
  type EncodingIgnore = JavaEncodingIgnore @field

  type EnableChecksum = JavaEnableChecksum
  type EnableCompress = JavaEnableCompress
  type EnableCrypto = JavaEnableCrypto
  type EnableProtocolVersion = JavaEnableProtocolVersion
  type EnableFixedLength = JavaEnableFixedLength
}
