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
import org.indunet.fastproto.ProtocolVersionAssist;
import org.indunet.fastproto.TypeAssist;
import org.indunet.fastproto.checksum.CheckerUtils;
import org.indunet.fastproto.exception.AddressingException;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.pipeline.AbstractFlow;
import org.indunet.fastproto.pipeline.CodecContext;

import java.lang.annotation.ElementType;
import java.util.ArrayDeque;

/**
 * Infer length flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class InferLengthFlow extends AbstractFlow<CodecContext> {
    public static final long FLOW_CODE = 0x0100;

    @Override
    public void process(CodecContext context) {
        val queue = new ArrayDeque<TypeAssist>();
        int max = 0;
        queue.add(context.getTypeAssist());

        while (!queue.isEmpty()) {
            val assist = queue.remove();

            assist.getElements().stream()
                    .filter(a -> a.getElementType() == ElementType.TYPE)
                    .forEach(a -> queue.add(a));

            int length = assist.getElements().stream()
                    .filter(a -> a.getElementType() == ElementType.FIELD)
                    .mapToInt(a -> {
                        if (a.getByteOffset() < 0 || a.getLength() < 0) {
                            throw new AddressingException(CodecError.UNABLE_INFER_LENGTH);
                        } else {
                           return a.getByteOffset() + a.getSize() + a.getLength();
                        }
                    }).max()
                    .orElse(0);

            if (length > max) {
                max = length;
            }
        }

        if (max == 0) {
            throw new AddressingException(CodecError.UNABLE_INFER_LENGTH);
        } else {
            max += CheckerUtils.getSize(context.getProtocolClass());
            max += ProtocolVersionAssist.size(context.getTypeAssist());
            context.setDatagram(new byte[max]);
        }

        this.nextFlow(context);
    }

    @Override
    public long getFlowCode() {
        return FLOW_CODE;
    }
}
