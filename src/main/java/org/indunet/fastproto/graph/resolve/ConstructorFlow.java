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
import lombok.var;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.exception.ResolvingException;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.graph.Reference.ConstructorType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * ConstructorFlow Class.
 * This class is responsible for resolving the constructor type in the context.
 * It checks the protocol class for the constructors and sets the constructor type in the reference accordingly.
 * This class extends the ResolvePipeline class and overrides the process method to implement its functionality.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class ConstructorFlow extends ResolvePipeline {
    @Override
    public void process(Reference reference) {
        val protocolClass = reference.getProtocolClass();
        var cnt = getMinConstructorParameterCount(protocolClass);

        if (cnt == 0) {
            reference.setConstructorType(ConstructorType.NO_ARGS);
        } else {
            setAllArgsConstructorType(reference, protocolClass);
        }

        this.forward(reference);
    }

    protected int getMinConstructorParameterCount(Class<?> protocolClass) {
        return Arrays.stream(protocolClass.getConstructors())
                .mapToInt(java.lang.reflect.Constructor::getParameterCount)
                .min()
                .orElseThrow(() -> new CodecException(
                        String.format("Fail getting constructor parameters of %s", protocolClass.getName())));
    }

    protected void setAllArgsConstructorType(Reference reference, Class<?> protocolClass) {
        // Filter transient fields, jacoco would add it during test.
        val paramTypes = Arrays.stream(protocolClass.getDeclaredFields())
                .filter(f -> !Modifier.isTransient(f.getModifiers()))
                .map(Field::getType)
                .toArray(Class<?>[]::new);

        try {
            protocolClass.getConstructor(paramTypes);

            reference.setConstructorType(ConstructorType.ALL_ARGS);
        } catch (NoSuchMethodException e) {
            throw new ResolvingException(String.format(
                    "The constructor parameters of %s and class fields does not match.",
                    protocolClass.getName()), e);
        }
    }
}
