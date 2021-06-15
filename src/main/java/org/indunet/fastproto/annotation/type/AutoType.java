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

package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.TypeFlag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Auto type, infer by the field type, support all Java primitive type and their wrapper classes.
 *
 * @author Deng Ran
 * @since 1.4.0
 */
@TypeFlag
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoType {
    int value();

    int bitOffset() default 0;

    int length() default -1;

    ProtocolType dataType() default ProtocolType.LONG;

    TimeUnit unit() default TimeUnit.MILLISECONDS;

    Class<? extends Function>[] afterDecode() default {};

    Class<? extends Function>[] beforeEncode() default {};
}
