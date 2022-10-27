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

package org.indunet.fastproto.pipeline.encode;

import lombok.val;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.PipelineContext;
import org.indunet.fastproto.pipeline.FlowCode;

import java.text.MessageFormat;


/**
 * Encode flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class EncodeFlow extends Pipeline<PipelineContext> {
    @Override
    public void process(PipelineContext context) {
        val graph = context.getGraph();
        val object = context.getObject();
        val bytes = context.getBytes();
        val refs = graph.getValidReferences();

        graph.copy(object);
        refs.stream()
                .filter(r -> !r.getEncodingIgnore())
                .filter(r -> r.getValue().get() != null)
                .forEach(r -> {
                    try {
                        r.encode(bytes);
                    } catch (EncodingException e) {
                        throw new EncodingException(MessageFormat.format(
                                CodecError.FAIL_ENCODING_FIELD.getMessage(), r.getField().toString()),
                                e);
                    }
                });

        this.forward(context);
    }

    @Override
    public long getCode() {
        return FlowCode.ENCODE_FLOW_CODE;
    }
}
