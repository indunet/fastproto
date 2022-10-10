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

package org.indunet.fastproto.domain.datagram;

import lombok.Data;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.*;

import java.sql.Timestamp;

/**
 * @author Deng Ran
 * @since 1.6.2
 */
@Data
@DefaultEndian(EndianPolicy.LITTLE)
public class StateDatagram {
    @UInt8Type(offset = DatagramProtocol.VEHICLE_MODEL_NUMBER_OFFSET)
    Integer vehicleModelNumber;
    Long vehicleModelId;

    @UInt8Type(offset = DatagramProtocol.SERIAL_NUMBER_OFFSET)
    Integer serialNumber;

    @UInt8Type(offset = DatagramProtocol.PROTOCOL_VERSION_OFFSET)
    Integer protocolVersion;
    Long datagramProtocolId;

    @TimeType(offset = DatagramProtocol.EVENT_TIME_OFFSET)
    Timestamp eventTime;
    Timestamp receiveTime;

    @UInt16Type(offset = DatagramProtocol.VEHICLE_NUMBER_OFFSET)
    Integer vehicleNumber;
    Long vehicleId;
    String vehicleCode;

    @UInt16Type(offset = DatagramProtocol.DATAGRAM_TYPE_NUMBER_OFFSET)
    Integer datagramTypeNumber;

    @UInt16Type(offset = DatagramProtocol.DATAGRAM_NUMBER_OFFSET)
    Integer datagramNumber;
    Long datagramId;

    @UInt16Type(offset = DatagramProtocol.SECURITY_CODE_OFFSET)
    Integer securityCode;

    @UInt16Type(offset = DatagramProtocol.CRC16_CODE_OFFSET)
    Integer crc16Code;

    @BinaryType(offset = 0, length = -1)
    byte[] bytes;

    CheckCode checkCode;
}
