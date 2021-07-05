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
import org.indunet.fastproto.TypeAssist;
import org.indunet.fastproto.encoder.EncodeContext;
import org.indunet.fastproto.encoder.EncoderFactory;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.EncodeException;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.CodecContext;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Consumer;

/**
 * Encode flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class EncodeFlow extends AbstractFlow<CodecContext> {
    public static final int FLOW_CODE = 0x0200;

    @Override
    public void process(CodecContext context) {
        val assist = context.getTypeAssist();
        val object = context.getObject();
        val datagram = context.getDatagram();

        List<EncodeContext> encodeContexts = assist.toEncodeContexts(object, datagram);

        encodeContexts.stream()
                .forEach(c -> {
                    TypeAssist a = c.getTypeAssist();

                    try {
                        if (a.getEncodeFormula() != null) {
                            Object o = EncoderFactory.getFormula(c.getTypeAssist().getEncodeFormula())
                                    .apply(c.getValue());
                            c.setValue(o);
                        }

                        Consumer<EncodeContext> consumer = EncoderFactory.getEncoder(c.getTypeAssist().getEncoderClass());
                        consumer.accept(c);
                    } catch (EncodeException e) {
                        throw new EncodeException(MessageFormat.format(
                                CodecError.FAIL_ENCODING_FIELD.getMessage(), a.getField().toString()),
                                e);
                    }
                });

        this.nextFlow(context);
    }

    @Override
    public int getFlowCode() {
        return FLOW_CODE;
    }
}
