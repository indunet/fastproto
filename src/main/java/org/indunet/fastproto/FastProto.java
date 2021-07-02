/*
 * Copyright 2019-2021 indunet
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
import org.indunet.fastproto.flow.CodecContext;
import org.indunet.fastproto.flow.FlowFactory;

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
     * @param datagram binary message
     * @param clazz    deserialized object
     * @return deserialize object instance
     */
    public static <T> T parseFrom(@NonNull byte[] datagram, @NonNull Class<T> clazz) {
        return parseFrom(datagram, clazz, CodecFeature.DEFAULT);
    }

    public static <T> T parseFrom(@NonNull byte[] datagram, @NonNull Class<T> protocolClass, int codecFeature) {
        TypeAssist assist = TypeAssist.byClass(protocolClass);
        CodecContext context = CodecContext.builder()
                .datagram(datagram)
                .protocolClass(protocolClass)
                .codecFeature(codecFeature)
                .typeAssist(assist)
                .build();
        FlowFactory.createDecode(assist.getCodecFeature() | codecFeature)
                .process(context);

        return (T) context.getObject();
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object serialized object
     * @return binary datagram.
     */
    public static byte[] toByteArray(@NonNull Object object) {
        return toByteArray(object, -1, CodecFeature.DEFAULT);
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object         serialized object
     * @param enableCompress enable compress
     * @return binary datagram.
     */
    @Deprecated
    public static byte[] toByteArray(@NonNull Object object, boolean enableCompress) {
//        TypeAssist assist = assists.computeIfAbsent(object.getClass(), c -> TypeAssist.head(c));
//        int length = assist.getMaxLength();
//        length += CheckerUtils.getSize(object.getClass());
//        length += VersionAssist.getSize(object.getClass());
//
//        return toByteArray(object, length, enableCompress);

        if (enableCompress) {
            return toByteArray(object, -1, CodecFeature.IGNORE_ENABLE_COMPRESS);
        } else {
            return toByteArray(object, -1, CodecFeature.DEFAULT);
        }
    }

    /**
     * Convert object into binary datagram.
     *
     * @param object serialized object
     * @param length the length of the datagram.
     * @return binary datagram.
     */
    public static byte[] toByteArray(@NonNull Object object, int length) {
        return toByteArray(object, length, CodecFeature.DEFAULT);
    }

    public static byte[] toByteArray(@NonNull Object object, int length, int codecFeature) {
        TypeAssist assist = TypeAssist.byClass(object.getClass());
        CodecContext.CodecContextBuilder builder = CodecContext.builder()
                .object(object)
                .protocolClass(object.getClass())
                .codecFeature(codecFeature)
                .typeAssist(assist);
        CodecContext context;

        if (length == -1) {
            context = builder.build();
            FlowFactory.createEncode(assist.getCodecFeature() | codecFeature)
                    .process(context);
        } else {
            context = builder
                    .datagram(new byte[length])
                    .build();
            FlowFactory.createEncode(codecFeature | CodecFeature.NON_INFER_LENGTH)
                    .process(context);
        }

        return context.getDatagram();
    }
}
