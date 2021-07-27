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
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.DecodeFormulaException;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.ValidationContext;
import org.indunet.fastproto.util.TypeUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Abstract flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class FieldFlow extends AbstractFlow<ValidationContext> {
    public static int FIELD_FLOW = 0x01;

    @Override
    public void process(ValidationContext context) {
        val typeAnnotation = context.getTypeAnnotation();
        val typeAnnotationClass = context.getTypeAnnotationClass();
        val field = context.getField();
        Class<? extends Function> decodeFormula;
        Class<? extends Function> encodeFormula;

        try {
            decodeFormula = TypeUtils.decodeFormula(typeAnnotation);
            encodeFormula = TypeUtils.encodeFormula(typeAnnotation);

            context.setDecodeFormula(decodeFormula);
            context.setEncodeFormula(encodeFormula);
        } catch (Exception e) {
            throw new DecodeFormulaException(
                    MessageFormat.format(
                            CodecError.FAIL_GETTING_DECODE_FORMULA.getMessage(),
                            typeAnnotation.annotationType().getName(), field.getName()), e);
        }

        if (decodeFormula == null && encodeFormula == null) {
            Arrays.stream(TypeUtils.javaTypes(typeAnnotation))
                    .filter(t -> t == field.getType()
                                || (field.getType().isEnum() && (((Class<?>) t).isAssignableFrom(field.getType()))))
                    .findAny()
                    .orElseThrow(() -> new CodecException(MessageFormat.format(
                            CodecError.ANNOTATION_FIELD_NOT_MATCH.getMessage(), typeAnnotation.annotationType().getName(), field.getName())));
        }

        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return FIELD_FLOW;
    }
}
