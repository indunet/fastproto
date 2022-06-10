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

import lombok.val;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.TimeType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodingException;

import java.util.concurrent.TimeUnit;

/**
 * Abstract flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class TimestampValidator extends TypeValidator {
    @Override
    public void process(ValidatorContext context) {
        val protocolType = context.getProtocolType();

        if (protocolType instanceof TimeType) {
            val unit = ((TimeType) protocolType).unit();
            val genericType = ((TimeType) protocolType).genericType();

            val condition1 = genericType == ProtocolType.UINT32 && unit == TimeUnit.SECONDS;
            val condition2 = genericType == ProtocolType.LONG && unit == TimeUnit.MILLISECONDS;

            if (!condition1 && !condition2) {
                throw new DecodingException(CodecError.ILLEGAL_TIMESTAMP_PARAMETERS);
            }
        }

        this.forward(context);
    }
}
