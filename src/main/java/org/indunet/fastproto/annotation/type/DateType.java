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

package org.indunet.fastproto.annotation.type;

import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.decoder.DateDecoder;
import org.indunet.fastproto.encoder.DateEncoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Date type, corresponding to Java java.util.Date
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 2.2.0
 */
@TypeFlag
@Decoder(DateDecoder.class)
@Encoder(DateEncoder.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateType {
    Type[] JAVA_TYPES = {Date.class};
    ProtocolType[] PROTOCOL_TYPES = {ProtocolType.UINTEGER32, ProtocolType.LONG};

    int value();

    ProtocolType protocolType() default ProtocolType.LONG;

    TimeUnit unit() default TimeUnit.MILLISECONDS;

    Class<? extends Function<Date, ?>>[] afterDecode() default {};

    Class<? extends Function<?, Date>>[] beforeEncode() default {};
}
