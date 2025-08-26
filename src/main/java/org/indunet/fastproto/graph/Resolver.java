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
import org.indunet.fastproto.annotation.LengthRef;
import org.indunet.fastproto.annotation.AutoType;
import org.indunet.fastproto.exception.ResolvingException;
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

            // Bind dynamic length supplier for fields with dynamic length configuration
            val refs = graph.getValidReferences();
            for (val r : refs) {
                Annotation typeAnn = r.getDataTypeAnnotation();
                if (typeAnn == null) continue;

                // detect whether annotation has length()
                boolean hasLength = Arrays.stream(typeAnn.annotationType().getMethods())
                        .anyMatch(m -> m.getName().equals("length") && m.getParameterCount() == 0);
                if (!hasLength) {
                    // also process legacy @LengthRef only when target supports length
                    continue;
                }

                // 1) Prefer annotation-level lengthRef if present
                String annLengthRef = null;
                boolean annUseSelf = false;
                int min = Integer.MIN_VALUE, max = Integer.MAX_VALUE;
                try {
                    // optional methods
                    java.lang.reflect.Method mRef = null;
                    try { mRef = typeAnn.annotationType().getMethod("lengthRef"); } catch (NoSuchMethodException ignore) {}
                    if (mRef != null) {
                        annLengthRef = (String) mRef.invoke(typeAnn);
                        if (annLengthRef != null && annLengthRef.startsWith("$")) {
                            annLengthRef = annLengthRef.substring(1);
                        }
                    }
                    java.lang.reflect.Method mSelf = null;
                    try { mSelf = typeAnn.annotationType().getMethod("useSelfOnEncode"); } catch (NoSuchMethodException ignore) {}
                    if (mSelf != null) {
                        annUseSelf = (Boolean) mSelf.invoke(typeAnn);
                    }
                    java.lang.reflect.Method mMin = null, mMax = null;
                    try { mMin = typeAnn.annotationType().getMethod("min"); } catch (NoSuchMethodException ignore) {}
                    try { mMax = typeAnn.annotationType().getMethod("max"); } catch (NoSuchMethodException ignore) {}
                    if (mMin != null) { min = (Integer) mMin.invoke(typeAnn); }
                    if (mMax != null) { max = (Integer) mMax.invoke(typeAnn); }
                } catch (Exception e) {
                    // ignore, use defaults
                }

                String refNameTmp = annLengthRef;
                LengthRef fieldLenRef = r.getField().getAnnotation(LengthRef.class);
                boolean useFieldLenRef = (refNameTmp == null || refNameTmp.isEmpty()) && fieldLenRef != null;
                if (useFieldLenRef) {
                    refNameTmp = fieldLenRef.value();
                    // override min/max from field-level when annotation-level absent
                    min = fieldLenRef.min();
                    max = fieldLenRef.max();
                }
                final String refName = refNameTmp;

                // if refName still empty, we may rely on static length from annotation. Validate zero-length rule on decode path later.
                if (refName != null && !refName.isEmpty()) {
                    // Find source reference by field name within the same declaring class
                    val src = refs.stream()
                            .filter(x -> x.getField() != null)
                            .filter(x -> x.getField().getDeclaringClass() == r.getField().getDeclaringClass())
                            .filter(x -> x.getField().getName().equals(refName))
                            .findFirst()
                            .orElseThrow(() -> new ResolvingException(String.format("@LengthRef source '%s' not found for %s", refName, r.getField())));

                    // Validate order: source must appear before target
                    if (refs.indexOf(src) >= refs.indexOf(r)) {
                        throw new ResolvingException(String.format("@LengthRef source '%s' must be declared before %s", refName, r.getField()));
                    }

                    final int minF = min;
                    final int maxF = max;
                    final val srcRef = src;
                    r.setLengthSupplier(() -> {
                        int len;
                        Object v = srcRef.getValue().get();
                        if (v == null) {
                            len = 0;
                        } else if (v instanceof Number) {
                            len = ((Number) v).intValue();
                        } else {
                            throw new IllegalArgumentException("@LengthRef source is not a Number");
                        }
                        if (len < minF || len > maxF) {
                            throw new IllegalArgumentException("@LengthRef length out of bounds");
                        }
                        return len;
                    });
                } else {
                    // No ref: ensure length() is not 0 for decoding; enforcement will be done in CodecFlow via decode supplier
                    // Here we do nothing; CodecFlow will read original length().
                }
            }

            // Preserve legacy behavior: for fields annotated with @AutoType that target a type with length(),
            // force-evaluate length() now to surface missing length immediately as ResolvingException.
            for (val r : refs) {
                if (r.getField().isAnnotationPresent(AutoType.class)) {
                    boolean hasLength = Arrays.stream(r.getDataTypeAnnotation().annotationType().getMethods())
                            .anyMatch(m -> m.getName().equals("length") && m.getParameterCount() == 0);
                    if (hasLength) {
                        try {
                            r.getDataTypeAnnotation().annotationType().getMethod("length").invoke(r.getDataTypeAnnotation());
                        } catch (RuntimeException e) {
                            throw e;
                        } catch (Exception e) {
                            throw new ResolvingException("Failed resolving @AutoType length", e);
                        }
                    }
                }
            }

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
