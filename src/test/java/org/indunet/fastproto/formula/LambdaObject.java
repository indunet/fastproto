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

package org.indunet.fastproto.formula;

import lombok.Data;
import lombok.val;
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.util.BinaryUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Deng Ran
 * @since 2.0.0
 */
@Data
public class LambdaObject {
    @UInt16Type(offset = 0)
    @DecodingFormula(lambda = "x -> x * 0.1")
    @EncodingFormula(lambda = "x -> (int) (x * 10)")
    Double speed = 8.1;

    @UInt8Type(offset = 2)
    @DecodingFormula(lambda = "x -> x * 10")
    @EncodingFormula(lambda = "x -> x / 10")
    Integer current = 160;

    public byte[] toBytes() throws IOException {
        val stream = new ByteArrayOutputStream();

        stream.write(BinaryUtils.int16Of(81, EndianPolicy.LITTLE));
        stream.write(new byte[] {(byte) (this.current / 10)});

        return stream.toByteArray();
    }
}
