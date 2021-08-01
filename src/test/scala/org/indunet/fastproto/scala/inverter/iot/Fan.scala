package org.indunet.fastproto.scala.inverter.iot

import org.indunet.fastproto.annotation.`type`.{BooleanType, FloatType}

import scala.annotation.meta.field

/**
 * @author Deng Ran
 * @since 2.4.0
 */
case class Fan(
                @(FloatType@field)(4)
                speed: Float,
                @(BooleanType@field)(value = 8, bitOffset = 0)
                state: Boolean
              )
