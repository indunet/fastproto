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

package org.indunet.fastproto.pipeline;

import lombok.val;
import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.pipeline.decode.*;
import org.indunet.fastproto.pipeline.encode.*;
import org.indunet.fastproto.pipeline.validate.*;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Abstract flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public abstract class AbstractFlow<T> {
    protected static Class<? extends AbstractFlow>[] decodeFlowClasses = new Class[] {
            DecryptFlow.class,
            DecompressFlow.class,
            VerifyChecksumFlow.class,
            VerifyProtocolVersionFlow.class,
            DecodeFlow.class};
    protected static Class<? extends AbstractFlow>[] encodeFlowClasses = new Class[] {
            InferLengthFlow.class,
            EncodeFlow.class,
            WriteProtocolVersionFlow.class,
            WriteChecksumFlow.class,
            CompressFlow.class,
            EncryptFlow.class
    };

    AbstractFlow<T> next = null;

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

    protected static ConcurrentMap<Long, AbstractFlow> decodeFlows = new ConcurrentHashMap<>();
    protected static ConcurrentMap<Long, AbstractFlow> encodeFlows = new ConcurrentHashMap<>();

    protected static AbstractFlow<ValidationContext> validateFlow;

    public static AbstractFlow<CodecContext> getDecodeFlow(long codecFeature) {
        return decodeFlows.computeIfAbsent(codecFeature, __ -> getFlow(decodeFlowClasses, codecFeature));
    }

    public static AbstractFlow<CodecContext> getEncodeFlow(long codecFeature) {
        return encodeFlows.computeIfAbsent(codecFeature, __ -> getFlow(encodeFlowClasses, codecFeature));
    }
    
    public synchronized static AbstractFlow<ValidationContext> getValidateFlow() {
        if (validateFlow == null) {
            val filedFlow = new FieldFlow();
            val decodeFormulaFlow = new DecodeFormulaFlow();
            val encodeFormulaFlow = new EncodeFormulaFlow();
            val arrayFlow = new ArrayFlow();
            val timestampFlow = new TimestampFlow();

            filedFlow.setNext(decodeFormulaFlow)
                    .setNext(encodeFormulaFlow)
                    .setNext(arrayFlow)
                    .setNext(timestampFlow);

            validateFlow = filedFlow;
        }

        return validateFlow;
    }

    protected static AbstractFlow getFlow(Class<? extends AbstractFlow>[] flowClasses, long codecFeature) {
        AbstractFlow[] array = Arrays.stream(flowClasses)
                .map(c -> {
                    try {
                        return (AbstractFlow) c.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new DecodeException(CodecError.FAIL_CREATING_DECODE_FLOW, e);
                    }
                })
                .filter(f -> (f.getFlowCode() & codecFeature) == 0)
                .toArray(AbstractFlow[]::new);

        AbstractFlow flow = array[0];

        for (int i = 1; i < array.length; i ++) {
            flow.setNext(array[i]);
            flow = flow.next;
        }

        return array[0];
    }
}
