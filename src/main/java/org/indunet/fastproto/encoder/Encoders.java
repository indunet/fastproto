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

package org.indunet.fastproto.encoder;

import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.exception.EncodeException.EncodeError;

import java.text.MessageFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Deng Ran
 * @since 1.0.0
 */
public class Encoders {
    protected static ConcurrentHashMap<Class<? extends TypeEncoder>, TypeEncoder> encoders = new ConcurrentHashMap<>();
    protected static ConcurrentHashMap<Class<? extends Function>, Function> formulas = new ConcurrentHashMap<>();

    public static Consumer<EncodeContext> getEncoder(Class<? extends TypeEncoder> clazz) {
        return encoders.computeIfAbsent(clazz, c -> {
            try {
                return c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new EncodeException(
                        MessageFormat.format(EncodeError.FAIL_INITIALIZING_ENCODER.getMessage(), c.getName()), e);
            }
        })::encode;
    }

    public static <T, R> Function<? super T, ? extends R> getFormula(Class<? extends Function> clazz) {
        return formulas.computeIfAbsent(clazz, c -> {
            try {
                return c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new EncodeException(
                        MessageFormat.format(EncodeError.FAIL_INITIALIZING_ENCODE_FORMULA.getMessage(), c.getName()), e);
            }
        });
    }
}
