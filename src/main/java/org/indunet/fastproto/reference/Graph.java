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

package org.indunet.fastproto.reference;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.reference.Reference.ConstructorType;
import org.indunet.fastproto.reference.Reference.ReferenceType;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reference Graph.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class Graph {
    protected LinkedHashMap<Reference, ArrayList<Reference>> adj = new LinkedHashMap<>();

    protected Graph() {

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
//        return this.adj.keySet().stream()
//                .anyMatch(s -> s.equals(reference));
        return this.adj.keySet().contains(reference);
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

    public Reference getReference(@NonNull Class<?> protocolCLass) {
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

    public List<Reference> decodeReferences() {
        return this.adj.values().stream()
                .flatMap(Collection::stream)
                .filter(r -> r.referenceType == Reference.ReferenceType.FIELD)
                .filter(r -> !r.decodingIgnore)
                .collect(Collectors.toList());
    }

    public Stream<Reference> stream() {
        val list = new ArrayList<Reference>();

        this.foreach(r -> list.add(r));

        return list.stream();
    }

    public void foreach(Consumer<Reference> consumer) {
        val queue = new ArrayDeque<Reference>();
        val marks = new HashSet<Reference>();

        queue.add(this.root());
        marks.add(this.root());

        while (!queue.isEmpty()) {
            val ref = queue.remove();

            consumer.accept(ref);

            this.adj(ref).stream()
                    .filter(r -> r.getReferenceType() == ReferenceType.CLASS)
                    .filter(r -> !marks.contains(r))
                    .forEach(r -> {
                        queue.add(r);
                        marks.add(r);
                    });

            this.adj(ref).stream()
                    .filter(r -> r.getReferenceType() == ReferenceType.FIELD)
                    .forEach(consumer::accept);
        }
    }

    public Object generate() {
        val root = this.root();
        this.generate(root);

        val object = root.getValue().get();
        this.foreach(r -> r.clear());

        return object;
    }

    protected void generate(Reference reference) {
        val list = this.adj(reference);

        if (reference.getConstructorType() == ConstructorType.NO_ARGS) {

            reference.newInstance();

            list.stream()
                    .filter(r -> r.getReferenceType() != ReferenceType.INVALID)
                    .forEach(r -> {
                        if (r.getReferenceType() == ReferenceType.CLASS && r.getValue().get() == null) {
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
    }

    public List<Reference> encodeReferences(@NonNull Object object) {
        Set<Reference> marks = new HashSet<>();
        Deque<Reference> queue = new ArrayDeque<>();
        List<Reference> list = new ArrayList<>();

        marks.add(this.root());
        queue.add(this.root());

        while (!queue.isEmpty()) {
            val ref = queue.remove();
            val obj = ref.getValue(object);

            // Ignore null object.
            if (obj == null) {
                continue;
            }

            this.adj(ref).stream()
                    .filter(r -> r.getReferenceType() == ReferenceType.FIELD)
                    .filter(r -> !r.getEncodingIgnore())
                    .filter(r -> r.getValue(obj) != null)   // Filter null object.
                    .forEach(r -> {
                        r.setValue(r.getValue(obj));
                        list.add(r);
                    });

            for (val r: this.adj(ref)) {
                if (r.getReferenceType() == ReferenceType.CLASS && !marks.contains(r)) {
                    queue.add(r);
                    marks.add(r);
                }
            }
        }

        return list;
    }
}
