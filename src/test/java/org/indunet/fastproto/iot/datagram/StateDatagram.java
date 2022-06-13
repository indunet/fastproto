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

package org.indunet.fastproto.iot.datagram;

import lombok.Data;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.type.*;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * @author Deng Ran
 * @since 1.6.2
 */
@Data
@Endian(EndianPolicy.LITTLE)
public class StateDatagram {
    @UInt8Type(DatagramProtocol.VEHICLE_MODEL_NUMBER_OFFSET)
    Integer vehicleModelNumber;
    Long vehicleModelId;

    @UInt8Type(DatagramProtocol.SERIAL_NUMBER_OFFSET)
    Integer serialNumber;

    @UInt8Type(DatagramProtocol.PROTOCOL_VERSION_OFFSET)
    Integer protocolVersion;
    Long datagramProtocolId;

    @TimeType(value = DatagramProtocol.EVENT_TIME_OFFSET, genericType = UInt32Type.class, unit = TimeUnit.SECONDS)
    Timestamp eventTime;
    Timestamp receiveTime;

    @UInt16Type(DatagramProtocol.VEHICLE_NUMBER_OFFSET)
    Integer vehicleNumber;
    Long vehicleId;
    String vehicleCode;

    @UInt16Type(DatagramProtocol.DATAGRAM_TYPE_NUMBER_OFFSET)
    Integer datagramTypeNumber;

    @UInt16Type(DatagramProtocol.DATAGRAM_NUMBER_OFFSET)
    Integer datagramNumber;
    Long datagramId;

    @UInt16Type(DatagramProtocol.SECURITY_CODE_OFFSET)
    Integer securityCode;

    @UInt16Type(DatagramProtocol.CRC16_CODE_OFFSET)
    Integer crc16Code;

    @BinaryType(value = 0, length = -1)
    byte[] bytes;

    CheckCode checkCode;
}