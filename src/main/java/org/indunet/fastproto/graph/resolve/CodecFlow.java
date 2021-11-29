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
import org.indunet.fastproto.decoder.TypeDecoder;
import org.indunet.fastproto.encoder.TypeEncoder;
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.graph.validate.TypeValidator;
import org.indunet.fastproto.graph.validate.ValidatorContext;
import org.indunet.fastproto.util.TypeUtils;

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
        val typeAnnotation = reference.getTypeAnnotation();
        Class<? extends TypeDecoder> decoderClass = TypeUtils.decoderClass(typeAnnotation);
        Class<? extends TypeEncoder> encoderClass = TypeUtils.encoderClass(typeAnnotation);
        Class<? extends Function> afterDecode = TypeUtils.decodingFormula(typeAnnotation);
        Class<? extends Function> beforeEncode = TypeUtils.encodingFormula(typeAnnotation);

        reference.setDecoderClass(decoderClass);
        reference.setEncoderClass(encoderClass);
        reference.setDecodeFormula(afterDecode);
        reference.setEncodeFormula(beforeEncode);

        val field = reference.getField();
        val context = ValidatorContext.builder()
                .field(field)
                .typeAnnotation(typeAnnotation)
                .protocolType(reference.getProtocolType())
                .build();

        try {
            val validator = typeAnnotation.annotationType().getAnnotation(Validator.class);

            TypeValidator.create(validator.value())
                    .process(context);
        } catch (ResolveException e) {
            throw new ResolveException(MessageFormat.format(
                    "Fail resolving the filed of %s", field.toString()
            ), e);
        }

        this.forward(reference);
    }
}
