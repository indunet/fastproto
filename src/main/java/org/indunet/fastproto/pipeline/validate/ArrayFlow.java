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
import org.indunet.fastproto.annotation.type.ArrayType;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.FlowCode;
import org.indunet.fastproto.pipeline.ValidationContext;
import org.indunet.fastproto.util.TypeUtils;

import java.util.Arrays;

/**
 * Array type validation flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class ArrayFlow extends AbstractFlow<ValidationContext> {
    @Override
    public void process(ValidationContext context) {
        val typeAnnotation = context.getTypeAnnotation();

        if (typeAnnotation instanceof ArrayType) {
            val protocolType = ((ArrayType) typeAnnotation).protocolType();
            val protocolTypes = TypeUtils.protocolTypes(typeAnnotation);

            Arrays.stream(protocolTypes)
                    .filter(t -> t == protocolType)
                    .findAny()
                    .orElseThrow(CodecException::new);
        }

        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return FlowCode.ARRAY_FLOW_CODE;
    }
}
