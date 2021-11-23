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
import org.indunet.fastproto.annotation.Decoder;
import org.indunet.fastproto.annotation.Encoder;
import org.indunet.fastproto.annotation.TypeFlag;
import org.indunet.fastproto.annotation.Validator;
import org.indunet.fastproto.decoder.TimestampDecoder;
import org.indunet.fastproto.encoder.TimestampEncoder;
import org.indunet.fastproto.graph.validate.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Timestamp type, corresponding to Java java.sql.Timestamp.
 *
 * @author Deng Ran
 * @see TypeFlag
 * @since 1.1.0
 */
@TypeFlag
@Decoder(TimestampDecoder.class)
@Encoder(TimestampEncoder.class)
@Validator({FieldValidator.class, DecodingFormulaValidator.class, EncodingFormulaValidator.class, TimestampValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimestampType {
    Type[] JAVA_TYPES = {Timestamp.class, Date.class};
    ProtocolType[] PROTOCOL_TYPES = {ProtocolType.UINTEGER32, ProtocolType.LONG};

    int value();

    ProtocolType protocolType() default ProtocolType.LONG;

    TimeUnit unit() default TimeUnit.MILLISECONDS;

    Class<? extends Function<Timestamp, ?>>[] decodingFormula() default {};

    Class<? extends Function<?, Timestamp>>[] encodingFormula() default {};

    String description() default "";
}
