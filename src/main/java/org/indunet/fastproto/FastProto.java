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
     * Convert binary message into object.
     *
     * @param datagram      binary message
     * @param protocolClass deserialized object
     * @return deserialize object instance
     */
    public static <T> T parse(@NonNull byte[] datagram, @NonNull Class<T> protocolClass) {
        return parse(datagram, protocolClass, CodecFeature.DEFAULT);
    }

    /**
     * Convert binary message into object.
     *
     * @param datagram      binary message
     * @param protocolClass deserialized object
     * @param codecFeatures codec feature code
     * @return deserialize object instance
     */
    public static <T> T parse(@NonNull byte[] datagram, @NonNull Class<T> protocolClass, long... codecFeatures) {
        val graph = Resolver.resolve(protocolClass);
        val codecFeature = CodecFeature.of(codecFeatures);
        val context = PipelineContext.builder()
                .datagram(datagram)
                .protocolClass(protocolClass)
                .codecFeature(codecFeature)
                .graph(graph)
                .build();

        val feature = CodecFeature.of(graph.root());

        Pipeline.getDecodeFlow(feature | codecFeature)
                .process(context);

        return context.getObject(protocolClass);
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object serialized object
     * @return binary datagram.
     */
    public static byte[] toBytes(@NonNull Object object) {
        return toBytes(object, CodecFeature.DEFAULT);
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object serialized object
     * @param length the length of the datagram.
     * @return binary datagram.
     */
    public static byte[] toBytes(@NonNull Object object, int length) {
        return toBytes(object, new byte[length], CodecFeature.DEFAULT);
    }

    public static void toBytes(@NonNull Object object, byte[] buffer) {
        toBytes(object, buffer, CodecFeature.DEFAULT);
    }

    public static byte[] toBytes(@NonNull Object object, long... codecFeatures) {
        val graph = Resolver.resolve(object.getClass());
        val codecFeature = CodecFeature.of(codecFeatures);
        val context = PipelineContext.builder()
                .object(object)
                .protocolClass(object.getClass())
                .codecFeature(codecFeature)
                .graph(graph)
                .build();
        val feature = CodecFeature.of(graph.root());

        Pipeline.getEncodeFlow(feature | codecFeature)
                .process(context);

        return context.getDatagram();
    }

    public static byte[] toBytes(@NonNull Object object, int length, long... codecFeatures) {
        return toBytes(object, new byte[length], codecFeatures);
    }

    public static byte[] toBytes(@NonNull Object object, byte[] buffer, long... codecFeatures) {
        val graph = Resolver.resolve(object.getClass());
        val codecFeature = CodecFeature.of(codecFeatures);
        val context = PipelineContext.builder()
                .object(object)
                .protocolClass(object.getClass())
                .codecFeature(codecFeature)
                .datagram(buffer)
                .graph(graph)
                .build();
        val feature = CodecFeature.of(graph.root());

        Pipeline.getEncodeFlow(feature | CodecFeature.NON_INFER_LENGTH | codecFeature)
                .process(context);

        return context.getDatagram();
    }
}
