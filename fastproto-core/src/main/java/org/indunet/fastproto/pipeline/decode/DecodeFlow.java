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

package org.indunet.fastproto.pipeline.decode;

import lombok.val;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.graph.Graph;
import org.indunet.fastproto.io.ByteBufferInputStream;
import org.indunet.fastproto.pipeline.FlowCode;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.PipelineContext;

/**
 * Decode flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class DecodeFlow extends Pipeline<PipelineContext> {
    @Override
    public void process(PipelineContext context) {
        val reference = context.getGraph().root();
        val inputStream = context.getInputStream();

        context.setObject(linearDecode(inputStream, context.getGraph()));

        this.forward(context);
    }

    public Object linearDecode(ByteBufferInputStream inputStream, Graph graph) {
        val refs = graph.getValidReferences();

        refs.stream()
                .filter(r -> !r.getDecodingIgnore())
                .forEach(r -> {
                    try {
                        r.decode(inputStream);
                    } catch (DecodingException e) {
                        throw new DecodingException(String.format("Fail decoding field %s", r.getField().toString()), e);
                    }
                });

        return graph.generate();
    }

    @Override
    public long getCode() {
        return FlowCode.DECODE_FLOW_CODE;
    }
}
