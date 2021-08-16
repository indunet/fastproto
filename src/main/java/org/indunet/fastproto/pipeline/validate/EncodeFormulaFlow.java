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

package org.indunet.fastproto.pipeline.validate;

import lombok.val;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.FlowCode;
import org.indunet.fastproto.pipeline.ValidationContext;

import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Encode formula validation flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class EncodeFormulaFlow extends AbstractFlow<ValidationContext> {
    @Override
    public void process(ValidationContext context) {
        val encodeFormula = context.getEncodeFormula();
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
                            return t == ProtocolType.wrapperClass(field.getType().getName());
                        } else if (field.getType().isEnum()) {
                            // Enum type.
                            return ((Class<?>) t).isAssignableFrom(field.getType());
                        } else {
                            return t == field.getType();
                        }
                    }).findAny()
                    .orElseThrow(() -> new EncodeException(MessageFormat.format(
                            CodecError.ANNOTATION_FIELD_NOT_MATCH.getMessage(), typeAnnotation.annotationType().getName(), field.getName())));
        }

        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return FlowCode.ENCODE_FORMULA_FLOW_CODE;
    }
}
