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

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.graph.Reference.ReferenceType;
import org.indunet.fastproto.graph.resolve.ResolvePipeline;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Reference resolver.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class ReferenceResolver {
    protected static ConcurrentHashMap<Class<?>, ReferenceGraph> graphs = new ConcurrentHashMap<>();
    protected static ResolvePipeline resolveClassFlow = ResolvePipeline.getClassPipeline();
    protected static ResolvePipeline resolveFieldFlow = ResolvePipeline.getFieldPipeline();

    public static ReferenceGraph resolve(@NonNull Class<?> protocolClass) {
        return graphs.computeIfAbsent(protocolClass, __ -> {
            val graph = new ReferenceGraph();
            val deque = new ArrayDeque<Field>();

            // Root.
            val reference = Reference.builder()
                    .protocolClass(protocolClass)
                    .referenceType(ReferenceType.CLASS)
                    .build();
            resolveClassFlow.process(reference);
            graph.addClass(reference);

            Arrays.stream(protocolClass.getDeclaredFields())
                    .peek(f -> f.setAccessible(true))
                    .forEach(f -> deque.add(f));

            // BFS
            while (!deque.isEmpty()) {
                val field = deque.remove();

                if (isData(field)) {
                    val r = Reference.builder()
                            .field(field)
                            .referenceType(Reference.ReferenceType.FIELD)
                            .build();

                    resolveFieldFlow.process(r);
                    graph.addReference(r);
                } else if (isClass(field)) {
                    if (graph.contains(field.getType())) {
                        val ref = graph.getReference(field.getType())
                                .withField(field);

                        graph.addReference(ref);
                    } else {
                        val s = Reference.builder()
                                .protocolClass(field.getType())
                                .field(field)
                                .referenceType(Reference.ReferenceType.CLASS)
                                .build();

                        resolveClassFlow.process(s);
                        graph.addClass(s);
                        graph.addReference(s);
                        Arrays.stream(field.getType().getDeclaredFields())
                                .peek(f -> f.setAccessible(true))
                                .forEach(f -> deque.add(f));
                    }
                } else {
                    // Invalid field.
                    val s = Reference.builder()
                            .field(field)
                            .referenceType(Reference.ReferenceType.INVALID)
                            .build();

                    graph.addReference(s);
                }
            }

            return graph;
        });
    }

    protected static boolean isClass(@NonNull Field field) {
        Predicate<Field> isProtocolType = f -> ProtocolType.isSupported(f.getType());
        Predicate<Field> isTransient = f -> Modifier.isTransient(f.getModifiers());
        Predicate<Field> isEnum = f -> f.isEnumConstant()
                || Enum.class.isAssignableFrom(f.getType());
        Predicate<Field> isEnumSet = f -> EnumSet.class.isAssignableFrom(f.getType());
        Predicate<Field> isArray = f -> f.getType().isArray();
        Predicate<Field> isClass = f -> f.getType() == Class.class;
        Predicate<Field> isObject = f -> f.getType() == Object.class;
        Predicate<Field> isList = f -> List.class.isAssignableFrom(f.getType());
        Predicate<Field> isMap = f -> Map.class.isAssignableFrom(f.getType());
        Predicate<Field> isSet = f -> Set.class.isAssignableFrom(f.getType());

        return !isProtocolType.or(isTransient)
                .or(isEnum)
                .or(isEnumSet)
                .or(isArray)
                .or(isClass)
                .or(isObject)
                .or(isList)
                .or(isMap)
                .or(isSet)
                .test(field);
    }

    protected static boolean isData(@NonNull Field field) {
        Predicate<Field> isTypeFlag = f -> Arrays.stream(f.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(t -> t.isAnnotationPresent(DataType.class));

        return isTypeFlag.test(field);
    }
}
