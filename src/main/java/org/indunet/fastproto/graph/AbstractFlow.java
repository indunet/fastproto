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

package org.indunet.fastproto.graph;

import lombok.val;
import org.indunet.fastproto.graph.resolve.*;

/**
 * Abstract flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public abstract class AbstractFlow<T> {
    AbstractFlow<T> next = null;

    public static AbstractFlow<Reference> getResolveClassFlow() {
        val endianFlow = new EndianFlow();
        val enableFixedLength = new EnableFixedLengthFlow();
        val enableProtocolVersionFlow = new EnableProtocolVersionFlow();
        val enableCryptoFlow = new EnableCryptoFlow();
        val enableCompressFlow = new EnableCompressFlow();
        val enableChecksumFlow = new EnableChecksumFlow();
        val constructorFlow = new ConstructorFlow();
        val codecIgnoreFlow = new CodecIgnoreFlow();

        endianFlow
                .setNext(enableFixedLength)
                .setNext(enableProtocolVersionFlow)
                .setNext(enableCryptoFlow)
                .setNext(enableCompressFlow)
                .setNext(enableChecksumFlow)
                .setNext(constructorFlow)
                .setNext(codecIgnoreFlow);

        return endianFlow;
    }

    public static AbstractFlow<Reference> getResolveFieldFlow() {
        val typeAnnotationFlow = new TypeAnnotationFlow();
        val endianFlow = new EndianFlow();
        val codecFlow = new CodecFlow();
        val codecIgnoreFlow = new CodecIgnoreFlow();

        typeAnnotationFlow.setNext(endianFlow)
                .setNext(codecFlow)
                .setNext(codecIgnoreFlow);

        return typeAnnotationFlow;
    }

    public abstract void process(T context);

    public AbstractFlow<T> setNext(AbstractFlow<T> next) {
        this.next = next;

        return this.next;
    }

    public void nextFlow(T context) {
        if (next != null) {
            this.next.process(context);
        }
    }

    public void end() {
        this.next = null;
    }

    public abstract long getFlowCode();
}
