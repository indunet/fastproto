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
import org.indunet.fastproto.annotation.EnableChecksum;
import org.indunet.fastproto.checksum.Checker;
import org.indunet.fastproto.pipeline.Pipeline;
import org.indunet.fastproto.pipeline.PipelineContext;
import org.indunet.fastproto.pipeline.FlowCode;

/**
 * Write checksum flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public class WriteChecksumFlow extends Pipeline<PipelineContext> {
    @Override
    public void process(PipelineContext context) {
        val object = context.getObject();
        val datagram = context.getDatagram();

        // Check sum.
        if (object.getClass().isAnnotationPresent(EnableChecksum.class)) {
            EnableChecksum checkSum = object.getClass().getAnnotation(EnableChecksum.class);
            Checker checker = Checker.getInstance(checkSum);

            checker.setValue(datagram, object.getClass());
        }

        this.forward(context);
    }

    @Override
    public long getCode() {
        return  FlowCode.WRITE_CHECKSUM_FLOW_CODE;
    }
}
