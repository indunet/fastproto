/*
 * Copyright 2019-2022 indunet.org
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

package org.indunet.fastproto.api.time;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.annotation.TimeType;
import org.indunet.fastproto.io.ByteBufferOutputStream;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

/**
 * Time object.
 *
 * @author Deng Ran
 * @since 3.4.0
 */
@Data
public class TimeObject {
    @TimeType(offset = 0)
    Date date = new Date();
    @TimeType(offset = 8)
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    @TimeType(offset = 16)
    Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
    @TimeType(offset = 24)
    Calendar calendar = Calendar.getInstance();

    public byte[] toBytes() {
        val outputStream = new ByteBufferOutputStream();

        outputStream.writeInt64(ByteOrder.LITTLE, date.getTime());
        outputStream.writeInt64(ByteOrder.LITTLE, timestamp.getTime());
        outputStream.writeInt64(ByteOrder.LITTLE, instant.toEpochMilli());
        outputStream.writeInt64(ByteOrder.LITTLE, calendar.getTimeInMillis());

        return outputStream.toByteBuffer().toBytes();
    }
}
