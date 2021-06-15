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

package org.indunet.fastproto.performance;

import org.indunet.fastproto.annotation.type.IntegerType;

/**
 * @author Deng Ran
 * @since 1.4.0
 */
public class IntegerBatch {
    protected static final int BYTE_OFFET = 4;

    @IntegerType(value = BYTE_OFFET)
    Integer i01;

    @IntegerType(value = BYTE_OFFET + 4)
    Integer i02;

    @IntegerType(value = BYTE_OFFET + 4 * 2)
    Integer i03;

    @IntegerType(value = BYTE_OFFET + 4 * 3)
    Integer i04;

    @IntegerType(value = BYTE_OFFET + 4 * 4)
    Integer i05;

    @IntegerType(value = BYTE_OFFET + 4 * 5)
    Integer i06;

    @IntegerType(value = BYTE_OFFET + 4 * 6)
    Integer i07;

    @IntegerType(value = BYTE_OFFET + 4 * 7)
    Integer i08;

    @IntegerType(value = BYTE_OFFET + 4 * 8)
    Integer i09;

    @IntegerType(value = BYTE_OFFET + 4 * 9)
    Integer i10;

    @IntegerType(value = BYTE_OFFET + 4 * 10)
    Integer i11;

    @IntegerType(value = BYTE_OFFET + 4 * 11)
    Integer i12;

    @IntegerType(value = BYTE_OFFET + 4 * 12)
    Integer i13;

    @IntegerType(value = BYTE_OFFET + 4 * 13)
    Integer i14;

    @IntegerType(value = BYTE_OFFET + 4 * 14)
    Integer i15;

    @IntegerType(value = BYTE_OFFET + 4 * 15)
    Integer i16;
}
