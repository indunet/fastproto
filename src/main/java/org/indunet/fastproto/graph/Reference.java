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

package org.indunet.fastproto.graph;

import lombok.*;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Schema.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
public class Reference {
    ReferenceType referenceType;
    ByteOrder byteOrder;
    BitOrder bitOrder;

    @Builder.Default
    Boolean decodingIgnore = false;
    @Builder.Default
    Boolean encodingIgnore = false;

    Class<?> protocolClass;
    ConstructorType constructorType;

    Field field;
    Annotation dataTypeAnnotation;
    ProtocolType protocolType;

    Class<? extends Function> decodingFormulaClass;
    Class<? extends Function> encodingFormulaClass;

    Function decodingFormula;
    Function decodingLambda;
    Function encodingFormula;
    Function encodingLambda;
    Function<ByteBufferInputStream, ?> decoder;
    BiConsumer<ByteBufferOutputStream, ? super Object> encoder;

    @Builder.Default
    ThreadLocal<Object> value = new ThreadLocal<>();

    public void decode(ByteBufferInputStream inputStream) {
        val value = this.decoder.apply(inputStream);

        this.setValue(value);
    }

    public void encoder(ByteBufferOutputStream outputStream) {
        this.encoder.accept(outputStream, this.getValue().get());
    }

    public Object newInstance() {
        try {
            val object = this.protocolClass.newInstance();

            this.value.set(object);

            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DecodingException(
                    String.format("Fail decoding the class %s", this.protocolClass.getName()), e);
        }
    }

    public Object newInstance(Reference[] references) {
        val types = Arrays.stream(references)
                .filter(r -> r.getReferenceType() != ReferenceType.INVALID)
                .map(Reference::getField)
                .map(Field::getType)
                .toArray(Class<?>[]::new);

        try {
            Constructor<?> constructor = this.protocolClass.getConstructor(types);
            val args = Arrays.stream(references)
                    .filter(r -> r.getReferenceType() != ReferenceType.INVALID)
                    .map(r -> r.getValue().get())
                    .toArray();

            val object = constructor.newInstance(args);

            this.value.set(object);

            return object;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new DecodingException(
                    String.format("Fail decoding the class %s", this.protocolClass.getName()), e);
        }
    }

    public void setField(Reference reference) {
        val field = reference.getField();

        if (this.value.get() != null && reference.getValue().get() != null) {
            try {
                field.set(this.value.get(), reference.getValue().get());
            } catch (IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding the field %s of class %s", field, this.protocolClass.getName()), e);
            }
        }
    }

    public Object parse(Object object) {
        try {
            if (object == null) {
                return null;
            } else if (this.field == null) {
                return object;
            } else {
                return this.field.get(object);
            }
        } catch (IllegalAccessException e) {
            throw new EncodingException(
                    String.format("Fail decoding the field %s of class %s", this.field, this.protocolClass.getName()), e);
        }
    }

    public void clear() {
        this.value.remove();
    }

    public void setValue(Object value) {
        this.value.set(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Reference && this.referenceType == ReferenceType.CLASS && ((Reference) other).referenceType == ReferenceType.CLASS) {
            return this.protocolClass == ((Reference) other).protocolClass;
        } else {
            return super.equals(other);
        }
    }

    @Override
    public int hashCode() {
        if (this.referenceType == ReferenceType.CLASS) {
            return this.protocolClass.hashCode();
        } else {
            return super.hashCode();
        }
    }

    public enum ReferenceType {
        CLASS,
        FIELD,
        INVALID
    }

    public enum ConstructorType {
        NO_ARGS,
        ALL_ARGS
    }
}
