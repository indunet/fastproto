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

package org.indunet.fastproto.pipeline.validate;

import lombok.val;
import org.indunet.fastproto.ProtocolType;
import org.indunet.fastproto.annotation.type.TimestampType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.FlowCode;
import org.indunet.fastproto.pipeline.ValidationContext;

import java.util.concurrent.TimeUnit;

/**
 * Abstract flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class TimestampFlow extends AbstractFlow<ValidationContext> {
    @Override
    public void process(ValidationContext context) {
        val typeAnnotation = context.getTypeAnnotation();

        if (typeAnnotation instanceof TimestampType) {
            val protocolType = ((TimestampType) typeAnnotation).protocolType();
            val unit = ((TimestampType) typeAnnotation).unit();

            val condition1 = protocolType == ProtocolType.UINTEGER32 && unit == TimeUnit.SECONDS;
            val condition2 = protocolType == ProtocolType.LONG && unit == TimeUnit.MILLISECONDS;

            if (!condition1 && !condition2) {
                throw new ResolveException(CodecError.ILLEGAL_TIMESTAMP_PARAMETERS);
            }
        }

        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return FlowCode.TIMESTAMP_FLOW_CODE;
    }
}
