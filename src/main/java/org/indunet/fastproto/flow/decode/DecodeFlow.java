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

package org.indunet.fastproto.flow.decode;

import lombok.val;
import org.indunet.fastproto.TypeAssist;
import org.indunet.fastproto.decoder.DecodeContext;
import org.indunet.fastproto.decoder.DecoderFactory;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.flow.AbstractFlow;
import org.indunet.fastproto.flow.CodecContext;

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
    public static final int FLOW_CODE = 0x0008;

    @Override
    public void process(CodecContext context) {
        val assist = context.getTypeAssist();
        val datagram = context.getDatagram();
        val protocolClass = context.getProtocolClass();
        List<DecodeContext> decodeContexts = assist.toDecodeContexts(datagram);

        decodeContexts.parallelStream()
                .forEach(c -> {
                    TypeAssist a = c.getTypeAssist();
                    Function<DecodeContext, ?> func = DecoderFactory.getDecoder(
                            a.getDecoderClass(),
                            a.getDecodeFormula());
                    try {
                        Object value = func.apply(c);
                        Object o = c.getObject();
                        a.setValue(o, value);
                    } catch (DecodeException e) {
                        throw new DecodeException(MessageFormat.format(
                                CodecError.FAIL_DECODING_FIELD.getMessage(), a.getField().toString()), e);
                    }
                });

        context.setObject(assist.getObject(protocolClass));
    }

    public int getFlowCode() {
        return FLOW_CODE;
    }
}
