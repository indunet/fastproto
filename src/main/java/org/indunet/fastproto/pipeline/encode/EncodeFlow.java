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
import org.indunet.fastproto.encoder.EncodeContext;
import org.indunet.fastproto.encoder.EncoderFactory;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodingException;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.CodecContext;
import org.indunet.fastproto.pipeline.FlowCode;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Consumer;

/**
 * Encode flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class EncodeFlow extends Pipeline<CodecContext> {
    @Override
    public void process(CodecContext context) {
        // val assist = context.getTypeAssist();
        val graph = context.getGraph();
        val object = context.getObject();
        val datagram = context.getDatagram();

        List<EncodeContext> encodeContexts = graph.encodeContexts(object, datagram);

        encodeContexts
                .forEach(c -> {
                    val ref = c.getReference();

                    try {
                        if (ref.getEncodeFormula() != null) {
                            Object o = EncoderFactory.getFormula(ref.getEncodeFormula())
                                    .apply(c.getValue());
                            c.setValue(o);
                        }

                        Consumer<EncodeContext> consumer = EncoderFactory.getEncoder(ref.getEncoderClass());
                        consumer.accept(c);
                    } catch (EncodingException e) {
                        throw new EncodingException(MessageFormat.format(
                                CodecError.FAIL_ENCODING_FIELD.getMessage(), ref.getField().toString()),
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
