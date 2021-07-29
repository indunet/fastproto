package org.indunet.fastproto.iot.inverter

import org.indunet.fastproto.annotation.`type`.{BooleanType, FloatType}

import scala.annotation.meta.field

case class Fan(
              @(FloatType @field)(4)
              speed: Float,
              @(BooleanType @field)(value = 8, bitOffset = 0)
              state: Boolean
              )
