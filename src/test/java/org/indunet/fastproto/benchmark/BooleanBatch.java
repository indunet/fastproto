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

package org.indunet.fastproto.benchmark;

import org.indunet.fastproto.annotation.type.BooleanType;

/**
 * @author Deng Ran
 * @since 1.4.0
 */
public class BooleanBatch {
    protected static final int BYTE_OFFET = 0;

    @BooleanType(value = BYTE_OFFET, bitOffset = 0)
    Boolean b01;

    @BooleanType(value = BYTE_OFFET, bitOffset = 1)
    Boolean b02;

    @BooleanType(value = BYTE_OFFET, bitOffset = 2)
    Boolean b03;

    @BooleanType(value = BYTE_OFFET, bitOffset = 3)
    Boolean b04;

    @BooleanType(value = BYTE_OFFET, bitOffset = 4)
    Boolean b05;

    @BooleanType(value = BYTE_OFFET, bitOffset = 5)
    Boolean b06;

    @BooleanType(value = BYTE_OFFET, bitOffset = 6)
    Boolean b07;

    @BooleanType(value = BYTE_OFFET, bitOffset = 7)
    Boolean b08;

    @BooleanType(value = BYTE_OFFET + 1, bitOffset = 0)
    Boolean b09;

    @BooleanType(value = BYTE_OFFET + 1, bitOffset = 1)
    Boolean b10;

    @BooleanType(value = BYTE_OFFET + 1, bitOffset = 2)
    Boolean b11;

    @BooleanType(value = BYTE_OFFET + 1, bitOffset = 3)
    Boolean b12;

    @BooleanType(value = BYTE_OFFET + 1, bitOffset = 4)
    Boolean b13;

    @BooleanType(value = BYTE_OFFET + 1, bitOffset = 5)
    Boolean b14;

    @BooleanType(value = BYTE_OFFET + 1, bitOffset = 6)
    Boolean b15;

    @BooleanType(value = BYTE_OFFET + 1, bitOffset = 7)
    Boolean b16;

    @BooleanType(value = BYTE_OFFET + 2, bitOffset = 0)
    Boolean b17;

    @BooleanType(value = BYTE_OFFET + 2, bitOffset = 1)
    Boolean b18;

    @BooleanType(value = BYTE_OFFET + 2, bitOffset = 2)
    Boolean b19;

    @BooleanType(value = BYTE_OFFET + 2, bitOffset = 3)
    Boolean b20;

    @BooleanType(value = BYTE_OFFET + 2, bitOffset = 4)
    Boolean b21;

    @BooleanType(value = BYTE_OFFET + 2, bitOffset = 5)
    Boolean b22;

    @BooleanType(value = BYTE_OFFET + 2, bitOffset = 6)
    Boolean b23;

    @BooleanType(value = BYTE_OFFET + 2, bitOffset = 7)
    Boolean b24;
}
