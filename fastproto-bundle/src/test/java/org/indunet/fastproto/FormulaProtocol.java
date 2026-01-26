/*
 * Copyright 2019-2024 indunet.org
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

package org.indunet.fastproto;

import lombok.Data;
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;
import org.indunet.fastproto.annotation.Int16Type;
import org.indunet.fastproto.annotation.UInt32Type;

/**
 * Protocol class with lambda formulas for testing processor integration.
 * Lambda formulas require the annotation processor to generate Function classes at compile time.
 *
 * @author Deng Ran
 * @since 4.1.0
 */
@Data
public class FormulaProtocol {
    @Int16Type(offset = 0)
    @DecodingFormula(lambda = "x -> x * 0.1")           // raw -> engineering (x * 0.1)
    @EncodingFormula(lambda = "x -> (int)(x * 10)")     // engineering -> raw (x * 10)
    private double temperature;

    @UInt32Type(offset = 4)
    @DecodingFormula(lambda = "x -> x * 0.01")          // raw -> engineering (x * 0.01)
    @EncodingFormula(lambda = "x -> (long)(x * 100)")   // engineering -> raw (x * 100)
    private double pressure;
}
