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

package org.indunet.fastproto.pipeline;

import org.indunet.fastproto.exception.CodecError;
import org.indunet.fastproto.exception.DecodeException;
import org.indunet.fastproto.pipeline.decode.DecodeFlow;
import org.indunet.fastproto.pipeline.decode.DecompressFlow;
import org.indunet.fastproto.pipeline.decode.VerifyChecksumFlow;
import org.indunet.fastproto.pipeline.decode.VerifyProtocolVersionFlow;
import org.indunet.fastproto.pipeline.encode.*;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Flow Factory.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class FlowFactory {
    protected static Class<? extends AbstractFlow>[] decodeFlowClasses = new Class[] {
            DecompressFlow.class,
            VerifyChecksumFlow.class,
            VerifyProtocolVersionFlow.class,
            DecodeFlow.class};
    protected static Class<? extends AbstractFlow>[] encodeFlowClasses = new Class[] {
            InferLengthFlow.class,
            EncodeFlow.class,
            WriteProtocolVersionFlow.class,
            WriteChecksumFlow.class,
            CompressFlow.class
    };
    protected static ConcurrentMap<Integer, AbstractFlow> decodeFlows = new ConcurrentHashMap<>();
    protected static ConcurrentMap<Integer, AbstractFlow> encodeFlows = new ConcurrentHashMap<>();

    public static AbstractFlow<CodecContext> createDecode(int codecFeature) {
        return decodeFlows.computeIfAbsent(codecFeature, __ -> create(decodeFlowClasses, codecFeature));
    }

    public static AbstractFlow<CodecContext> createEncode(int codecFeature) {
        return encodeFlows.computeIfAbsent(codecFeature, __ -> create(encodeFlowClasses, codecFeature));
    }

    protected static AbstractFlow create(Class<? extends AbstractFlow>[] flowClasses, int codecFeature) {
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
