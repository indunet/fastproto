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

package org.indunet.fastproto.graph.resolve.validate;

import lombok.val;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.util.TypeUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

/**
 * Encode formula validation flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class EncodingFormulaValidator extends TypeValidator {
    @Override
    public void process(ValidatorContext context) {
        val encodeFormula = context.getEncodingFormulaClass();
        val typeAnnotation = context.getTypeAnnotation();
        val field = context.getField();

        // Validate encoder parameter type.
        if (encodeFormula != null) {
            Arrays.stream(encodeFormula.getGenericInterfaces())
                    .filter(i -> i instanceof ParameterizedType)
                    .map(i -> ((ParameterizedType) i).getActualTypeArguments())
                    .map(a -> a[0])
                    .filter(t -> {
                        if (field.getType().isPrimitive()) {
                            return t == TypeUtils.wrapperClass(field.getType().getName());
                        } else if (field.getType().isEnum()) {
                            // Enum type.
                            return ((Class<?>) t).isAssignableFrom(field.getType());
                        } else {
                            return t == field.getType();
                        }
                    }).findAny()
                    .orElseThrow(() -> new EncodingException(
                            String.format("Data type annotation and field does not match", typeAnnotation.annotationType().getName(), field.getName())));
        }

        this.forward(context);
    }
}
