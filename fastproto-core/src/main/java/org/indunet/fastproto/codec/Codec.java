/*
 * Copyright 2019-2022 indunet.org
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

package org.indunet.fastproto.codec;

import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.io.ByteBufferOutputStream;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


/**
 * Interface for Codec.
 * This interface defines the structure for a Codec in the fastproto library.
 * A Codec is responsible for encoding and decoding data.
 * It provides methods for decoding from a CodecContext and ByteBufferInputStream, and encoding into a ByteBufferOutputStream.
 * It is implemented by various classes to handle different types of data.
 *
 * @author Deng Ran
 * @since 3.2.1
 */
public interface Codec<T> {
    ConcurrentHashMap<Class<? extends Codec>, Codec> codecs = new ConcurrentHashMap<>();
    ConcurrentHashMap<Class<? extends Function>, Function> formulas = new ConcurrentHashMap<>();


    static Codec getCodec(Class<? extends Codec> clazz) {
        return codecs.computeIfAbsent(clazz, __ -> {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new CodecException(String.format("Fail initializing codec %s", clazz.getName()), e);
            }
        });
    }
    
    static Function getFormula(Class<? extends Function> clazz) {
        return formulas.computeIfAbsent(clazz, __ -> {
           try {
               return clazz.newInstance();
           } catch (InstantiationException | IllegalAccessException e) {
               e.printStackTrace();
               throw new CodecException(String.format("Fail initializing formula %s", clazz.getName()), e);
           }
        });
    }

    T decode(CodecContext context, ByteBufferInputStream inputStream);

    void encode(CodecContext context, ByteBufferOutputStream outputStream, T value);
}
