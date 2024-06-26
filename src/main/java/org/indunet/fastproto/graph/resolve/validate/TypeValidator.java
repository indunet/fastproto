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

package org.indunet.fastproto.graph.resolve.validate;

import lombok.val;
import org.indunet.fastproto.annotation.Validator;
import org.indunet.fastproto.pipeline.Pipeline;

import java.util.concurrent.ConcurrentHashMap;

/**
 * TypeValidator Class.
 * This abstract class is a pipeline for validating types in the context.
 * It provides a static method to create a TypeValidator instance based on the provided Validator annotation.
 * The created TypeValidator instance is cached in a ConcurrentHashMap for future use.
 * This class extends the Pipeline class and overrides the getCode method to implement its functionality.
 *
 * @author Chance
 * @since 1.0.0
 */
public abstract class TypeValidator extends Pipeline<ValidatorContext> {
    protected final static ConcurrentHashMap<Class<?>[], TypeValidator> validators = new ConcurrentHashMap<>();

    @Override
    public long getCode() {
        return 0;
    }

    public static TypeValidator create(Validator validator) {
        val classes = validator.value();

        return validators.computeIfAbsent(classes,  __ -> (TypeValidator) TypeValidator.create(classes));
    }
}
