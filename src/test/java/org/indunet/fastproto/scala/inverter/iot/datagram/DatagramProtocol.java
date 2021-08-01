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

package org.indunet.fastproto.scala.inverter.iot.datagram;

/**
 * @author Deng Ran
 * @since 1.6.2
 */
public class DatagramProtocol {
    public static final int VEHICLE_MODEL_NUMBER_OFFSET = 0;
    public static final int SERIAL_NUMBER_OFFSET = 4;
    public static final int PROTOCOL_VERSION_OFFSET = 11;
    public static final int EVENT_TIME_OFFSET = 14;
    public static final int VEHICLE_NUMBER_OFFSET = 18;
    public static final int DATAGRAM_TYPE_NUMBER_OFFSET = 20;
    public static final int DATAGRAM_NUMBER_OFFSET = 24;
    public static final int SECURITY_CODE_OFFSET = -4;
    public static final int CRC16_CODE_OFFSET = -2;
}
