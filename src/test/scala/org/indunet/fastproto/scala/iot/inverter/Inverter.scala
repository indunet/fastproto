/*
 * Copyright 2019-2021 indunet
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

package org.indunet.fastproto.scala.iot.inverter

import org.indunet.fastproto.EndianPolicy
import org.indunet.fastproto.annotation.scala._

/**
 * @author Deng Ran
 * @since 2.4.0
 */
case class Inverter(
                     @UInt8Type(offset = 0) voltage: Int,
                     @UInt8Type(offset = 2) current: Int,
                     fan: Fan,
                     @Endian(EndianPolicy.BIG) @UInt16Type(offset = 8) power: Int
                   )
