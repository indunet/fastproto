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

package org.indunet.fastproto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

/**
 * Annotation for decoding formula. The result of parsing is substituted into this formula,
 * and the final result is assigned to the field with this annotation.
 *
 * @author Deng Ran
 * @since 3.5.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecodingFormula {
    /**
     * The decoding formula class which must implement the java.util.function.Function interface.
     */
    Class<? extends Function<?, ?>>[] value() default {};

    /**
     * Java lambda expression in the form of string, FastProto can automatically compile it into class.
     */
    String lambda() default "";
}
