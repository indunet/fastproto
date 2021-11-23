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

package org.indunet.fastproto.graph.validate;

import lombok.Builder;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * Validation context.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
@Data
@Builder
public class ValidatorContext {
    Field field;
    Annotation typeAnnotation;
    Class<? extends Annotation> typeAnnotationClass;
    Class<? extends Function> decodingFormula;
    Class<? extends Function> encodingFormula;
}
