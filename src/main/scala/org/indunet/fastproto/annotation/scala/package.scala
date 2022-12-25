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

import org.indunet.fastproto.annotation.{BinaryType => JavaBinaryType, BoolType => JavaBoolType, CharType => JavaCharType, AsciiType => JavaAsciiType, DefaultEndian => JavaDefaultEndian, DoubleArrayType => JavaDoubleArrayType, DoubleType => JavaDoubleType, EnumType => JavaEnumType, FloatArrayType => JavaFloatArrayType, FloatType => JavaFloatType, Int16ArrayType => JavaInt16ArrayType, Int16Type => JavaInt16Type, Int32ArrayType => JavaInt32ArrayType, Int32Type => JavaInt32Type, Int64ArrayType => JavaInt64ArrayType, Int64Type => JavaInt64Type, Int8ArrayType => JavaInt8ArrayType, Int8Type => JavaInt8Type, StringType => JavaStringType, TimeType => JavaTimeType, UInt16ArrayType => JavaUInt16ArrayType, UInt16Type => JavaUInt16Type, UInt32ArrayType => JavaUInt32ArrayType, UInt32Type => JavaUInt32Type, UInt64ArrayType => JavaUInt64ArrayType, UInt64Type => JavaUInt64Type, UInt8ArrayType => JavaUInt8ArrayType, UInt8Type => JavaUInt8Type}
import org.indunet.fastproto.annotation.{AutoType => JavaAutoType}
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
  type AsciiType = JavaAsciiType @field
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
  type AutoType = JavaAutoType @field

  type DefaultEndian = JavaDefaultEndian @field
  type DecodingIgnore = JavaDecodingIgnore @field
  type EncodingIgnore = JavaEncodingIgnore @field

  type FixedLength = JavaFixedLength
}
