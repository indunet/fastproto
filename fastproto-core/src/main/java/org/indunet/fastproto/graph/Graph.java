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
import org.indunet.fastproto.exception.ResolvingException;
import org.indunet.fastproto.graph.Reference.ConstructorType;
import org.indunet.fastproto.graph.Reference.ReferenceType;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents a Graph of References.
 * It is used to manage and manipulate a collection of Reference objects.
 *
 * @author Deng Ran
 * @since 2.5.0
 */
public class Graph {
    protected LinkedHashMap<Reference, ArrayList<Reference>> adj = new LinkedHashMap<>();
    protected List<Reference> validReferences;


    protected Graph() {

    }

    public boolean contains(@NonNull Class<?> protocolClass) {
        return this.adj.keySet().stream()
                .anyMatch(s -> s.getProtocolClass() == protocolClass);
    }

    public boolean contains(@NonNull Reference reference) {
        return this.adj.containsKey(reference);
    }

    public void addClass(@NonNull Reference reference) {
        if (!this.contains(reference)) {
            this.adj.put(reference, new ArrayList<>());
        }
    }

    public void addReference(@NonNull Reference parent, @NonNull Reference child) {
        List<Reference> children = adj.computeIfAbsent(parent, k -> new ArrayList<>());

        children.add(child);
    }

    public void addReference(@NonNull Reference reference) {
        val key = this.adj.keySet().stream()
                .filter(s -> s.getProtocolClass()
                        == reference.getField().getDeclaringClass())
                .findAny()
                .orElseThrow(ResolvingException::new);

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
        StringBuilder sb = new StringBuilder();

        this.adj.forEach((key, value) -> {
            sb.append(key).append("\n");

            value.forEach(s -> sb.append("\t").append(s).append("\n"));
        });

        System.out.println(sb);
    }

    public synchronized List<Reference> getValidReferences() {
        if (this.validReferences == null) {
            this.validReferences = this.adj.values().stream()
                    .flatMap(Collection::stream)
                    .filter(r -> r.referenceType == Reference.ReferenceType.FIELD)
                    .collect(Collectors.toList());
        }

        return this.validReferences;
    }

    public Stream<Reference> stream() {
        val list = new ArrayList<Reference>();

        this.foreach(list::add);

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
                    .forEach(consumer);
        }
    }

    public Object generate() {
        val root = this.root();
        this.generate(root);

        val object = root.getValue().get();
        this.foreach(Reference::clear);

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

            reference.newInstance(list.toArray(new Reference[0]));
        }
    }

    public void copy(Object object) {
        Set<Reference> marks = new HashSet<>();
        Deque<Reference> queue = new ArrayDeque<>();

        marks.add(this.root());
        queue.add(this.root());

        while (!queue.isEmpty()) {
            val ref = queue.remove();
            val obj = ref.parse(object);

            // Ignore null object.
            if (obj == null) {
                continue;
            }

            this.adj(ref).stream()
                    .filter(r -> r.getReferenceType() == ReferenceType.FIELD)
                    .filter(r -> !r.getEncodingIgnore())
                    .forEach(r -> r.setValue(r.parse(obj)));

            for (val r: this.adj(ref)) {
                if (r.getReferenceType() == ReferenceType.CLASS && !marks.contains(r)) {
                    queue.add(r);
                    marks.add(r);
                }
            }
        }
    }
}
