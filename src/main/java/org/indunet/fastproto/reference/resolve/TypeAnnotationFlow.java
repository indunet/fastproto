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

package org.indunet.fastproto.reference.resolve;

import lombok.val;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.reference.Reference;
import org.indunet.fastproto.util.TypeUtils;

import java.util.Arrays;

/**
 * Resolve element type flow.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class TypeAnnotationFlow extends ResolvePipeline {
    @Override
    public void process(Reference reference) {
        val field = reference.getField();

        val typeAnnotation = Arrays.stream(field.getAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(TypeFlag.class))
                .findAny()
                .orElseThrow(ResolveException::new);

        reference.setTypeAnnotation(typeAnnotation);
        reference.setReferenceType(Reference.ReferenceType.FIELD);

        reference.setDecoderClass(TypeUtils.decoderClass(typeAnnotation));
        reference.setEncoderClass(TypeUtils.encoderClass(typeAnnotation));
        reference.setDecodeFormula(TypeUtils.decodingFormula(typeAnnotation));
        reference.setEncodeFormula(TypeUtils.encodingFormula(typeAnnotation));

        reference.setProtocolType(ProtocolType.proxy(typeAnnotation));

        this.forward(reference);
    }
}
