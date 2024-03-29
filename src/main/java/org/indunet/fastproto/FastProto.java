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
 * Convert binary data to Java object, or convert Java object to binary data.
 *
 * @author Deng Ran
 * @since 1.0.0
 */
public class FastProto {
    /**
     * Convert binary data to Java object with FastProto annotations.
     *
     * @param bytes Binary data to be converted.
     * @param clazz Java object annotated with FastProto annotations.
     * @return Java object instance.
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
     * Convert Java object to binary data with FastProto method chain.
     *
     * @param bytes Binary data to be converted.
     * @return Decoder which supplies FastProto method chain.
     */
    public static Decoder decode(byte[] bytes) {
        return new Decoder(bytes);
    }

    /**
     * Convert Java object to binary data with FastProto annotations, the length of the binary data is automatically
     * calculated, but reverse addressing is not supported.
     *
     * @param object Java object to be converted.
     * @return Binary data.
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
     * Convert Java object to binary data with FastProto annotations, reverse addressing is supported because of fixed length.
     *
     * @param object Java object to be converted.
     * @param length The length of the binary data.
     * @return Binary data.
     */
    public static byte[] encode(Object object, int length) {
        val bytes = new byte[length];

        encode(object, bytes);

        return bytes;
    }

    /**
     * Convert Java object to binary data with FastProto annotations, reverse addressing is supported because of fixed length.
     *
     * @param object Java object to be converted.
     * @param buffer Binary data will be written to this buffer.
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
     * Create binary block with FastProto method chain.
     *
     * @return Encoder object which supplies chain api.
     */
    public static Encoder create() {
        return new Encoder();
    }

    /**
     * Create fixed-length binary block with FastProto method chain, support reverse addressing.
     *
     * @param  length The length of the byte array
     * @return Encoder which supplies FastProto method chain.
     */
    public static Encoder create(int length) {
        return new Encoder(new byte[length]);
    }
}
