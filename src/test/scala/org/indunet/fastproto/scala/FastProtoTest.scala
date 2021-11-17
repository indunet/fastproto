package org.indunet.fastproto.scala

import org.indunet.fastproto.{EndianPolicy, FastProto}
import org.indunet.fastproto.scala.iot.inverter.{Fan, Inverter}
import org.indunet.fastproto.util.CodecUtils
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

    CodecUtils.floatType(datagram, 4, EndianPolicy.LITTLE, 10.2f)
    CodecUtils.uinteger16Type(datagram, 8, EndianPolicy.BIG, 192)

    val expected = Inverter(1, 2, Fan(10.2f, false), 192)
    val inverter = FastProto.parseFrom(datagram, classOf[Inverter])

    assertEquals(expected.toString, inverter.toString)
  }
}
