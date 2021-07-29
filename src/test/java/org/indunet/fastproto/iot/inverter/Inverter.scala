package org.indunet.fastproto.iot.inverter

import org.indunet.fastproto.annotation.`type`.{FloatType, UInteger16Type, UInteger8Type}

import annotation.meta.field

case class Inverter(
                   @(UInteger8Type @field)(0) voltage: Int,
                   @(UInteger16Type @field)(2) current: Int
                   // fan: Fan
                   )
