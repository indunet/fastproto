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

package org.indunet.fastproto;

import lombok.NonNull;
import lombok.val;
import org.indunet.fastproto.graph.Resolver;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.PipelineContext;

/**
 * FastProto API.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
public class FastProto {
    /**
     * Convert byte array into object.
     *
     * @param bytes      byte array
     * @param clazz class of deserialized object
     * @return deserialize object
     */
    public static <T> T parse(@NonNull byte[] bytes, @NonNull Class<T> clazz) {
        val graph = Resolver.resolve(clazz);
        val context = PipelineContext.builder()
                .bytes(bytes)
                .clazz(clazz)
                .graph(graph)
                .build();

        Pipeline.getDecodeFlow()
                .process(context);

        return context.getObject(clazz);
    }

    /**
     * Convert object into byte array.
     *
     * @param object serialized object
     * @return byte array
     */
    public static byte[] toBytes(Object object) {
        val graph = Resolver.resolve(object.getClass());
        val context = PipelineContext.builder()
                .object(object)
                .clazz(object.getClass())
                .graph(graph)
                .build();

        Pipeline.getEncodeFlow()
                .process(context);

        return context.getBytes();
    }

    /**
     * Convert object into byte array.
     *
     * @param object serialized object
     * @param length the length of byte array
     * @return byte array
     */
    public static byte[] toBytes(Object object, int length) {
        val bytes = new byte[length];

        toBytes(object, bytes);

        return bytes;
    }

    /**
     * Convert object into byte array.
     *
     * @param object serialized object
     * @param buffer write result into buffer
     * @return byte array
     */
    public static void toBytes(Object object, byte[] buffer) {
        val graph = Resolver.resolve(object.getClass());
        val context = PipelineContext.builder()
                .object(object)
                .clazz(object.getClass())
                .bytes(buffer)
                .graph(graph)
                .build();

        Pipeline.getEncodeFlow()
                .process(context);
    }
}
