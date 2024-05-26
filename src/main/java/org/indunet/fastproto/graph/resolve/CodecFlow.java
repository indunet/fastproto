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

import org.indunet.fastproto.annotation.Validator;
import org.indunet.fastproto.codec.CodecContext;
import org.indunet.fastproto.exception.ResolvingException;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.graph.resolve.validate.TypeValidator;
import org.indunet.fastproto.graph.resolve.validate.ValidatorContext;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.mapper.CodecMapper;
import org.indunet.fastproto.mapper.JavaTypeMapper;

import java.text.MessageFormat;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * This class is responsible for resolving the decoder and encoder flow.
 * It creates a CodecContext and then parses out the decoder and encoder based on this context.
 * Finally, it validates whether the parsed references are valid.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class CodecFlow extends ResolvePipeline {

    @Override
    public void process(Reference reference) {
        CodecContext ctx = CodecContext.builder()
                .dataTypeAnnotation(reference.getDataTypeAnnotation())
                .fieldType(reference.getField().getType())
                .field(reference.getField())
                .defaultByteOrder(reference.getByteOrder())
                .defaultBitOrder(reference.getBitOrder())
                .build();

        reference.setDecoder(resolveDecoder(reference, ctx));
        reference.setEncoder(resolveEncoder(reference, ctx));

        validateReference(reference);

        this.forward(reference);
    }

    // Resolve decoder.
    protected Function<ByteBufferInputStream, ?> resolveDecoder(Reference reference, CodecContext context) {
        if (reference.getDecodingFormulaClass() != null) {
            return CodecMapper.getDecoder(context, reference.getDecodingFormulaClass());
        } else if (reference.getDecodingLambda() != null) {
            Class<?> javaType = JavaTypeMapper.get(reference.getDataTypeAnnotation().annotationType());
            Function<ByteBufferInputStream, ?> decoder = CodecMapper.getDefaultDecoder(context, javaType);

            return decoder.andThen(reference.getDecodingLambda());
        } else {
            return CodecMapper.getDecoder(context, null);
        }
    }

    // Resolve encoder.
    protected BiConsumer<ByteBufferOutputStream, Object> resolveEncoder(Reference reference, CodecContext context) {
        if (reference.getEncodingFormulaClass() != null) {
            return CodecMapper.getEncoder(context, reference.getEncodingFormulaClass());
        } else if (reference.getEncodingLambda() != null) {
            Class<?> javaType = JavaTypeMapper.get(reference.getDataTypeAnnotation().annotationType());
            BiConsumer<ByteBufferOutputStream, Object> encoder = CodecMapper.getDefaultEncoder(context, javaType);
            Function<Object, Object> func = reference.getEncodingLambda();

            return (outputStream, value) -> encoder.accept(outputStream, func.apply(value));
        } else {
            return CodecMapper.getEncoder(context, null);
        }
    }

    // Validate reference.
    protected void validateReference(Reference reference) {
        try {
            ValidatorContext ctx = ValidatorContext.builder()
                    .field(reference.getField())
                    .typeAnnotation(reference.getDataTypeAnnotation())
                    .protocolType(reference.getProtocolType())
                    .decodingFormulaClass(reference.getDecodingFormulaClass())
                    .encodingFormulaClass(reference.getEncodingFormulaClass())
                    .build();
            Validator validator = reference.getDataTypeAnnotation()
                    .annotationType()
                    .getAnnotation(Validator.class);

            TypeValidator.create(validator.value()).process(ctx);
        } catch (ResolvingException e) {
            throw new ResolvingException(MessageFormat.format(
                    "Failed resolving the field of %s", reference.getField().toString()
            ), e);
        }
    }
}
