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
import org.indunet.fastproto.exception.ResolveException;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.pipeline.FlowCode;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.PipelineContext;

/**
 * Infer length flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class InferLengthFlow extends Pipeline<PipelineContext> {
    @Override
    public void process(PipelineContext context) {
        val graph = context.getGraph();

        int max = graph.stream()
                .filter(r -> r.getReferenceType() == Reference.ReferenceType.FIELD)
                .mapToInt(r -> {
                    val type = r.getProtocolType();

                    if (type.offset() < 0 || type.length() < 0) {
                        throw new ResolveException(CodecError.UNABLE_INFER_LENGTH);
                    } else {
                        return type.offset() + type.size() + type.length();
                    }
                }).max()
                .orElse(0);

        if (max == 0) {
            throw new ResolveException(CodecError.UNABLE_INFER_LENGTH);
        } else {
            context.setDatagram(new byte[max]);
        }

        this.forward(context);
    }

    @Override
    public long getCode() {
        return FlowCode.INFER_LENGTH_FLOW_CODE;
    }
}
