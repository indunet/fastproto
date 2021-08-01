package org.indunet.fastproto.scala

import org.indunet.fastproto.FastProto
import org.indunet.fastproto.scala.inverter.iot.Inverter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FastProtoTest {
  @Test
  def testInverter(): Unit = {
    val datagram = new Array[Byte](10)

    datagram(0) = 1
    datagram(2) = 2

    val expected = new Inverter(1, 2)
    val inverter = FastProto.parseFrom(datagram, classOf[Inverter])

    assertEquals(expected.toString, inverter.toString)
  }
}
