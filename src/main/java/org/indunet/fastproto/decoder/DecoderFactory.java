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

package org.indunet.fastproto.decoder;

import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.exception.DecodeException.DecodeError;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class DecoderFactory {
    protected static ConcurrentHashMap<Class<? extends TypeDecoder>, TypeDecoder<?>> decoders = new ConcurrentHashMap<>();
    protected static ConcurrentHashMap<Class<? extends Function>, Function> formulas = new ConcurrentHashMap<>();

    public static Function<DecodeContext, ?> getDecoder(Class<? extends TypeDecoder> clazz) {
        return decoders.computeIfAbsent(clazz, c -> {
            try {
                return c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new DecodeException(
                        MessageFormat.format(DecodeError.FAIL_INITIALIZING_DECODER.getMessage(), clazz.getName()), e);
            }
        })::decode;
    }

    public static <T, R> Function<T, R> getFormula(Class<? extends Function> clazz) {
        return formulas.computeIfAbsent(clazz, c -> {
            try {
                return c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new DecodeException(
                        MessageFormat.format(DecodeError.FAIL_INITIALIZING_DECODE_FORMULA.getMessage(), clazz.getName()), e);
            }
        });
    }

    public static Function<DecodeContext, ?> getDecoder(Class<? extends TypeDecoder> decoderClass, Class<? extends Function> formulaClass) {
        if (formulaClass != null) {
            return getDecoder(decoderClass).andThen(getFormula(formulaClass));
        } else {
            return getDecoder(decoderClass);
        }
    }
}
