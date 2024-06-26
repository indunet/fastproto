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

import lombok.val;
import org.indunet.fastproto.annotation.DataType;
import org.indunet.fastproto.graph.Reference.ReferenceType;
import org.indunet.fastproto.graph.resolve.ResolvePipeline;
import org.indunet.fastproto.mapper.CodecMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * This class is responsible for resolving references within a protocol class.
 * It creates a graph of references, where each reference can be a class or a field.
 * The graph is used to manage and manipulate the references, and is crucial for the encoding and decoding processes.
 * The class uses a ConcurrentHashMap to store the graphs for each protocol class, ensuring thread safety.
 * It also uses two ResolvePipelines to process class and field references.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class Resolver {
    protected static ConcurrentHashMap<Class<?>, Graph> graphs = new ConcurrentHashMap<>();
    protected static ResolvePipeline resolveClassFlow = ResolvePipeline.getClassPipeline();
    protected static ResolvePipeline resolveFieldFlow = ResolvePipeline.getFieldPipeline();

    private static final Predicate<Field> IS_CLASS_CONDITION = f -> CodecMapper.isSupported(f.getType());

    private static final Predicate<Field> IS_DATA_CONDITION = f -> Arrays.stream(f.getAnnotations())
            .map(Annotation::annotationType)
            .anyMatch(t -> t.isAnnotationPresent(DataType.class));

    public static Graph resolve(Class<?> protocolClass) {
        return graphs.computeIfAbsent(protocolClass, __ -> {
            val graph = new Graph();
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
                    .forEach(deque::add);

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
                                .forEach(deque::add);
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

            graph.adj.entrySet()
                    .stream()
                    .forEach(entry -> {
                        val parent = entry.getKey();
                        val children = entry.getValue();

                        if (parent.decodingIgnore) {
                            children.forEach(child -> child.decodingIgnore = true);
                        }

                        if (parent.encodingIgnore) {
                            children.forEach(child -> child.encodingIgnore = true);
                        }
                    });

            return graph;
        });
    }

    protected static boolean isClass(Field field) {
        return !IS_CLASS_CONDITION
                .or(f -> Modifier.isTransient(f.getModifiers()))
                .or(f -> f.isEnumConstant() || Enum.class.isAssignableFrom(f.getType()))
                .or(f -> EnumSet.class.isAssignableFrom(f.getType()))
                .or(f -> f.getType().isArray())
                .or(f -> f.getType() == Class.class)
                .or(f -> f.getType() == Object.class)
                .or(f -> List.class.isAssignableFrom(f.getType()))
                .or(f -> Map.class.isAssignableFrom(f.getType()))
                .or(f -> Set.class.isAssignableFrom(f.getType()))
                .test(field);
    }

    protected static boolean isData(Field field) {
        return IS_DATA_CONDITION.test(field);
    }
}
