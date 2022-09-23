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

package org.indunet.fastproto.reference.resolve.validate;

import lombok.val;
import org.indunet.fastproto.annotation.type.ArrayType;
import org.indunet.fastproto.exception.ResolveException;

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

        if (protocolType instanceof ArrayType) {
            val genericType = ((ArrayType) protocolType).genericType();

            Arrays.stream(ArrayType.ALLOWED_GENERIC_TYPES)
                    .filter(t -> t == genericType)
                    .findAny()
                    .orElseThrow(() -> new ResolveException("Illegal generic type for array"));
        }

        this.forward(context);
    }
}
