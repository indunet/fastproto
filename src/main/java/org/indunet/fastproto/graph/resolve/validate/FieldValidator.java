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
import org.indunet.fastproto.annotation.DecodingFormula;
import org.indunet.fastproto.annotation.EncodingFormula;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeFormulaException;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.function.Function;

/**
 * Abstract flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class FieldValidator extends TypeValidator {
    @Override
    public void process(ValidatorContext context) {
        val protocolType = context.getProtocolType();
        val typeAnnotationClass = context.getTypeAnnotationClass();
        val field = context.getField();
        Class<? extends Function> decodingFormula = Optional.ofNullable(field.getAnnotation(DecodingFormula.class))
                .map(DecodingFormula::value)
                .orElse(null);
        Class<? extends Function> encodingFormula = Optional.ofNullable(field.getAnnotation(EncodingFormula.class))
                .map(EncodingFormula::value)
                .orElse(null);

        try {
            context.setDecodingFormula(decodingFormula);
            context.setEncodingFormula(encodingFormula);
        } catch (Exception e) {
            throw new DecodeFormulaException(
                    MessageFormat.format(
                            CodecError.FAIL_GETTING_DECODE_FORMULA.getMessage(),
                            protocolType.getType().getName(), field.getName()), e);
        }

//        if (decodingFormula == null && encodingFormula == null) {
//            Arrays.stream(protocolType.javaTypes())
//                    .filter(t -> t == field.getType()
//                                || (field.getType().isEnum() && (((Class<?>) t).isAssignableFrom(field.getType()))))
//                    .findAny()
//                    .orElseThrow(() -> new CodecException(MessageFormat.format(
//                            CodecError.ANNOTATION_FIELD_NOT_MATCH.getMessage(), protocolType.getType().getSimpleName(), field.toString())));  // Field name with class name.
//        }

        this.forward(context);
    }
}