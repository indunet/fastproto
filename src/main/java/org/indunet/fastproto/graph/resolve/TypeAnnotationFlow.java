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
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.AutoType;
import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.exception.ResolvingException;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.mapper.AnnotationMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * TypeAnnotationFlow Class.
 * This class is responsible for resolving the data type annotation in the context.
 * It checks the protocol field for the DataType annotation and sets the data type annotation in the reference accordingly.
 * If the annotation is AutoType, it will automatically determine the appropriate data type annotation.
 * This class extends the ResolvePipeline class and overrides the process method to implement its functionality.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class TypeAnnotationFlow extends ResolvePipeline {
    @Override
    public void process(Reference reference) {
        val field = reference.getField();
        val typeAnnotation = findDataTypeAnnotation(field);

        if (typeAnnotation instanceof AutoType) {
            setAutoTypeAnnotation(reference, (AutoType) typeAnnotation);
        } else {
            setDataTypeAnnotation(reference, typeAnnotation);
        }

        reference.setReferenceType(Reference.ReferenceType.FIELD);

        this.forward(reference);
    }

    private Annotation findDataTypeAnnotation(Field field) {
        return Arrays.stream(field.getAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(DataType.class))
                .findAny()
                .orElseThrow(ResolvingException::new);
    }

    private void setAutoTypeAnnotation(Reference reference, AutoType autoType) {
        Class<? extends Annotation> type = AnnotationMapper.get(reference.getField().getGenericType());
        Annotation proxy = ProtocolType.proxy(autoType, type);

        reference.setDataTypeAnnotation(proxy);
        reference.setProtocolType(ProtocolType.proxy(proxy));
    }

    private void setDataTypeAnnotation(Reference reference, Annotation typeAnnotation) {
        reference.setDataTypeAnnotation(typeAnnotation);
        reference.setProtocolType(ProtocolType.proxy(typeAnnotation));
    }
}
