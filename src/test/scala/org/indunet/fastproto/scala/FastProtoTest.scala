package org.indunet.fastproto.scala

import org.indunet.fastproto.FastProto
import org.indunet.fastproto.scala.iot.inverter.Inverter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author Deng Ran
 * @since 2.4.0
 */
class FastProtoTest {
  @Test
  def testInverter(): Unit = {
    val datagram = new Array[Byte](10)

    datagram(0) = 1
    datagram(2) = 2

    val expected = Inverter(1, 2)
    val inverter = FastProto.parseFrom(datagram, classOf[Inverter])

    assertEquals(expected.toString, inverter.toString)
  }
}
