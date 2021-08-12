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
import org.indunet.fastproto.annotation.type.ListType;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.CodecException;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.FlowCode;
import org.indunet.fastproto.pipeline.ValidationContext;

import java.lang.reflect.ParameterizedType;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Abstract flow.
 *
 * @author Deng Ran
 * @since 2.3.0
 */
public class ListFlow extends AbstractFlow<ValidationContext> {
    @Override
    public void process(ValidationContext context) {
        val typeAnnotation = context.getTypeAnnotation();

        if (typeAnnotation instanceof ListType) {
            val listType = (ListType) typeAnnotation;
            val protocolType = listType.protocolType();

            if (listType.beforeEncode().length == 0 || listType.afterDecode().length == 0) {
                if (!Arrays.stream(ListType.PROTOCOL_TYPES)
                        .anyMatch(t -> t == protocolType)) {
                    throw new CodecException(MessageFormat.format(
                            CodecError.LIST_UNSUPPORTED_DATA_TYPE.getMessage(), protocolType.toString()
                    ));
                }

                val field = context.getField();
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Class<?> typeArgument = (Class<?>)parameterizedType.getActualTypeArguments()[0];

                if (!protocolType.match(typeArgument)) {
                    throw new CodecException(MessageFormat.format(
                            CodecError.LIST_UNSUPPORTED_DATA_TYPE.getMessage(), typeArgument.toString()
                    ));
                }
            }
        }

        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return FlowCode.LIST_FLOW_CODE;
    }
}
