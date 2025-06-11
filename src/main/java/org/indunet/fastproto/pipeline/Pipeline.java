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

import org.indunet.fastproto.exception.DecodingException;
import org.indunet.fastproto.graph.resolve.validate.ValidatorContext;
import org.indunet.fastproto.pipeline.decode.DecodeFlow;
import org.indunet.fastproto.pipeline.encode.EncodeFlow;

import java.util.Arrays;

/**
 * Abstract flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public abstract class Pipeline<T> {
    protected static Class<? extends Pipeline>[] decodeFlowClasses = new Class[] {
            DecodeFlow.class};
    protected static Class<? extends Pipeline>[] encodeFlowClasses = new Class[] {
            EncodeFlow.class,
    };

    Pipeline<T> next = null;

    public abstract void process(T context);

    public Pipeline<T> setNext(Pipeline<T> next) {
        this.next = next;

        return this.next;
    }

    public void forward(T context) {
        if (next != null) {
            this.next.process(context);
        }
    }

    public Pipeline<T> append(Class<? extends Pipeline<T>> clazz) {
        Pipeline temp = this;

        while (temp.next != null) {
            temp = temp.next;
        }

        try {
            temp.setNext(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DecodingException("Fail creating decoding flow.", e);
        }

        return this;
    }

    @Override
    public String toString() {
        String name = this.getClass().getSimpleName();

        if (this.next != null) {
            return String.format("%s -> %s", name, this.next);
        } else {
            return name;
        }
    }

    public abstract long getCode();

    protected static Pipeline decodePipeline;
    protected static Pipeline encodePipeline;

    static {
        // remove unnecessary flow.
        decodePipeline = new DecodeFlow()
                .append(org.indunet.fastproto.pipeline.checksum.ChecksumDecodeFlow.class);

        // remove unnecessary flow.
        encodePipeline = new EncodeFlow()
                .append(org.indunet.fastproto.pipeline.checksum.ChecksumEncodeFlow.class);
    }

    protected static Pipeline<ValidatorContext> validateFlow;

    public static Pipeline<PipelineContext> getDecodeFlow() {
        return decodePipeline;
    }

    public static Pipeline<PipelineContext> getEncodeFlow() {
        return encodePipeline;
    }

    public static Pipeline create(Class<? extends Pipeline>[] flowClasses, long codecFeature) {
        Pipeline[] array = Arrays.stream(flowClasses)
                .map(c -> {
                    try {
                        return (Pipeline) c.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new DecodingException("Fail creating decoding flow.", e);
                    }
                })
                .filter(f -> (f.getCode() & codecFeature) == 0)
                .toArray(Pipeline[]::new);

        Pipeline flow = array[0];

        for (int i = 1; i < array.length; i ++) {
            flow.setNext(array[i]);
            flow = flow.next;
        }

        return array[0];
    }

    public static <T> Pipeline<T> create(Class<? extends Pipeline<T>>[] pipelines) {
        return create(pipelines, 0);
    }
}
