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
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.Endian;
import org.indunet.fastproto.annotation.type.BinaryType;
import org.indunet.fastproto.annotation.type.TimestampType;
import org.indunet.fastproto.annotation.type.UInteger16Type;
import org.indunet.fastproto.annotation.type.UInteger8Type;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static org.indunet.fastproto.iot.datagram.DatagramProtocol.*;

/**
 * @author Deng Ran
 * @since 1.6.2
 */
@Data
@Endian(EndianPolicy.LITTLE)
public class StateDatagram {
    @UInteger8Type(VEHICLE_MODEL_NUMBER_OFFSET)
    Integer vehicleModelNumber;
//    Long vehicleModelId;

    @UInteger8Type(SERIAL_NUMBER_OFFSET)
    Integer serialNumber;

    @UInteger8Type(PROTOCOL_VERSION_OFFSET)
    Integer protocolVersion;
//    Long datagramProtocolId;

    @TimestampType(value = EVENT_TIME_OFFSET, protocolType = ProtocolType.UINTEGER32, unit = TimeUnit.SECONDS)
    Timestamp eventTime;
////    Timestamp receiveTime;

    @UInteger16Type(VEHICLE_NUMBER_OFFSET)
    Integer vehicleNumber;
////    Long vehicleId;
////    String vehicleCode;

    @UInteger16Type(DATAGRAM_TYPE_NUMBER_OFFSET)
    Integer datagramTypeNumber;

    @UInteger16Type(DATAGRAM_NUMBER_OFFSET)
    Integer datagramNumber;
//    Long datagramId;

    @UInteger16Type(SECURITY_CODE_OFFSET)
    Integer securityCode;

    @UInteger16Type(CRC16_CODE_OFFSET)
    Integer crc16Code;
    //
    @BinaryType(value = 0, length = -1)
    byte[] bytes;

     CheckCode checkCode;
}
