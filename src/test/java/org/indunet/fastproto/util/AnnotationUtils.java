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

package org.indunet.fastproto.util;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.indunet.fastproto.BitOrder;
import org.indunet.fastproto.ByteOrder;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.exception.CodecException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Deng Ran
 * @since 3.0.0
 */
public class AnnotationUtils {
    public static <T extends Annotation> T mock(Class<T> annotationClass, Map<String, Object> values) {
        val proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{annotationClass},
                (object, method, parameters) -> {
                    val name = method.getName();

                    if (values.keySet().contains(name)) {
                        return values.get(name);
                    } else if (name.equals("annotationType")) {
                        return annotationClass;
                    } else {
                        throw new CodecException("Cannot find matching method.");
                    }
                });

        return annotationClass.cast(proxy);
    }

    public static <T extends Annotation> T mock(Class<T> annotationClass, int offset, ByteOrder order) {
        Map<String, Object> map = new HashMap<>();

        map.put("offset", offset);
        map.put("byteOrder", order);

        return mock(annotationClass, map);
    }

    public static <T extends Annotation> T mock(Class<T> annotationClass, int byteOffset, int bitOffset, BitOrder order) {
        Map<String, Object> map = new HashMap<>();

        map.put("byteOffset", byteOffset);
        map.put("bitOffset", bitOffset);
        map.put("bitOrder", order);

        return mock(annotationClass, map);
    }

    public static <T extends Annotation> T mock(Class<T> annotationClass, int offset, int length, ByteOrder order) {
        Map<String, Object> map = new HashMap<>();

        map.put("offset", offset);
        map.put("length", length);
        map.put("byteOrder", order);

        return mock(annotationClass, map);
    }

    public static <T extends Annotation> T mock(Class<T> annotationClass, int value, String fieldName, ProtocolType protocolType) {
        Map<String, Object> map = new HashMap<>();

        map.put("value", value);
        map.put("fieldName", fieldName);
        map.put("protocolType", protocolType);

        return mock(annotationClass, map);
    }

    public static <T extends Annotation> T mock(Class<T> annotationClass, int value, ProtocolType protocolType) {
        Map<String, Object> map = new HashMap<>();
        map.put("value", value);
        map.put("protocolType", protocolType);

        return mock(annotationClass, map);
    }

    public static <T extends Annotation> T mock(Class<T> annotationClass, int value, int length) {
        Map<String, Object> map = new HashMap<>();
        map.put("value", value);
        map.put("length", length);

        return mock(annotationClass, map);
    }

    @SneakyThrows
    public static ProtocolType protocolType(@NonNull Annotation annotation) {
        val field = annotation.getClass().getDeclaredField("protocolType");

        return (ProtocolType) field.get(annotation);
    }
}
