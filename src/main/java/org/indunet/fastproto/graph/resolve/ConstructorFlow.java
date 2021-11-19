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
import org.indunet.fastproto.annotation.Constructor;
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.graph.AbstractFlow;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.graph.Reference.ConstructorType;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Resolve Constructor type flow.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
@Rule(name = "constructor")
public class ConstructorFlow extends AbstractFlow<Reference> {
    @Condition
    public boolean evaluate(Facts facts) {
        return true;
    }

    @Action
    @Override
    public void process(@Fact("reference") Reference reference) {
        val protocolClass = reference.getProtocolClass();
        var cnt = 0;

        if (Arrays.stream(protocolClass.getConstructors())
                .anyMatch(c -> c.isAnnotationPresent(Constructor.class))) {
            cnt = Arrays.stream(protocolClass.getConstructors())
                    .filter(c -> c.isAnnotationPresent(Constructor.class))
                    .findAny()
                    .get()
                    .getParameterCount();
        } else {
            cnt = Arrays.stream(protocolClass.getConstructors())
                    .mapToInt(c -> c.getParameterCount())
                    .min()
                    .getAsInt();
        }

        if (cnt == 0) {
            reference.setConstructorType(ConstructorType.NO_ARGS);
        } else {
            // Filter transient fields, jacoco would add it during test.
            val paramTypes = Arrays.stream(protocolClass.getDeclaredFields())
                    .filter(f -> !Modifier.isTransient(f.getModifiers()))
                    .map(Field::getType)
                    .toArray(Class<?>[]::new);

            try {
                protocolClass.getConstructor(paramTypes);
                reference.setConstructorType(ConstructorType.ALL_ARGS);
            } catch (NoSuchMethodException e) {
                throw new ResolveException(String.format(
                        "The type of constructor parameters and class fields does not match.",
                        protocolClass.getName()), e);
            }
        }

        this.nextFlow(reference);
    }

    @Override
    public long getFlowCode() {
        return 0;
    }
}
