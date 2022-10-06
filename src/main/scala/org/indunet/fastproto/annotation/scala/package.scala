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

import org.indunet.fastproto.annotation.`type`.{BinaryType => JavaBinaryType}
import org.indunet.fastproto.annotation.`type`.{BoolType => JavaBoolType}
import org.indunet.fastproto.annotation.`type`.{CharType => JavaCharType}
import org.indunet.fastproto.annotation.`type`.{DoubleType => JavaDoubleType}
import org.indunet.fastproto.annotation.`type`.{DoubleArrayType => JavaDoubleArrayType}
import org.indunet.fastproto.annotation.`type`.{EnumType => JavaEnumType}
import org.indunet.fastproto.annotation.`type`.{FloatType => JavaFloatType}
import org.indunet.fastproto.annotation.`type`.{FloatArrayType => JavaFloatArrayType}
import org.indunet.fastproto.annotation.`type`.{Int8Type => JavaInt8Type}
import org.indunet.fastproto.annotation.`type`.{Int8ArrayType => JavaInt8ArrayType}
import org.indunet.fastproto.annotation.`type`.{Int16Type => JavaInt16Type}
import org.indunet.fastproto.annotation.`type`.{Int16ArrayType => JavaInt16ArrayType}
import org.indunet.fastproto.annotation.`type`.{Int32Type => JavaInt32Type}
import org.indunet.fastproto.annotation.`type`.{Int32ArrayType => JavaInt32ArrayType}
import org.indunet.fastproto.annotation.`type`.{Int64Type => JavaInt64Type}
import org.indunet.fastproto.annotation.`type`.{Int64ArrayType => JavaInt64ArrayType}
import org.indunet.fastproto.annotation.`type`.{StringType => JavaStringType}
import org.indunet.fastproto.annotation.`type`.{TimeType => JavaTimeType}
import org.indunet.fastproto.annotation.`type`.{UInt8Type => JavaUInt8Type}
import org.indunet.fastproto.annotation.`type`.{UInt8ArrayType => JavaUInt8ArrayType}
import org.indunet.fastproto.annotation.`type`.{UInt16Type => JavaUInt16Type}
import org.indunet.fastproto.annotation.`type`.{UInt16ArrayType => JavaUInt16ArrayType}
import org.indunet.fastproto.annotation.`type`.{UInt32Type => JavaUInt32Type}
import org.indunet.fastproto.annotation.`type`.{UInt32ArrayType => JavaUInt32ArrayType}
import org.indunet.fastproto.annotation.`type`.{UInt64Type => JavaUInt64Type}
import org.indunet.fastproto.annotation.`type`.{UInt64ArrayType => JavaUInt64ArrayType}

import org.indunet.fastproto.annotation.{DefaultEndian => JavaDefaultEndian}
import org.indunet.fastproto.annotation.{EncodingIgnore => JavaEncodingIgnore}
import org.indunet.fastproto.annotation.{DecodingIgnore => JavaDecodingIgnore}

import org.indunet.fastproto.annotation.{FixedLength => JavaFixedLength}

import _root_.scala.annotation.meta.field

/**
 * Scala annotations.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
package object scala {
  type BinaryType = JavaBinaryType @field
  type BoolType = JavaBoolType @field
  type CharType = JavaCharType @field
  type DoubleType = JavaDoubleType @field
  type DoubleArrayType = JavaDoubleArrayType @field
  type EnumType = JavaEnumType @field
  type FloatType = JavaFloatType @field
  type FloatArrayType = JavaFloatArrayType @field
  type Int8Type = JavaInt8Type @field
  type Int8ArrayType = JavaInt8ArrayType @field
  type Int16Type = JavaInt16Type @field
  type Int16ArrayType = JavaInt16ArrayType @field
  type Int32Type = JavaInt32Type @field
  type Int32ArrayType = JavaInt32ArrayType @field
  type Int64Type = JavaInt64Type @field
  type Int64ArrayType = JavaInt64ArrayType @field
  type StringType = JavaStringType @field
  type TimeType = JavaTimeType @field
  type UInt8Type = JavaUInt8Type @field
  type UInt8ArrayType = JavaUInt8ArrayType @field
  type UInt16Type = JavaUInt16Type @field
  type UInt16ArrayType = JavaUInt16ArrayType @field
  type UInt32Type = JavaUInt32Type @field
  type UInt32ArrayType = JavaUInt32ArrayType @field
  type UInt64Type = JavaUInt64Type @field
  type UInt64ArrayType = JavaUInt64ArrayType @field

  type DefaultEndian = JavaDefaultEndian @field
  type DecodingIgnore = JavaDecodingIgnore @field
  type EncodingIgnore = JavaEncodingIgnore @field

  type FixedLength = JavaFixedLength
}
