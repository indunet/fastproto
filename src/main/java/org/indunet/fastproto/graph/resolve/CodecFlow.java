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

package org.indunet.fastproto.graph.resolve;

import lombok.val;
import org.indunet.fastproto.annotation.Validator;
import org.indunet.fastproto.codec.CodecContext;
import org.indunet.fastproto.codec.CodecMapper;
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.graph.resolve.validate.TypeValidator;
import org.indunet.fastproto.graph.resolve.validate.ValidatorContext;

import java.text.MessageFormat;

/**
 * Resolve decoder and encoder flow.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class CodecFlow extends ResolvePipeline {
    @Override
    public void process(Reference reference) {
        val context = CodecContext.builder()
                .dataTypeAnnotation(reference.getDataTypeAnnotation())
                .fieldType(reference.getField().getType())
                .field(reference.getField())
                .defaultEndianPolicy(reference.getEndianPolicy())
                .build();

        val decoder = CodecMapper
                .getDecoder(context, reference.getDecodingFormulaClass());

        if (reference.getDecodingFormula() != null) {
            val func = reference.getDecodingFormula();

            reference.setDecoder(decoder.andThen(func));
        } else {
            reference.setDecoder(decoder);
        }

        val encoder = CodecMapper
                .getEncoder(context, reference.getEncodingFormulaClass());

        if (reference.getEncodingFormula() != null) {
            val func = reference.getEncodingFormula();

            reference.setEncoder((byte[] bytes, Object value) -> encoder.accept(bytes, func.apply(value)));
        } else {
            reference.setEncoder(encoder);
        }

        try {
            val ctx = ValidatorContext.builder()
                    .field(reference.getField())
                    .typeAnnotation(reference.getDataTypeAnnotation())
                    .protocolType(reference.getProtocolType())
                    .decodingFormulaClass(reference.getDecodingFormulaClass())
                    .encodingFormulaClass(reference.getEncodingFormulaClass())
                    .build();
            val validator = reference.getDataTypeAnnotation()
                    .annotationType()
                    .getAnnotation(Validator.class);

            TypeValidator.create(validator.value())
                    .process(ctx);
        } catch (ResolveException e) {
            throw new ResolveException(MessageFormat.format(
                    "Fail resolving the filed of %s", reference.getField().toString()
            ), e);
        }

        this.forward(reference);
    }
}
