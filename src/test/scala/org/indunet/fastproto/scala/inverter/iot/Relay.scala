package org.indunet.fastproto.scala.inverter.iot

import org.indunet.fastproto.annotation.`type`.BooleanType

import scala.annotation.meta.field

/**
 * @author Deng Ran
 * @since 2.4.0
 */
class Relay {
  @(BooleanType @field)(value = 0, bitOffset = 1)
  var state = false
}
