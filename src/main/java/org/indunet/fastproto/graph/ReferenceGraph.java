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
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.encoder.EncodeContext;
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.graph.Reference.ConstructorType;
import org.indunet.fastproto.graph.Reference.ReferenceType;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Reference Graph.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class ReferenceGraph {
    protected LinkedHashMap<Reference, ArrayList<Reference>> adj = new LinkedHashMap<>();

    protected ReferenceGraph() {

    }

    public int countClass() {
        return adj.size();
    }

    public int countReference() {
        return this.adj.values().stream()
                .mapToInt(List::size)
                .sum();
    }

    public LinkedHashMap<Reference, ArrayList<Reference>> getAdj() {
        return this.adj;
    }

    public boolean contains(@NonNull Class<?> protocolClass) {
        return this.adj.keySet().stream()
                .anyMatch(s -> s.getProtocolClass() == protocolClass);
    }

    public boolean contains(@NonNull Reference reference) {
        return this.adj.keySet().stream()
                .anyMatch(s -> s.equals(reference));
    }

    public void addClass(@NonNull Reference reference) {
        if (!this.contains(reference)) {
            this.adj.put(reference, new ArrayList<>());
        }
    }

    public void addReference(@NonNull Reference parent, @NonNull Reference child) {
        if (this.contains(parent)) {
            adj.get(parent).add(child);
        } else {
            adj.put(parent, new ArrayList<Reference>());
            adj.get(parent).add(child);
        }
    }

    public void addReference(@NonNull Reference reference) {
        val key = this.adj.keySet().stream()
                .filter(s -> s.getProtocolClass()
                        == reference.getField().getDeclaringClass())
                .findAny()
                .orElseThrow(ResolveException::new);

        this.addReference(key, reference);
    }

    public Reference getSchema(@NonNull Class<?> protocolCLass) {
        return this.adj.keySet().stream()
                .filter(s -> s.getProtocolClass() == protocolCLass)
                .findAny()
                .orElse(null);
    }

    public List<Reference> adj(@NonNull Reference reference) {
        return this.adj.get(reference);
    }

    public int degree(@NonNull Reference schema) {
        if (this.adj.containsKey(schema)) {
            return this.adj
                    .get(schema)
                    .size();
        } else {
            return 0;
        }
    }

    public Reference root() {
        return this.adj.keySet()
                .stream()
                .findFirst()
                .get();
    }

    public void print() {
        this.adj.entrySet()
                .forEach(entry -> {
                    System.out.println(entry.getKey());

                    entry.getValue()
                            .forEach(s -> System.out.println("\t" + s));
                });
    }

    public List<DecodeContext> decodeContexts(@NonNull byte[] datagram) {
        return this.adj.values().stream()
                .flatMap(Collection::stream)
                .filter(r -> !r.decodeIgnore)
                .filter(r -> r.referenceType == Reference.ReferenceType.FIELD)
                .map(r -> DecodeContext.builder()
                        .datagram(datagram)
                        .reference(r)
                        .build())
                .collect(Collectors.toList());
    }

    public Object generate() {
        val root = this.root();
        this.generate(root);

        val object = root.getValue().get();
        this.foreach(r -> r.clear());

        return object;
    }

    public void foreach(Consumer<Reference> consumer) {
        val deque = new ArrayDeque<Reference>();
        this.adj.get(root())
                .forEach(r -> deque.add(r));

        // BFS
        while (!deque.isEmpty()) {
            val reference = deque.remove();

            consumer.accept(reference);
        }
    }

    protected Object generate(Reference reference) {
        val list = new ArrayList<Reference>();

        if (reference.getConstructorType() == ConstructorType.NO_ARGS) {

            reference.newInstance();

            list.stream()
                    .filter(r -> r.getReferenceType() != ReferenceType.INVALID)
                    .forEach(r -> {
                        if (r.getReferenceType() == ReferenceType.CLASS) {
                            this.generate(r);
                        }

                        reference.setField(r);
                    });
        } else if (reference.getConstructorType() == ConstructorType.ALL_ARGS) {
            list.stream()
                    .filter(r -> r.getReferenceType() == ReferenceType.CLASS)
                    .forEach(this::generate);

            reference.newInstance(list.toArray(new Reference[list.size()]));
        }

        return reference.getValue().get();
    }

    public List<EncodeContext> encodeContexts(@NonNull Object object, @NonNull byte[] datagram) {
        List<Reference> references = this.getAdj().get(this.root());

        val list = references.stream()
                .filter(r -> r.getReferenceType() == ReferenceType.FIELD);

        for (val reference: references) {
            if (reference.getReferenceType() == ReferenceType.FIELD) {
                val value = reference.getValue(object);
            } else if (reference.getReferenceType() == ReferenceType.CLASS) {

            }
        }
    }

    public List<EncodeContext> encodeContexts(@NonNull Object object, @NonNull byte[] datagram) {

    }
}
