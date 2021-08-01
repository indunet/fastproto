package org.indunet.fastproto.scala.inverter.iot

import org.indunet.fastproto.annotation.`type`.{UInteger16Type, UInteger8Type}

import scala.annotation.meta.field

/**
 * @author Deng Ran
 * @since 2.4.0
 */
case class Inverter(
                     @(UInteger8Type@field)(0) voltage: Int,
                     @(UInteger16Type@field)(2) current: Int
                     // fan: Fan
                   )
