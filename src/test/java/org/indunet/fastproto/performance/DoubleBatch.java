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

import org.indunet.fastproto.annotation.type.DoubleType;

/**
 * @author Deng Ran
 * @since 1.4.0
 */
public class DoubleBatch {
    protected static final int BYTE_OFFET = 64;

    @DoubleType(value = BYTE_OFFET)
    Double d01;

    @DoubleType(value = BYTE_OFFET + 8)
    Double d02;

    @DoubleType(value = BYTE_OFFET + 8 * 2)
    Double d03;

    @DoubleType(value = BYTE_OFFET + 8 * 3)
    Double d04;

    @DoubleType(value = BYTE_OFFET + 8 * 4)
    Double d05;

    @DoubleType(value = BYTE_OFFET + 8 * 5)
    Double d06;

    @DoubleType(value = BYTE_OFFET + 8 * 6)
    Double d07;

    @DoubleType(value = BYTE_OFFET + 8 * 7)
    Double d08;
}
