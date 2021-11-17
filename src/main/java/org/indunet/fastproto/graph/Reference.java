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
import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.annotation.*;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.TypeDecoder;
import org.indunet.fastproto.encoder.TypeEncoder;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.exception.EncodingException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    EndianPolicy endianPolicy;

    @Builder.Default
    Boolean decodingIgnore = false;
    @Builder.Default
    Boolean encodingIgnore = false;

    Class<?> protocolClass;
    ConstructorType constructorType;
    EnableCompress enableCompress;
    EnableProtocolVersion enableProtocolVersion;
    EnableChecksum enableChecksum;
    EnableCrypto enableCrypto;
    EnableFixedLength enableFixedLength;

    Field field;
    Annotation typeAnnotation;
    Class<? extends TypeDecoder> decoderClass;
    Class<? extends TypeEncoder> encoderClass;
    Class<? extends Function> decodeFormula;
    Class<? extends Function> encodeFormula;
    Function<DecodeContext, ?> decoder;
    Consumer<?> encoder;

    Integer byteOffset;
    Integer bitOffset;
    Integer size;
    Integer length;

    @Builder.Default
    ThreadLocal<Object> value = new ThreadLocal<>();

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
                .map(Reference::getField)
                .map(Field::getType)
                .collect(Collectors.toList())
                .toArray(new Class[references.length]);

        try {
            Constructor<?> constructor = this.protocolClass.getConstructor(types);
            val args = Arrays.stream(references)
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

    public void setField(@NonNull Reference reference) {
        val field = reference.getField();

        if (this.value.get() != null) {
            try {
                field.set(this.value.get(), reference.getValue().get());
            } catch (IllegalAccessException e) {
                throw new DecodingException(
                        String.format("Fail decoding the field %s of class %s", field.toString(), this.protocolClass.getName()), e);
            }
        }
//        else {
//            throw new DecodingException(
//                    String.format("Fail decoding the class %s", this.protocolClass.getName()));
//        }
    }

    public Object getValue(Object object) {
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
                    String.format("Fail decoding the field %s of class %s", this.field.toString(), this.protocolClass.getName()), e);
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
        INVALID;
    }

    public enum ConstructorType {
        NO_ARGS,
        ALL_ARGS;
    }
}
