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

package org.indunet.fastproto.annotation;

import org.indunet.fastproto.EndianPolicy;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.UInteger16Type;

import java.lang.annotation.*;


/**
 * @author Deng Ran
 * @since 1.5.3
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableVersion {
    Class<?>[] PROTOCOL_TYPES = {
            ProtocolType.UINTEGER8,
            ProtocolType.UINTEGER16,
            ProtocolType.INTEGER
    };

    int value();

    int version();

    Class<? extends Annotation> genericType() default UInteger16Type.class;

    EndianPolicy[] endianPolicy() default {};
}
