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

import lombok.val;
import org.indunet.fastproto.graph.Resolver;
import org.indunet.fastproto.io.ByteBuffer;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.PipelineContext;

/**
 * This class provides methods to convert binary data into a Java object and vice versa.
 * The conversion process utilizes FastProto annotations.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
public class FastProto {
    /**
     * Converts binary data into a Java object with FastProto annotations.
     *
     * @param <T>   The type of the Java object that the binary data will be converted into.
     * @param bytes The binary data to be converted.
     * @param clazz The class of the Java object, annotated with FastProto annotations.
     * @return An instance of the Java object.
     */
    public static <T> T decode(byte[] bytes, Class<T> clazz) {
        val graph = Resolver.resolve(clazz);
        val context = PipelineContext.builder()
                .inputStream(new ByteBufferInputStream(bytes))
                .clazz(clazz)
                .graph(graph)
                .build();

        Pipeline.getDecodeFlow()
                .process(context);

        return (T) context.getObject();
    }

    /**
     * Converts a Java object into binary data using the FastProto method chain.
     *
     * @param bytes The binary data that will be converted.
     * @return A Decoder instance that provides a FastProto method chain for further operations.
     */
    public static Decoder decode(byte[] bytes) {
        return new Decoder(bytes);
    }

    /**
     * Converts a Java object into binary data using FastProto annotations. The binary data's length is automatically determined.
     * Note: This method does not support reverse addressing.
     *
     * @param object The Java object to be converted, which should be annotated with FastProto annotations.
     * @return The resulting binary data from the conversion.
     */
    public static byte[] encode(Object object) {
        val graph = Resolver.resolve(object.getClass());
        val context = PipelineContext.builder()
                .outputStream(new ByteBufferOutputStream())
                .object(object)
                .clazz(object.getClass())
                .graph(graph)
                .build();

        Pipeline.getEncodeFlow()
                .process(context);

        return context.getOutputStream()
                .toByteBuffer()
                .toBytes();
    }

    /**
     * Transforms a Java object into binary data using FastProto annotations. This method supports reverse addressing due to the fixed length of the binary data.
     *
     * @param object The Java object to be converted. It should be annotated with FastProto annotations.
     * @param length The predetermined length of the resulting binary data.
     * @return The binary data resulting from the conversion of the Java object.
     */
    public static byte[] encode(Object object, int length) {
        val bytes = new byte[length];

        encode(object, bytes);

        return bytes;
    }

    /**
     * Converts a Java object into binary data using FastProto annotations. This method supports reverse addressing due to the fixed length of the binary data.
     *
     * @param object The Java object to be converted. It should be annotated with FastProto annotations.
     * @param buffer The buffer where the resulting binary data will be written.
     */
    public static void encode(Object object, byte[] buffer) {
        val graph = Resolver.resolve(object.getClass());
        val context = PipelineContext.builder()
                .object(object)
                .clazz(object.getClass())
                .outputStream(new ByteBufferOutputStream(new ByteBuffer(buffer)))
                .graph(graph)
                .build();

        Pipeline.getEncodeFlow()
                .process(context);
    }

    /**
     * Generates a binary block using the FastProto method chain.
     *
     * @return An Encoder instance that provides a chain API for further operations.
     */
    public static Encoder create() {
        return new Encoder();
    }

    /**
     * Creates a binary block of a fixed length using the FastProto method chain. This method supports reverse addressing.
     *
     * @param length The predetermined length of the binary block.
     * @return An Encoder instance that provides a FastProto method chain for further operations.
     */
    public static Encoder create(int length) {
        return new Encoder(new byte[length]);
    }
}
