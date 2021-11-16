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
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.DecoderFactory;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.graph.Reference;
import org.indunet.fastproto.graph.ReferenceGraph;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.CodecContext;
import org.indunet.fastproto.pipeline.FlowCode;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Function;

/**
 * Decode flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class DecodeFlow extends AbstractFlow<CodecContext> {
    @Override
    public void process(CodecContext context) {
        // val assist = context.getTypeAssist();
        val reference = context.getReferenceGraph().root();
        val datagram = context.getDatagram();

        context.setObject(linearDecode(datagram, context.getReferenceGraph()));

        this.nextFlow(context);
    }

    public Object linearDecode(byte[] datagram, ReferenceGraph graph) {
        List<DecodeContext> decodeContexts = graph.decodeContexts(datagram);

        decodeContexts
                .forEach(c -> {
                    Reference r = c.getReference();
                    Function<DecodeContext, ?> func = DecoderFactory.getDecoder(
                            r.getDecoderClass(),
                            r.getDecodeFormula());

                    try {
                        Object value = func.apply(c);

                        r.setValue(value);
                    } catch (DecodingException e) {
                        throw new DecodingException(MessageFormat.format(
                                CodecError.FAIL_DECODING_FIELD.getMessage(), r.getField().toString()), e);
                    }
                });

        return graph.generate();
    }

    @Override
    public long getFlowCode() {
        return FlowCode.DECODE_FLOW_CODE;
    }
}
