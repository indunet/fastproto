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
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.graph.resolve.validate.TypeValidator;
import org.indunet.fastproto.graph.resolve.validate.ValidatorContext;
import org.indunet.fastproto.mapper.CodecMapper;
import org.indunet.fastproto.mapper.JavaTypeMapper;

import java.text.MessageFormat;
import java.util.function.Function;

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
                .defaultByteOrder(reference.getByteOrder())
                .defaultBitOrder(reference.getBitOrder())
                .build();

        if (reference.getDecodingFormulaClass() != null) {
            Function decoder = CodecMapper.getDecoder(context, reference.getDecodingFormulaClass());

            reference.setDecoder(decoder);
        } else if (reference.getDecodingLambda() != null) {
            val javaType = JavaTypeMapper.get(reference.getDataTypeAnnotation().annotationType());
            val decoder = CodecMapper.getDefaultDecoder(context, javaType);
            val func = reference.getDecodingLambda();

            reference.setDecoder(decoder.andThen(func));
        } else {
            Function decoder = CodecMapper.getDecoder(context, null);

            reference.setDecoder(decoder);
        }

        if (reference.getEncodingFormulaClass() != null) {
            val encoder = CodecMapper.getEncoder(context, reference.getEncodingFormulaClass());

            reference.setEncoder(encoder);
        } else if (reference.getEncodingLambda() != null) {
            val javaType = JavaTypeMapper.get(reference.getDataTypeAnnotation().annotationType());
            val encoder = CodecMapper.getDefaultEncoder(context, javaType);
            val func = reference.getEncodingLambda();

            reference.setEncoder((outputStream, value) -> encoder.accept(outputStream, func.apply(value)));
        } else {
            val encoder = CodecMapper.getEncoder(context, null);

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
