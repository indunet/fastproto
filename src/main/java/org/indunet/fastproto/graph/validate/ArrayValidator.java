/*
 * Copyright 2019-2021 indunet.org
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

package org.indunet.fastproto.graph.validate;

import lombok.val;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.ArrayType;
import org.indunet.fastproto.exception.CodecException;

import java.util.Arrays;

/**
 * Array type validation flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class ArrayValidator extends TypeValidator {
    @Override
    public void process(ValidatorContext context) {
        val protocolType = context.getProtocolType();

        if (typeAnnotation instanceof ArrayType) {
            val protocolType = ((ArrayType) typeAnnotation).genericType();
            val protocolTypes = ProtocolType.valueOf(typeAnnotation).protocolTypes();

            Arrays.stream(protocolType.)
                    .filter(t -> t == protocolType)
                    .findAny()
                    .orElseThrow(CodecException::new);
        }

        this.forward(context);
    }
}
