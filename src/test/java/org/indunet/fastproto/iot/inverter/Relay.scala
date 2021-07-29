package org.indunet.fastproto.iot.inverter

import org.indunet.fastproto.annotation.`type`.{BooleanType, UInteger8Type}

import scala.annotation.meta.field

class Relay {
  @(BooleanType @field)(value = 0, bitOffset = 1)
  var state: Boolean = false
}
