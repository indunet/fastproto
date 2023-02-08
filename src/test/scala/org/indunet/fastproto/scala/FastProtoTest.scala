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

package org.indunet.fastproto.scala

import org.indunet.fastproto.scala.iot.inverter.{Fan, Inverter}
import org.indunet.fastproto.util.EncodeUtils
import org.indunet.fastproto.{ByteOrder, FastProto}
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Unit test of FastProto API in Scala.
 *
 * @author Deng Ran
 * @since 2.4.0
 */
class FastProtoTest {
  @Test
  def testInverter(): Unit = {
    val bytes = new Array[Byte](10)

    bytes(0) = 1
    bytes(2) = 2

    EncodeUtils.writeFloat(bytes, 4, ByteOrder.LITTLE, 10.2f)
    EncodeUtils.writeUInt16(bytes, 8, ByteOrder.BIG, 192)

    val expected = Inverter(1, 2, Fan(10.2f, false), 192)
    val inverter = FastProto.decode(bytes, classOf[Inverter])

    assertEquals(expected.toString, inverter.toString)
  }
}
